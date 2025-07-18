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
@Schema(description = "카카오 로그인 요청 DTO")
public class KakaoLoginRequestDto {

    @Schema(description = "카카오 로그인 용 accessToken", example = "VuSRToGHiRY9-C7mvf7pC2oI5Sr7dmKMAAAAAQoXEpYAAAGYDRk-sKew61y3DOUZ")
    private String accessToken;
}
