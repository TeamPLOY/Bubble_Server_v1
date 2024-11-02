package com.ploy.bubble_server_v1.domain.notification.repository;


import com.ploy.bubble_server_v1.domain.notification.model.entity.NotifiReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotifiReservationRepository extends JpaRepository<NotifiReservation, Long> {
    Optional<NotifiReservation> findByUserIdAndMachine(Long userId, String machine);
}
