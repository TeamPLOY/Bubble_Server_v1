package com.laundering.laundering_server.domain.washingMachine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class ReservationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private boolean isCancel;

    private LocalDateTime date; // 예약한 날짜

    private LocalDate resDate; // 예약할 날짜

    private String washingRoom; // 세탁실 위치
}
