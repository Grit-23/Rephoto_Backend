package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
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