package com.ploy.bubble_server_v1.domain.user.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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

    @Schema(description = "세탁실 위치")
    private String washingRoom;
}
