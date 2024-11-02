package com.ploy.bubble_server_v1.domain.reservation.repository;


import com.ploy.bubble_server_v1.domain.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserIdAndDate(Long userId, LocalDate date);
    List<Reservation> findByWashingRoomAndDateBetween(String washingRoom, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Reservation> findByUserId(Long userId);
    Optional<Reservation> findByUserIdAndDateAndIsCancelFalse(Long userId, LocalDate date);

}