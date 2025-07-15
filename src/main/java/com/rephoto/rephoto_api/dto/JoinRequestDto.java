package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class JoinRequestDto {

    @Schema(description = "로그인 아이디", example = "chwwwon")
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Schema(description = "로그인 비밀번호", example = "password1234!")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(description = "회원 이름", example = "최형원")
    @NotBlank(message = "이름은 필수입니다.")
    private String username;
}
