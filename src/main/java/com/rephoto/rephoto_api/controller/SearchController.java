package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.SearchRequestDto;
import com.rephoto.rephoto_api.dto.SearchResponseDto;
import com.rephoto.rephoto_api.repository.SearchRepository;
import com.rephoto.rephoto_api.service.PhotoService;
import com.rephoto.rephoto_api.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "검색어 입력 API", description = "AI 기반 검색어 입력 기록")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    @Operation(summary = "검색어 입력", description = "사용자가 검색어를 입력하면 쿼리 변수에 기록으로 저장.")
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

        SearchResponseDto response = searchService.searchPhotos(request.getQuery(), user.getUserId());
        return ResponseEntity.ok(response);
    }

}
