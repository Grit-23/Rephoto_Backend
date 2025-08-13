package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사진 검색 응답 DTO")
public class SearchResponseDto {

    @Schema(description = "사용자가 입력한 자연어 검색어", example = "벚꽃길 산책")
    private String query;

    @Schema(description = "사진 검색 결과 목록")
    private List<SearchResults> searchResults;

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResults {

        @Schema(description = "검색된 결과 사진의 url", example = "https://example.com/test_photo.jpg")
        private String imageUrl;

        @Schema(description = "검색된 결과 사진 아이디", example = "1")
        private Long photoId;
    }
}
