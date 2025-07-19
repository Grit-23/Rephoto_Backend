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
public class FileUploadController {

    private final S3UploadService s3UploadService;

    private static final long MAX_FILE_SIZE = 500 * 1024 * 1024; // 500MB

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/heic"
    );

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic"
    );


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
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String extension = (fileName != null && fileName.contains("."))
                ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase()
                : "";

        if ("image".equalsIgnoreCase(type)) {
            boolean validType = contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType);
            boolean validExt = ALLOWED_IMAGE_EXTENSIONS.contains(extension);
            if (!validType && !validExt) {
                throw new CustomException(ErrorCode.UNSUPPORTED_IMAGE_TYPE);
            }
        }

        String url = s3UploadService.upload(file, type);
        return ResponseEntity.ok().body(url);
    }
}