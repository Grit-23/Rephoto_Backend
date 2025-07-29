package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "클러스터 내 사진 조회 응답 DTO")
public class ClusterResponseDto {

    @Schema(description = "사진 ID", example = "123")
    private Long photoId;

    @Schema(description = "사진 이미지 URL", example = "https://example.com/photo.jpg")
    private String imageUrl;

    @Schema(description = "사진 위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "사진 경도", example = "126.9780")
    private Double longitude;

    @Schema(description = "사진 등록일", example = "2025-07-29T15:30:00")
    private LocalDateTime createdAt;
}
