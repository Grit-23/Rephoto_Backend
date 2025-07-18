package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Photo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class PhotoListDto {
    private Long photoId;
    private String imageUrl;
    private boolean isPrivate;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
