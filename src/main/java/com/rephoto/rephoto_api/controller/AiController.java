package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.CaptionResponse;
import com.rephoto.rephoto_api.service.AiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI 호출 API", description = "AI 연동하여 이미지 설명/태그/임베딩 생성")
public class AiController {

    private final AiService aiService;

    @PostMapping("/caption")
    public ResponseEntity<CaptionResponse> generateCaption(
            @RequestParam("file")MultipartFile file
            ){
        CaptionResponse response = aiService.generateCaption(file);
        return ResponseEntity.ok(response);
    }
}
