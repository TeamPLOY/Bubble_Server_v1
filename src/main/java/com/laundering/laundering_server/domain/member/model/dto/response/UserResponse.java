package com.laundering.laundering_server.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @Schema(description = "이름")
    private String name;

    @Schema(description = "학번")
    private int studentNum;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "기숙사 호실")
    private String roomNum;
}
