package com.laundering.laundering_server.domain.reservation.repository;


import com.laundering.laundering_server.domain.reservation.model.entity.ReservationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ReservationUserRepository extends JpaRepository<ReservationUser, Long>, JpaSpecificationExecutor<ReservationUser>
{
}

