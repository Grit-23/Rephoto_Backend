package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.service.DescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/descriptions")
@Tag(name = "설명 API", description = "사진의 설명 조회")
public class DescriptionController {

    private final DescriptionService descriptionService;

    @GetMapping("/{photoId}")
    @Operation(summary = "사진 설명 조회", description = "photoId로 해당 사진의 설명을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "설명 조회 성공",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "ㄱㄱ에서 ㄴㄴ과 함께 ㄷㄷ날짜에 ㄹㄹ을 하며 찍은 사진")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "해당 사진이나 설명이 존재하지 않음"),
            @ApiResponse(responseCode = "403", description = "설명 접근 권한 없음")
    })
    public ResponseEntity<String> getDescription(@PathVariable Long photoId) {
        String description = descriptionService.getDescription(photoId);
        return ResponseEntity.ok(description);
    }

}
