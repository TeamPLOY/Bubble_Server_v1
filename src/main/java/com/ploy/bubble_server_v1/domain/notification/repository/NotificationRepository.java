package com.ploy.bubble_server_v1.domain.notification.repository;

import com.ploy.bubble_server_v1.domain.notification.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>
{

}


