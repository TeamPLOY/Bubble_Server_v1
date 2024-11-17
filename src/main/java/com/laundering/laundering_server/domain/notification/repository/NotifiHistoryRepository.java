package com.laundering.laundering_server.domain.notification.repository;

import com.laundering.laundering_server.domain.notification.model.entity.NotifiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifiHistoryRepository extends JpaRepository<NotifiHistory, Long> {
    List<NotifiHistory> findByUserId(Long userId);
}

