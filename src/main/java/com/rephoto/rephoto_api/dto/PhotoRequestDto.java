package com.rephoto.rephoto_api.dto;

import java.time.LocalDateTime;

import com.rephoto.rephoto_api.domain.Photo;
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
}
