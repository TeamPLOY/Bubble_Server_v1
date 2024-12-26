package com.laundering.laundering_server.domain.washingMachine.repository;


import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByUserIdAndDateAndCancelFalse(Long userId, LocalDate date);

    List<Reservation> findByUserIdAndDateBetweenAndCancelFalse(Long userId, LocalDate startDate, LocalDate endDate);

    List<Reservation> findByWashingRoomAndDateBetweenAndCancelFalse(String washingRoom, LocalDate startDate, LocalDate endDate);

    List<Reservation> findByDateAndMachineAndCancelFalse(LocalDate date, String machine);

    List<Reservation> findByDateAndCancelFalse(LocalDate date);
}