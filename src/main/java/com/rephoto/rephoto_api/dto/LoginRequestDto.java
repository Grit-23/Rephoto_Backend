package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {

    @Schema(description = "로그인 아이디", example = "chwwwon")
    private String loginId;

    @Schema(description = "로그인 비밀번호", example = "password1234!")
    private String password;
}
