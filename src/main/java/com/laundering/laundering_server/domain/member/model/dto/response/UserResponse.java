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
    private Long studentNum;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "호실")
    private Long roomNum;
}
