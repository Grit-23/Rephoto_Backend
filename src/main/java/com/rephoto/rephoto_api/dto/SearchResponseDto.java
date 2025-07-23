package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사진 검색 응답 DTO")
public class SearchResponseDto {

    @Schema(description = "검색어 ID", example = "12")
    private Long queryId;

    @Schema(description = "사용자가 입력한 자연어 검색어", example = "벚꽃길 산책")
    private String query;

    @Schema(description = "검색한 시각", example = "2025-07-20T15:00:00")
    private LocalDateTime createdAt;
}
