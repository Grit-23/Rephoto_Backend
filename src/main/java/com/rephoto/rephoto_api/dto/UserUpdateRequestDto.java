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
@Schema(description = "회원 정보 수정 요청 DTO")
public class UserUpdateRequestDto {

    @Schema(description = "변경할 사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "변경할 비밀번호", example = "newPassword123!")
    private String password;
}
