package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지도에 뜨는 사진 요청 DTO")
public class MapRequestDto {

    @Schema(description = "사진 조회하는 유저의 아이디", example = "2")
    private Long userId;

    @Schema(description = "현재 화면의 위도 최솟값", example = "37.5665")
    private Double minLat;

    @Schema(description = "현재 화면의 위도 최댓값", example = "39.5665")
    private Double maxLat;

    @Schema(description = "현재 화면의 경도 최솟값", example = "126.9780")
    private Double minLng;

    @Schema(description = "현재 화면의 경도 최댓값", example = "127.9780")
    private Double maxLng;

    @Schema(description = "지도 확대 비율", example = "7")
    private Integer zoomLevel;
}
