package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "회원 정보 응답 DTO")
public class UserInfoResponseDto {

    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @Schema(description = "로그인 ID", example = "user123")
    private String loginId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;


    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(user.getUserId(), user.getLoginId(), user.getUsername());
    }
}
