package com.rephoto.rephoto_api.dto;

import java.time.LocalDateTime;

import com.rephoto.rephoto_api.domain.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class PhotoResponseDto {
    private Long photoId;
    private String imageUrl;
    private boolean isPrivate;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private String fileName;

    public static PhotoResponseDto fromEntity(Photo photo) {
        return PhotoResponseDto.builder()
                .photoId(photo.getPhotoId())
                .imageUrl(photo.getImageUrl())
                .isPrivate(photo.isPrivate())
                .build();
    }
}
