package com.laundering.laundering_server.domain.reservation.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

// 복합 키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserId implements Serializable {
    private Long id;
    private Long resId;
    private Long userId;
}