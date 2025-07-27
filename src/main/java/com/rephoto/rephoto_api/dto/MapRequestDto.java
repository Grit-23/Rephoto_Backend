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

    @Schema(description = "현재 화면의 위도 최솟값", example = "37.1")
    private double minLat;

    @Schema(description = "현재 화면의 위도 최댓값", example = "37.9")
    private double maxLat;

    @Schema(description = "현재 화면의 경도 최솟값", example = "126.7")
    private double minLng;

    @Schema(description = "현재 화면의 경도 최댓값", example = "127.2")
    private double maxLng;

    @Schema(description = "지도 확대 비율", example = "11")
    private int zoomLevel;


}
