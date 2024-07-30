package com.laundering.launering_server.domain.member.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reservation_User")
public class ReservationUser {

    @EmbeddedId
    private ReservationUserId id;

    @ManyToOne
    @MapsId("resId")
    @JoinColumn(name = "res_id")
    private Reservation reservation;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    // 필요한 경우 다른 필드 및 메서드 추가
}
