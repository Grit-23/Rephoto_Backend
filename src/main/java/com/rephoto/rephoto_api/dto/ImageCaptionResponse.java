package com.rephoto.rephoto_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "AI 설명, 태그, 임베딩 생성 응답 DTO")
public class ImageCaptionResponse {

    private String explanation;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("explanation_embedding")
    private List<Double> explanation_embedding;

    @JsonProperty("private_info")
    private boolean private_info;
}
