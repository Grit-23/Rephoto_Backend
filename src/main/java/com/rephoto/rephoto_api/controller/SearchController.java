package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.SearchRequestDto;
import com.rephoto.rephoto_api.dto.SearchResponseDto;
import com.rephoto.rephoto_api.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "검색어 입력 API", description = "AI 기반 사진 검색 수행")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    @Operation(summary = "검색어 입력", description = "사용자가 입력한 검색어를 바탕으로 db에서 사진 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색어 입력 성공",
                    content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "검색어 누락 또는 형식 오류", content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT 토큰 오류", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<SearchResponseDto> search(
            @RequestBody SearchRequestDto request,
            @AuthenticationPrincipal User user) {

        String query = request.getQuery();

        SearchResponseDto response;
        if (query.trim().startsWith("#")) {
            // 태그 기반 검색
            response = searchService.searchPhotosByTags(query, user.getUserId());
        } else {
            // 설명 기반 검색
            response = searchService.searchPhotosByQuery(query, user.getUserId());
        }

        return ResponseEntity.ok(response);
    }

}
