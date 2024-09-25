package com.laundering.laundering_server.domain.notification.repository;

import com.laundering.laundering_server.domain.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>
{

}


