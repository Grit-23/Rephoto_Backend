package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "초기 사진 배치 업로드 요청 DTO")
public class PhotoBatchRequestDto {

    @Schema(description = "사진 메타데이터 목록")
    private List<PhotoMetadata> photos;

    @Data
    @Schema(description = "사진 메타데이터")
    public static class PhotoMetadata {

        @Schema(description = "위도", example = "37.5665")
        private Double latitude;

        @Schema(description = "경도", example = "126.9780")
        private Double longitude;

        @Schema(description = "이미지 URL", example = "https://rephoto.s3.ap-northeast-2.amazonaws.com/user1/photo1.jpg")
        private String imageUrl;

        @Schema(description = "사진 생성 시각", example = "2025-07-20T15:00:00")
        private String createdAt;

        @Schema(description = "파일 이름", example = "IMG_20250720_150000.jpg")
        private String fileName;
    }

    public List<PhotoRequestDto> toPhotoRequestDtoList() {
        return photos.stream()
                .map(meta -> PhotoRequestDto.builder()
                        .imageUrl(meta.getImageUrl())
                        .isPrivate(false)
                        .latitude(meta.getLatitude())
                        .longitude(meta.getLongitude())
                        .createdAt(LocalDateTime.parse(meta.getCreatedAt()))
                        .fileName(meta.getFileName())
                        .build())
                .toList();
    }
}
