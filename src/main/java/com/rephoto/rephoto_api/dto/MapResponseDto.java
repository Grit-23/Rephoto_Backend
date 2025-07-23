package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "지도에 뜨는 사진 응답 DTO")
public class MapResponseDto {

    @Schema(description = "사진이 표시되는 위도값", example = "37.5665")
    private double latitude;

    @Schema(description = "사진이 표시되는 경도값", example = "126.9780")
    private double longitude;

    @Schema(description = "화면에 표시되는 대표 이미지의 url", example = "https://example.com/test_photo.jpg")
    private String thumbnailUrl;

    @Schema(description = "해당 위치에서 찍은 사진에 포함되는 사진 수", example = "40")
    private int photoCount;
}
