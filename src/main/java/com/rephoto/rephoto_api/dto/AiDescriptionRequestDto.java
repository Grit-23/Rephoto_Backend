package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI에 사진 설명 생성 요청 DTO")
public class AiDescriptionRequestDto {

    @NotNull(message = "사진 ID는 필수입니다.")
    @Schema(description = "설명을 등록할 사진", example = "3", required = true )
    private Long photoId;

    @NotNull(message = "위도 정보는 필수입니다.")
    @Schema(description = "설명을 요청하는 사진의 위도", example = "37.5665", required = true )
    private Double latitude;

    @NotNull(message = "경도 정보는 필수입니다.")
    @Schema(description = "설명을 요청하는 사진의 경도", example = "126.9780", required = true )
    private Double longitude;

    @NotNull(message = "사진의 날짜/시간 정보는 필수입니다.")
    @Schema(description = "설명을 요청하는 사진의 날짜/시간 정보", example = "2025-07-19T15:32:00", required = true )
    private LocalDateTime createdAt;

    @NotNull(message = "개인정보 포함 여부는 필수입니다.")
    @Schema(description = "설명을 요청하는 사진에 개인정보 포함 여부", example = "true", required = true )
    private boolean isPrivate;
}
