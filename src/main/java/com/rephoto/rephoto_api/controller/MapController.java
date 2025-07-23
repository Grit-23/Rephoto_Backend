package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.MapPhotoResponseDto;
import com.rephoto.rephoto_api.dto.MapRequestDto;
import com.rephoto.rephoto_api.dto.MapResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photos/map")
@Tag(name = "지도 API", description = "사용자 현재 위치 가반으로 1km 내 사진 조회")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    @Operation(
            summary = "지도 기반 사진 조회",
            description = ""
    )
    public ResponseEntity<List<MapResponseDto>> getMapClusters(
            @RequestParam Long userId,
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng,
            @RequestParam int zoomLevel
    ) {
        /*
        MapRequestDto requestDto = new MapRequestDto(userId, minLat, maxLat, minLng, maxLng, zoomLevel);
        List<MapResponseDto> clusters = mapService.getPhotoClusters(requestDto);
        return ResponseEntity.ok(clusters);

         */
        return null;
    }
}
