package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Album;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumDto {
    private Long userId;
    private Long tagId;
    private String tagName;

    public static AlbumDto fromEntity(Album album) {
        return AlbumDto.builder()
                .userId(album.getUser().getUserId())
                .tagId(album.getTag().getTagId())
                .tagName(album.getTag().getTagName())
                .build();
    }
}
