package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Album;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "앨범 검색 응답 DTO")
public class AlbumResponseDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "태그 ID", example = "10")
    private Long tagId;

    @Schema(description = "태그 이름", example = "벚꽃")
    private String tagName;

    public static AlbumResponseDto fromEntity(Album album) {
        return AlbumResponseDto.builder()
                .userId(album.getUser().getUserId())
                .tagId(album.getTag().getTagId())
                .tagName(album.getTag().getTagName())
                .build();
    }
}
