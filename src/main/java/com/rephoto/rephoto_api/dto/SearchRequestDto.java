package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사진 검색 요청 DTO")
public class SearchRequestDto {

    @NotBlank(message = "검색어를 입력하세요.")
    @Schema(description = "사용자가 입력한 자연어 검색어", example = "올해 5월 초 바다 사진")
    private String query;
}
