package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "S3 파일 업로드 응답 DTO")
public class S3UploadResponseDto {

    @Schema(description = "S3에 업로드된 파일 URL", example = "https://rephoto-s3-bucket.s3.region.amazonaws.com/images/1234.jpg")
    private String url;
}
