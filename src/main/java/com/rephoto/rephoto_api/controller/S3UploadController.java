package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@Tag(name = "S3 파일 업로드 API", description = "S3에 파일 업로드 기능 수행")
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @PostMapping("/upload")
    @Operation(
            summary = "S3 파일 업로드",
            description = "파일을 업로드하고 S3 URL을 반환. 'type': 'images'로 이미지 업로드."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공 - S3 URL 반환"),
            @ApiResponse(responseCode = "400", description = "파일이 비어있거나 크기 초과 또는 지원하지 않는 파일 형식",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        String url = s3UploadService.upload(file, type);
        return ResponseEntity.ok(url);
    }
}