package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "태그 생성 및 수정 요청 DTO")
public class TagRequestDto {

    @Schema(description = "태그명", example = "여름", required = true)
    private String tagName;
}
