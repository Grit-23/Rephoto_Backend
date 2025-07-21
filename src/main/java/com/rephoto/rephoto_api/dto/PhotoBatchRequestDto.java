package com.rephoto.rephoto_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class PhotoBatchRequestDto {
    private Long userId;
    private List<PhotoMetadata> photos;

    @Data
    public static class PhotoMetadata {
        private Double latitude;
        private Double longitude;
        private String imageUrl;
        private String createdAt;
        private String hash;
    }
}
