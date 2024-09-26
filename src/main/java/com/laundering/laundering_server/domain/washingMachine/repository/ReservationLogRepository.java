package com.laundering.laundering_server.domain.washingMachine.repository;


import com.laundering.laundering_server.domain.washingMachine.model.entity.ReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationLogRepository extends JpaRepository<ReservationLog, Long> {
    List<ReservationLog> findByUserId(Long userId);

}


