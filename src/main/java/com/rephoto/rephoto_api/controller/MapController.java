package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.MapPhotoResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.exception.ErrorResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos/map")
@Tag(name = "지도 API", description = "사용자 현재 위치 가반으로 1km 내 사진 조회")
@RequiredArgsConstructor
public class MapController {

    private final PhotoService photoService;

    @GetMapping
    @Operation(
            summary = "위치 기반 사진 조회",
            description = "지정한 위도(lat), 경도(lng)를 이용하여 반경 1km 내에 존재하는 사진 조회하여 정보 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MapPhotoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "필수 파라미터 누락 또는 좌표 형식 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "사진 또는 상세 정보 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<MapPhotoResponseDto> getPhotosByLocation(
            @Parameter(description = "중심 위도", example = "37.5665")
            @RequestParam(required = true) Double lat,

            @Parameter(description = "중심 경도", example = "126.9780")
            @RequestParam(required = true) Double lng
    ){
        /*
        if (lat == null || lng == null) {
            throw new CustomException(ErrorCode.MAP_PARAMS_REQUIRED);
        }

        final int radius = 1000; //반경 1km 내 사

        MapPhotoResponseDto response = photoService.getPhotosByLocation(lat, lng, radius);
        return ResponseEntity.ok(response);

         */
        return null;
    }
}
