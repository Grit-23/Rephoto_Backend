package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Photo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "태그 응답용 DTO")
public class TagResponseDto {
    private String tagName;//태그명

    private Photo photo;//어떤 사진과 연결되어 있는지
}
