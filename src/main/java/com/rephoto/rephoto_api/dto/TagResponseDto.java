package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Photo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "태그 응답 DTO")
public class TagResponseDto {

    @Schema(description = "태그명", example = "여름")
    private String tagName;

    @Schema(description = "태그가 연결된 사진 정보")
    private Photo photo;
}