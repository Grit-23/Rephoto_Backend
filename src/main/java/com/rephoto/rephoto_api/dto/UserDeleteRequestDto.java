package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 탈퇴 요청 DTO")
public class UserDeleteRequestDto {

    @Schema(description = "비밀번호 입력(본인 확인용으로 비밀번호 재입력)", example = "testpassword00")
    private String password;
}
