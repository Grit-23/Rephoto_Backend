package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Album;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumResponseDto {
    private Long userId;
    private Long tagId;
    private String tagName;

    public static AlbumResponseDto fromEntity(Album album) {
        return AlbumResponseDto.builder()
                .userId(album.getUser().getUserId())
                .tagId(album.getTag().getTagId())
                .tagName(album.getTag().getTagName())
                .build();
    }
}
