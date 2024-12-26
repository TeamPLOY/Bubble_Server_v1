package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.entity.Users;
import com.laundering.laundering_server.domain.member.repository.UsersRepository;
import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.notification.repository.NotifiReservationRepository;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.ReservationListResponse;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.ReservationSummaryResponse;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final NotifiReservationRepository notifiReservationRepository;

    private static final List<String> MACHINES = List.of("세탁기1", "세탁기2", "세탁기3", "세탁기4");

    public void reservation(Long userId, LocalDate date, String machine) {
        // 사용자 조회
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 동일 날짜에 이미 예약이 있는지 확인
        Optional<Reservation> existingReservation = reservationRepository.findByUserIdAndDateAndCancelFalse(userId, date);
        if (existingReservation.isPresent()) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS);
        }

        // 주 시작일과 끝일 계산 (예: 일요일 시작, 토요일 끝)
        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SATURDAY);

        // 해당 주에 예약이 있는지 확인
        List<Reservation> weeklyReservations = reservationRepository.findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);
        if (!weeklyReservations.isEmpty()) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS_THIS_WEEK);
        }

        // 새로운 예약 생성
        reservationRepository.save(Reservation.builder()
                .userId(userId)        // 예약한 사용자 ID
                .date(date)           // 예약 날짜 (현재 날짜)
                .cancel(false)
                .washingRoom(user.getWashingRoom()) // User 테이블에서 조회한 washingRoom 값 설정
                .machine(machine)
                .build()
        );
    }

    public void cancelReservation(Long userId, LocalDate date) {
        // userId와 date, isCancel이 false인 예약 조회
        Reservation reservation = reservationRepository.findByUserIdAndDateAndCancelFalse(userId, date)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // isCancel을 true로 업데이트
        reservation.setCancel(true);

        // 변경사항을 데이터베이스에 저장
        reservationRepository.save(reservation);
    }


    public List<ReservationSummaryResponse> getReservation(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        String washingRoom = user.getWashingRoom();

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();

        LocalDate startTime;
        LocalDate endTime;

        // 현재 시간이 일요일 22:00~23:59 사이인지 확인
        if (today == DayOfWeek.SUNDAY && now.getHour() >= 22) {
            // 다음 주 월요일부터 목요일까지 설정
            startTime = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toLocalDate();
            endTime = startTime.plusDays(3); // 월요일부터 목요일까지
        } else {
            // 이번 주 월요일부터 목요일까지 설정
            startTime = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
            endTime = startTime.plusDays(3); // 월요일부터 목요일까지
        }

        // 예약 기록 조회
        List<Reservation> reservations = reservationRepository.findByWashingRoomAndDateBetweenAndCancelFalse(washingRoom, startTime, endTime);

        // 월요일부터 목요일에 해당하는 날짜만 필터링
        List<LocalDate> allWeekdays = startTime.datesUntil(endTime.plusDays(1))
                .filter(date -> {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    return dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.TUESDAY ||
                            dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.THURSDAY;
                })
                .collect(Collectors.toList());

        // 세탁기 예약 상태에 대한 맵핑
        Map<LocalDate, Set<Integer>> reservationMap = reservations.stream()
                .collect(Collectors.groupingBy(
                        Reservation::getDate,
                        Collectors.mapping(this::extractMachineId, Collectors.toSet())
                ));

        // 월요일부터 목요일에 대해서만 세탁기 예약 상태를 배열로 설정
        List<ReservationSummaryResponse> summaryList = allWeekdays.stream()
                .map(date -> {
                    boolean[] washingMachinesReserved = new boolean[4]; // 4개의 세탁기 (index 0: 세탁기1, index 1: 세탁기2, ...)

                    // 오늘 이전 날짜들은 모두 예약된 상태로 설정 (true)
                    if (date.isBefore(now.toLocalDate())) {
                        Arrays.fill(washingMachinesReserved, true); // 이전 날짜들은 모두 예약된 상태
                    } else {
                        // 그 외의 경우는 기존 예약 데이터에 따라 상태 설정
                        Set<Integer> reservedMachines = reservationMap.getOrDefault(date, Collections.emptySet());

                        // 예약된 세탁기 번호를 true로 설정
                        for (Integer machineId : reservedMachines) {
                            if (machineId >= 1 && machineId <= 4) {
                                washingMachinesReserved[machineId - 1] = true;
                            }
                        }
                    }

                    return new ReservationSummaryResponse(
                            date,
                            date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
                            washingMachinesReserved
                    );
                })
                .collect(Collectors.toList());

        return summaryList;
    }


    // 세탁기 이름에서 ID 추출
    private Integer extractMachineId(Reservation reservation) {
        String machine = reservation.getMachine();

        // 정규 표현식으로 "세탁기" 뒤에 오는 숫자 추출 (예: "세탁기2" -> 2)
        Pattern pattern = Pattern.compile("세탁기(\\d+)");
        Matcher matcher = pattern.matcher(machine);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1)); // 추출된 숫자 반환
        } else {
            return -1; // "세탁기" 번호가 없으면 -1 반환
        }
    }


    public boolean getIsReserved(Long userId) {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        // 현재 요일이 일요일이고 시간대가 22:00 ~ 23:59인지 확인
        boolean isSundayLateEvening = dayOfWeek == DayOfWeek.SUNDAY &&
                now.toLocalTime().isAfter(LocalTime.of(22, 0)) &&
                now.toLocalTime().isBefore(LocalTime.of(23, 59, 59));

        // 기간 설정을 위한 날짜 계산
        LocalDate startDate;
        LocalDate endDate;

        if (isSundayLateEvening) {
            // 이번 주 일요일 22:00부터 다음 주 일요일 21:59까지
            startDate = now.toLocalDate();
            endDate = now.plusWeeks(1).with(DayOfWeek.SUNDAY).minusHours(2).toLocalDate();
        } else {
            // 저번 주 일요일 22:00부터 이번 주 일요일 21:59까지
            startDate = now.minusWeeks(1).with(DayOfWeek.SUNDAY).toLocalDate();
            endDate = now.with(DayOfWeek.SUNDAY).minusHours(2).toLocalDate();
        }

        // 해당 기간 동안 userId로 예약된 내역이 있는지 조회
        List<Reservation> reservations = reservationRepository.findByUserIdAndDateBetweenAndCancelFalse(userId, startDate, endDate);

        // 예약 내역이 존재하면 true 반환
        return !reservations.isEmpty();
    }
}
