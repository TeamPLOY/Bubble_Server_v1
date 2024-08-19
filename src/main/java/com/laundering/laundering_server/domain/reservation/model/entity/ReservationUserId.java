package com.laundering.laundering_server.domain.reservation.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

// 복합 키 클래스
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class ReservationUserId implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resId;

    private Long userId;
}