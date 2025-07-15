package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginResponseDto {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}
