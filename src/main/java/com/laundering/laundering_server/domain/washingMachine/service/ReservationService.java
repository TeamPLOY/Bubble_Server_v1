package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.entity.Users;
import com.laundering.laundering_server.domain.member.repository.UsersRepository;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.ReservationSummaryResponse;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationLogRepository;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationLogRepository reservationLogRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private final UsersRepository usersRepository;

//    public void reservation(Long userId, LocalDate date) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
//
//        // userId와 오늘 날짜로 예약 조회
//        Optional<Reservation> existingReservation = reservationRepository.findByUserIdAndDate(userId, date);
//
//        // 예약이 이미 존재하면 처리
//        if (existingReservation.isPresent()) {
//            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS);
//        }
//
//        // 주 시작일과 끝일 계산 (예: 일요일 시작, 토요일 끝)
//        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
//        LocalDate endOfWeek = date.with(DayOfWeek.SATURDAY);
//
//        // 해당 주에 예약이 있는지 확인
//        List<Reservation> weeklyReservations = reservationRepository.findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);
//
//        if (!weeklyReservations.isEmpty()) {
//            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS_THIS_WEEK);
//        }
//
//        // 새로운 예약 생성
//        Reservation reservation = Reservation.builder()
//                .userId(userId)        // 예약한 사용자 ID
//                .date(date)           // 예약 날짜 (현재 날짜)
//                .washingRoom(user.getWashingRoom()) // User 테이블에서 조회한 washingRoom 값 설정
//                .build();
//
//        ReservationLog reservationLog = ReservationLog.builder()
//                .userId(userId)
//                .isCancel(false)
//                .date(LocalDateTime.now())
//                .resDate(date)
//                .washingRoom(user.getWashingRoom())
//                .build();
//
//        // 예약을 데이터베이스에 저장
//        reservationRepository.save(reservation);
//        reservationLogRepository.save(reservationLog);
//    }

    public void reservation(Long userId, LocalDate date) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        Optional<Reservation> existingReservation = reservationRepository.findByUserIdAndDateAndIsCancelFalse(userId, date);

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
        Reservation reservation = Reservation.builder()
                .userId(userId)        // 예약한 사용자 ID
                .date(date)           // 예약 날짜 (현재 날짜)
                .isCancel(false)
                .washingRoom(user.getWashingRoom()) // User 테이블에서 조회한 washingRoom 값 설정
                .build();

//        ReservationLog reservationLog = ReservationLog.builder()
//                .userId(userId)
//                .isCancel(false)
//                .date(LocalDateTime.now())
//                .resDate(date)
//                .washingRoom(user.getWashingRoom())
//                .build();

        // 예약을 데이터베이스에 저장
        reservationRepository.save(reservation);
//        reservationLogRepository.save(reservationLog);
    }


//    public void cancelReservation(Long userId, LocalDate date) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
//
//        Optional<Reservation> reservationOpt = reservationRepository.findByUserIdAndDate(userId, date);
//
//        // 예약이 존재하면 isCancel을 true로 변경하고 저장
//        if (reservationOpt.isPresent()) {
//            Reservation reservation = reservationOpt.get();
//            // 새로운 예약 생성
//            ReservationLog reservationLog = ReservationLog.builder()
//                    .userId(userId)
//                    .isCancel(true)
//                    .date(LocalDateTime.now())
//                    .resDate(date)
//                    .washingRoom(user.getWashingRoom())
//                    .build();
//
//            reservationRepository.delete(reservation); // 예약 삭제
//            reservationLogRepository.save(reservationLog);
//
//        } else {
//            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
//        }
//    }

    public void cancelReservation(Long userId, LocalDate date) {
        // userId와 date, isCancel이 false인 예약 조회
        Reservation reservation = reservationRepository.findByUserIdAndDateAndIsCancelFalse(userId, date)
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
            endTime = now.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)).toLocalDate();
        } else {
            // 이번 주 월요일부터 목요일까지 설정
            startTime = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate();
            endTime = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY)).toLocalDate();
        }

        // 예약 기록 조회
        List<Reservation> reservations = reservationRepository.findByWashingRoomAndDateBetween(washingRoom, startTime, endTime);

        // 월요일부터 목요일에 해당하는 날짜만 필터링
        List<LocalDate> allWeekdays = startTime.datesUntil(endTime.plusDays(1))
                .filter(date -> {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    return dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.TUESDAY ||
                            dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.THURSDAY;
                })
                .collect(Collectors.toList());

        // 날짜별 유저 수 매핑
        Map<LocalDate, Long> reservationCountMap = reservations.stream()
                .collect(Collectors.groupingBy(
                        Reservation::getDate,
                        Collectors.counting()
                ));

        // 월요일부터 목요일에 대해서만 userCount를 0으로 설정, 예약이 있으면 해당 유저 수로 덮어쓰기
        List<ReservationSummaryResponse> summaryList = allWeekdays.stream()
                .map(date -> new ReservationSummaryResponse(
                        date,
                        date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
                        reservationCountMap.getOrDefault(date, 0L)))
                .collect(Collectors.toList());

        return summaryList;
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
        List<Reservation> reservations = reservationRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        // 예약 내역이 존재하면 true 반환
        return !reservations.isEmpty();
    }
}