package com.laundering.laundering_server.domain.member.model.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private String refreshToken;

    private String password; //비밀번호

    private String name; // 이름

    private Integer stuNum; // 학번

    private String email; // 이메일

    private String roomNum; // 기숙사 호실
}
