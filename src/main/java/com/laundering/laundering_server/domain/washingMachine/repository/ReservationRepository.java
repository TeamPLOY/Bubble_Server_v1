package com.laundering.laundering_server.domain.washingMachine.repository;


import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // userId로 예약을 조회하는 메소드
    Optional<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByUserIdAndDate(Long userId, LocalDate date);

    List<Reservation> findByWashingRoom(String washingRoom);
}


