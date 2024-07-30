package com.laundering.laundering_server.domain.reservation.model.entity;

import com.laundering.laundering_server.domain.member.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

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