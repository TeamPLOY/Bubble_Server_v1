package com.laundering.laundering_server.domain.washingMachine.repository;


import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserIdAndDate(Long userId, LocalDate date);
    List<Reservation> findByWashingRoomAndDateBetween(String washingRoom, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByUserId(Long userId);
    Optional<Reservation> findByUserIdAndDateAndIsCancelFalse(Long userId, LocalDate date);

    List<Reservation> findByUserIdAndDateBetweenAndIsCancelFalse(Long userId, LocalDate startDate, LocalDate endDate);

    List<Reservation> findByWashingRoomAndDateBetweenAndIsCancelFalse(String washingRoom, LocalDate startDate, LocalDate endDate);

}