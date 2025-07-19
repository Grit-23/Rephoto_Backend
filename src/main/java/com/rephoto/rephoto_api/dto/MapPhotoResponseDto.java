package com.rephoto.rephoto_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MapPhotoResponseDto {

    private Center center;
    private int photoCount;
    private List<PhotoItem> photos;

    @Getter
    @AllArgsConstructor
    public static class Center {
        private double lat;
        private double lng;
        private int radius;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PhotoItem {
        private Long photoId;
        private String imageUrl;
        private String description;
        private double latitude;
        private double longitude;
        private LocalDateTime createdAt;
    }
}