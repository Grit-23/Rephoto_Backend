package com.rephoto.rephoto_api.dto;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "태그 응답 DTO")
public class TagResponseDto {

    @Schema(description = "사진-태그 매핑 ID", example = "10")
    private Long photoTagId;

    @Schema(description = "태그 ID", example = "3")
    private Long tagId;

    @Schema(description = "태그명", example = "여름")
    private String tagName;

    @Schema(description = "사진 ID", example = "1")
    private Long photoId;

    public static TagResponseDto of(PhotoTag pt) {
        return TagResponseDto.builder()
                .photoTagId(pt.getPhotoTagId())
                .tagId(pt.getTag().getTagId())
                .photoId(pt.getPhoto().getPhotoId())
                .tagName(pt.getTag().getTagName())
                .build();
    }

    public static TagResponseDto fromEntity(Tag tag, Photo photo) {
        return TagResponseDto.builder()
                .photoTagId(null)
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .photoId(photo != null ? photo.getPhotoId() : null)
                .build();
    }
}