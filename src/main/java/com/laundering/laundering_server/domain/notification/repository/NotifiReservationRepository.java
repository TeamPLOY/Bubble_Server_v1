package com.laundering.laundering_server.domain.notification.repository;


import com.laundering.laundering_server.domain.notification.model.entity.NotifiReservation;
import com.laundering.laundering_server.domain.washingMachine.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotifiReservationRepository extends JpaRepository<NotifiReservation, Long> {

    List<NotifiReservation> findByUserId(Long id);

}
