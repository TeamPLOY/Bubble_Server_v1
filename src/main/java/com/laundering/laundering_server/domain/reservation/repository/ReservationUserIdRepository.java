package com.laundering.laundering_server.domain.reservation.repository;


import com.laundering.laundering_server.common.socialPlatform.SocialPlatformType;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.reservation.model.entity.Reservation;
import com.laundering.laundering_server.domain.reservation.model.entity.ReservationUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReservationUserIdRepository extends JpaRepository<ReservationUserId, Long>, JpaSpecificationExecutor<ReservationUserId>
{
}

