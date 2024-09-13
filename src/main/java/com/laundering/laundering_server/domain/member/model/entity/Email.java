package com.laundering.laundering_server.domain.member.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private String email;

    private Integer code;

    private LocalDate date;

}
