package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사진 설명 생성 응답 DTO")
public class AiDescriptionResponseDto {

    @Schema(description = "등록된 사진설명의 ID", example = "5")
    private Long descriptionId;

    @Schema(description = "등록된 사진설명 내용", example = "비오는 여름날의 풍경")
    private String description;

    @Schema(description = "설명이 등록된 사진 Id", example = "5")
    private Long photoId;

    @Schema(description = "설명이 등록된 태그명(앨범명)", example = "바다")
    private String tagName;
}
