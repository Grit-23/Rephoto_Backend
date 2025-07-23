package com.rephoto.rephoto_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhotoBatchRequestDto {
    private Long userId;
    private List<PhotoMetadata> photos;

    @Data
    public static class PhotoMetadata {
        private Double latitude;
        private Double longitude;
        private String imageUrl;
        private String createdAt;
        private String hash;
    }

    public List<PhotoRequestDto> toPhotoRequestDtoList() {
        return photos.stream()
                .map(meta -> PhotoRequestDto.builder()
                        .imageUrl(meta.getImageUrl())
                        .isPrivate(false) // 초기엔 false 또는 정책에 따라
                        .latitude(meta.getLatitude())
                        .longitude(meta.getLongitude())
                        .createdAt(LocalDateTime.parse(meta.getCreatedAt()))
                        .hash(meta.getHash())
                        .build())
                .toList();
    }


}
