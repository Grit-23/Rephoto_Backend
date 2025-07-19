package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사용자 위치 기반 사진 조회 응답 DTO")
public class MapPhotoResponseDto {

    @Schema(description = "사용자의 현재 위치 정보")
    private CenterDto center;

    @Schema(description = "조회된 사진 수", example = "5")
    private int photoCount;

    @Schema(description = "조회된 사진 리스트")
    private List<PhotoListDto> photos;

    @Getter
    @AllArgsConstructor
    @Schema(description = "중심 좌표(사용자의 현위치)와 반경 정보")
    public static class CenterDto {

        @Schema(description = "위도", example = "37.5665")
        private double lat;

        @Schema(description = "경도", example = "126.9780")
        private double lng;

        @Schema(description = "반경(고정값)", example = "1000m")
        private int radius;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "사진 항목 정보")
    public static class PhotoListDto {

        @Schema(description = "사진 ID", example = "22")
        private Long photoId;

        @Schema(description = "사진 URL", example = "https://s3.amazonaws.com/bucket/filename.jpg")
        private String imageUrl;

        @Schema(description = "사진 설명", example = "여행 중 찍은 풍경 사진")
        private String description;

        @Schema(description = "사진 위도", example = "37.5651")
        private double latitude;

        @Schema(description = "사진 경도", example = "126.9895")
        private double longitude;

        @Schema(description = "사진 촬영 시각", example = "2025-07-19T15:32:00")
        private LocalDateTime createdAt;
    }
}