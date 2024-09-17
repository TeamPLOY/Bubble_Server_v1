package com.laundering.laundering_server.domain.washingMachine.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    public void reservation(Long userId) {
        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // userId와 오늘 날짜로 예약 조회
        Optional<Reservation> existingReservation = reservationRepository.findByUserIdAndDate(userId, today);

        // 예약이 이미 존재하면 메시지 반환
        if (existingReservation.isPresent()) {
            throw new BusinessException(ErrorCode.RESERVATION_ALREADY_EXISTS);
        }

        // 새로운 예약 생성
        Reservation reservation = Reservation.builder()
                .userId(userId)        // 예약한 사용자 ID
                .isCancel(false)       // 예약 취소 상태 (초기값 false)
                .date(today)           // 예약 날짜 (현재 날짜)
                .build();

        // 예약을 데이터베이스에 저장
        reservationRepository.save(reservation);
    }

    public void cancelReservation(Long userId) {
        // userId로 예약 조회
        Optional<Reservation> reservationOpt = reservationRepository.findByUserId(userId);

        // 예약이 존재하면 isCancel을 true로 변경하고 저장
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setCancel(true); // isCancel 값을 true로 설정
            reservationRepository.save(reservation); // 변경 사항 저장
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }
}
