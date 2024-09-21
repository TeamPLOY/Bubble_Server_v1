package com.laundering.laundering_server.domain.washingMachine.repository;


import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationLogRepository extends JpaRepository<ReservationLog, Long> {
    // userId로 예약을 조회하는 메소드
    Optional<ReservationLog> findByUserId(Long userId);

    Optional<ReservationLog> findByUserIdAndDate(Long userId, LocalDateTime date);

    List<ReservationLog> findByWashingRoom(String washingRoom);

    List<ReservationLog> findByWashingRoomAndDateBetween(String washingRoom, LocalDateTime start, LocalDateTime end);

}


