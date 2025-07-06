package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String loginId;
    private String name;

    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(user.getUserId(), user.getLoginId(), user.getUsername());
    }
}
