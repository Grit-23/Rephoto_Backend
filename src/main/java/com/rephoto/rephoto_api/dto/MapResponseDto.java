package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "지도에 뜨는 사진 응답 DTO")
public class MapResponseDto {

    @Schema(description = "클러스터 셀 시작 위도", example = "37.5")
    private double cellLat;

    @Schema(description = "클러스터 셀 시작 경도", example = "127.0")
    private double cellLng;

    @Schema(description = "클러스터의 썸네일 이미지 url", example = "https://example.com/test_photo.jpg")
    private String thumbnailUrl;

    @Schema(description = "클러스터에 포함되는 사진 수", example = "40")
    private long photoCount;
}
