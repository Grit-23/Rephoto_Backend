package com.rephoto.rephoto_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class PhotoBatchRequestDto {
    private Long userId;
    private List<PhotoMetadata> photos;

    @Data
    public static class PhotoMetadata {
        private String filename;
        private String createdAt;
        private String hash;  // 파일 해시값
    }
}
