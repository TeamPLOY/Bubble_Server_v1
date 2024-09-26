package com.laundering.laundering_server.domain.notification.repository;


import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotifiReservationRepository extends JpaRepository<NotifiReservation, Long> {
    Optional<NotifiReservation> findByUserIdAndMachine(Long userId, String machine);
}
