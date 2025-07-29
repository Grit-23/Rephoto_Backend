package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "클러스터 내 사진 조회 요청 DTO")
public class ClusterRequestDto {

    @Schema(description = "클러스터 셀 시작 위도", example = "37.5")
    private double cellLat;

    @Schema(description = "클러스터 셀 시작 경도", example = "127.0")
    private double cellLng;

    @Schema(description = "현재 줌 레벨", example = "13")
    private int zoomLevel;
}
