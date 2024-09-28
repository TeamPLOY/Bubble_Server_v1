package com.laundering.laundering_server.domain.washingMachine.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private boolean isCancel;

    private LocalDate date;

    private String washingRoom;
}