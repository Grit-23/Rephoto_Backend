package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Url 기반 AI 설명, 태그, 임베딩 생성 응답 DTO")
public class ImageCaptionRequestDto {


    private String imageUrl;
}
