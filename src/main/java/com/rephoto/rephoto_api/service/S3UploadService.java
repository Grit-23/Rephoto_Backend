package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final long MAX_FILE_SIZE = 500 * 1024 * 1024; // 500MB

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/heic"
    );

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic"
    );

    public String upload(MultipartFile file, String folder) {
        try {
            // 1. 파일 비어 있는지 검사
            if (file == null || file.isEmpty()) {
                throw new CustomException(ErrorCode.FILE_EMPTY);
            }

            // 2. 크기 제한 검사
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
            }

            // 3. 이미지 타입 검사 (type이 "image"일 경우)
            if ("image".equalsIgnoreCase(folder)) {
                validateImage(file);
            }

            // 4. 파일 업로드
            String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return getFileUrl(fileName);

        } catch (CustomException e) {
            throw e;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String extension = (fileName != null && fileName.contains("."))
                ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase()
                : "";

        boolean validType = contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType);
        boolean validExt = ALLOWED_IMAGE_EXTENSIONS.contains(extension);

        if (!validType && !validExt) {
            throw new CustomException(ErrorCode.UNSUPPORTED_IMAGE_TYPE);
        }
    }

    private String getFileUrl(String fileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
    }
}