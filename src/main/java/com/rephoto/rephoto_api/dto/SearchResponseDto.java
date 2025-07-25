package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사진 검색 응답 DTO")
public class SearchResponseDto {

    @Schema(description = "사용자가 입력한 자연어 검색어", example = "벚꽃길 산책")
    private String query;

    @Schema(description = "검색된 결과 사진의 리스트", example = "")
    private List<Long> photoIds;
}
