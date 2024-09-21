package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import com.laundering.laundering_server.domain.washingMachine.model.dto.response.ReservationSummaryResponse;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationLogRepository;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UserRepository userRepository;

    public void reservation(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // userId와 오늘 날짜로 예약 조회
        Optional<Reservation> existingReservation = reservationRepository.findByUserIdAndDate(userId, date);

        // 예약이 이미 존재하면 처리
        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();

            // 만약 기존 예약의 isCancel이 true이면 false로 바꾸고 저장
            if (reservation.isCancel()) {
                reservation.setCancel(false);  // 예약 복구
                reservationRepository.save(reservation);
            } else {
                // 이미 예약이 존재하고 취소되지 않았으므로 예외 처리
                throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS);
            }
        } else {
            // 새로운 예약 생성
            Reservation reservation = Reservation.builder()
                    .userId(userId)        // 예약한 사용자 ID
                    .isCancel(false)       // 예약 취소 상태 (초기값 false)
                    .date(date)           // 예약 날짜 (현재 날짜)
                    .washingRoom(user.getWashingRoom()) // User 테이블에서 조회한 washingRoom 값 설정
                    .build();

            ReservationLog reservationLog = ReservationLog.builder()
                    .userId(userId)
                    .isCancel(false)
                    .date(LocalDateTime.now())
                    .resDate(date)
                    .washingRoom(user.getWashingRoom())
                    .build();


            // 예약을 데이터베이스에 저장
            reservationRepository.save(reservation);
            reservationLogRepository.save(reservationLog);
        }
    }

    public void cancelReservation(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        Optional<Reservation> reservationOpt = reservationRepository.findByUserIdAndDate(userId, date);

        // 예약이 존재하면 isCancel을 true로 변경하고 저장
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setCancel(true);

            // 새로운 예약 생성
            ReservationLog reservationLog = ReservationLog.builder()
                    .userId(userId)
                    .isCancel(true)
                    .date(LocalDateTime.now())
                    .resDate(date)
                    .washingRoom(user.getWashingRoom())
                    .build();

            reservationRepository.save(reservation); // 변경 사항 저장
            reservationLogRepository.save(reservationLog);

        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }

    public List<ReservationSummaryResponse> getReservation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String washingRoom = user.getWashingRoom();

        LocalDateTime now = LocalDateTime.now();
        LocalDate startOfCurrentSunday22 = now
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
                .withHour(22)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toLocalDate();  // LocalDate로 변환

        LocalDate endOfCurrentSunday = now
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999)
                .toLocalDate();  // LocalDate로 변환

        LocalDate startTime;
        LocalDate endTime;

        // 현재 시간이 일요일 22:00~23:59 사이인지 확인
        if (now.isAfter(startOfCurrentSunday22.atTime(22, 0)) && now.isBefore(endOfCurrentSunday.atTime(23, 59))) {
            // 다음 주 일요일 22:00 ~ 그다음 주 일요일 21:59 설정
            startTime = now.with(TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY))
                    .withHour(22)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toLocalDate();  // LocalDate로 변환

            endTime = now.with(TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY))
                    .with(TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY))
                    .withHour(21)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(999999999)
                    .toLocalDate();  // LocalDate로 변환
        } else {
            // 전 주 일요일 22:00 ~ 이번 주 일요일 21:59 설정
            startTime = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
                    .withHour(22)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toLocalDate();  // LocalDate로 변환

            endTime = now.with(TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY))
                    .withHour(21)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(999999999)
                    .toLocalDate();  // LocalDate로 변환
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
                        Reservation::getDate,  // getDate()를 그대로 사용
                        Collectors.counting()  // 유저 수 카운팅
                ));

        // 월요일부터 목요일에 대해서만 userCount를 0으로 설정, 예약이 있으면 해당 유저 수로 덮어쓰기
        List<ReservationSummaryResponse> summaryList = allWeekdays.stream()
                .map(date -> new ReservationSummaryResponse(
                        date,
                        date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),  // 요일 추가
                        reservationCountMap.getOrDefault(date, 0L)))  // 예약이 없으면 0, 있으면 실제 유저 수
                .collect(Collectors.toList());

        return summaryList;
    }


}