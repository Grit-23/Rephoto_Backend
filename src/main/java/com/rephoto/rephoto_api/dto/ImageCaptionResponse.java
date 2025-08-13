package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "AI 설명, 태그, 임베딩 생성 응답 DTO")
public class ImageCaptionResponse {

    private String explanation;
    private List<String> tags;
    private List<Double> explanation_embedding;
    private boolean private_info;
}
