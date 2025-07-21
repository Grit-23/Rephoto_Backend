package com.rephoto.rephoto_api.dto;

import java.time.LocalDateTime;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhotoRequestDto {
    private String imageUrl;
    private boolean isPrivate;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private String hash;

    public static Photo toEntity(PhotoRequestDto dto, User user) {
        return Photo.builder()
                .user(user)
                .imageUrl(dto.getImageUrl())
                .isPrivate(dto.isPrivate())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .createdAt(dto.getCreatedAt())
                .hash(dto.getHash())
                .build();
    }

}
