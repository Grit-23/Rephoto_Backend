package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.dto.DescriptionRequestDto;
import com.rephoto.rephoto_api.dto.DescriptionResponseDto;
import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.security.UserDetailsImpl;
import com.rephoto.rephoto_api.service.DescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/description")
@Tag(name = "설명 API", description = "사진에 상세 설명 등록")
public class DescriptionController {

    private final DescriptionService descriptionService;

    @PostMapping
    @Operation(summary = "사진 설명 생성", description = "이미지를 분석하여 적절한 설명 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 설명 생성 성공",
                    content = @Content(schema = @Schema(implementation = DescriptionResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 접근(다른 회원의 사진에 접근)", content = @Content),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "500", description = "설명 생성에 실패하였습니다.", content = @Content)
    })
    public ResponseEntity<DescriptionResponseDto> generateDescription(
            @RequestBody @Valid DescriptionRequestDto requestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {

        /*
        Long userId = userDetails.getUserId();
        DescriptionResponseDto response = descriptionService.generateDescription(userId, requestDto.getPhotoId());
        return ResponseEntity.ok(response);
         */
        return null;
    }

}
