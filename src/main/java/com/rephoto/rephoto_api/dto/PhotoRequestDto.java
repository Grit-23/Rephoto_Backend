package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "사진 업로드 요청 DTO")
public class PhotoRequestDto {

    @Schema(description = "사진 이미지 URL", example = "https://rephoto.s3.ap-northeast-2.amazonaws.com/user1/photo1.jpg")
    private String imageUrl;

    @Schema(description = "개인정보 포함 여부 (true 시 민감 정보)", example = "false")
    private boolean isPrivate;

    @Schema(description = "사진의 위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "사진의 경도", example = "126.9780")
    private Double longitude;

    @Schema(description = "사진 생성 시각", example = "2025-07-20T15:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "사진 파일 이름", example = "IMG_20250720_150000.jpg")
    private String fileName;

    public static Photo toEntity(PhotoRequestDto dto, User user) {
        return Photo.builder()
                .user(user)
                .imageUrl(dto.getImageUrl())
                .isPrivate(dto.isPrivate())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .createdAt(dto.getCreatedAt())
                .fileName(dto.getFileName())
                .build();
    }
}
