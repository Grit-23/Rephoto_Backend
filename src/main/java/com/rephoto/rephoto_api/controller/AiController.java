package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.ImageCaptionRequestDto;
import com.rephoto.rephoto_api.dto.ImageCaptionResponse;
import com.rephoto.rephoto_api.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI 호출 API", description = "AI 연동하여 이미지 설명/태그/임베딩 생성")
public class AiController {

    private final AiService aiService;

    @PostMapping("/caption/generate")
    @Operation(summary = "URL로 설명 생성 요청", description = "S3 url 입력하여 요청 전달 (기본)")
    public ResponseEntity<ImageCaptionResponse> generateCaptionByUrl(
            @RequestBody ImageCaptionRequestDto imageCaptionRequestDto) {

        ImageCaptionResponse response = aiService.generateCaptionFromUrl(imageCaptionRequestDto.getImageUrl());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/caption/generate-by-file")
    @Operation(summary = "FILE로 설명 생성 요청", description = "파일 직접 업로드하여 요청 전달 (임시)")
    public ResponseEntity<ImageCaptionResponse> generateCaptionByFile(
            @RequestParam("file")MultipartFile file
            ){
        ImageCaptionResponse response = aiService.generateCaption(file);
        return ResponseEntity.ok(response);
    }

}
