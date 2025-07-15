package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카카오 로그인 응답 DTO")
public class KakaoLoginResponseDto {

    @Schema(description = "카카오 로그인 후 발급되는 JWT 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}

