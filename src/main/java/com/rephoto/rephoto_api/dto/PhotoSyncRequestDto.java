package com.rephoto.rephoto_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class PhotoSyncRequestDto {
    private Long userId;
    private List<PhotoMetadata> newPhotos;

    @Data
    public static class PhotoMetadata {
        private String filename;
        private String createdAt;
        private String hash;
    }
}
