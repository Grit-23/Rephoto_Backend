package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.MapPhotoResponseDto;
import com.rephoto.rephoto_api.dto.MapRequestDto;
import com.rephoto.rephoto_api.dto.MapResponseDto;
import com.rephoto.rephoto_api.dto.SearchResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.service.MapService;
import com.rephoto.rephoto_api.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@Tag(name = "지도 API", description = "사진의 위치 기반 클러스터링 수행하여 지도에 표시")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/photos")
    @Operation( summary = "지도 기반 사진 조회",
            description = "화면의 지도 위치, 줌 레벨 기반으로 사진을 클러스터링 하여 표시"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색어 입력 성공",
                    content = @Content(schema = @Schema(implementation = MapResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "파라미터 누락 또는 잘못된 형식", content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT 토큰 오류", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<MapResponseDto>> getClusteredPhotos(
            @ModelAttribute MapRequestDto request,
            @AuthenticationPrincipal User currentUser) {

        List<MapResponseDto> result = mapService.getClusteredPhotos(currentUser.getUserId(), request);
        return ResponseEntity.ok(result);
    }
}