package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.TagRequestDto;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.repository.TagRepository;
import com.rephoto.rephoto_api.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags/{photoId}")
@RequiredArgsConstructor
@Tag(name = "Tag API", description = "사진 태그 생성, 수정, 삭제 API")
public class TagController {

    private final TagService tagService;
    private final TagRepository tagRepository;

    @Operation(
            summary = "태그 삭제",
            description = "특정 사진(photoId)에서 지정한 태그(tagId)를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "태그 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 태그 또는 사진을 찾을 수 없음")
            }
    )
    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(
            @Parameter(description = "사진 ID", example = "1") @PathVariable Long photoId,
            @Parameter(description = "삭제할 태그 ID", example = "5") @PathVariable Long tagId) {
        tagService.deleteTag(photoId, tagId);
        return ResponseEntity.ok("삭제 완료");
    }

    @Operation(
            summary = "태그 수정",
            description = "특정 사진(photoId)의 특정 태그(tagId)를 새 태그명으로 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "태그 수정 성공",
                            content = @Content(schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "해당 태그 또는 사진을 찾을 수 없음")
            }
    )
    @PutMapping("/{tagId}/")
    public TagResponseDto updateTag(
            @Parameter(description = "사진 ID", example = "1") @PathVariable Long photoId,
            @Parameter(description = "수정할 태그 ID", example = "5") @PathVariable Long tagId,
            @RequestBody TagRequestDto requestDto) {
        return tagService.replaceTag(photoId, tagId, requestDto.getTagName());
    }

    @Operation(
            summary = "태그 생성",
            description = "특정 사진(photoId)에 새로운 태그를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "태그 생성 성공",
                            content = @Content(schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "해당 사진을 찾을 수 없음")
            }
    )
    @PostMapping("")
    public TagResponseDto saveTag(
            @Parameter(description = "사진 ID", example = "1") @PathVariable Long photoId,
            @RequestBody TagRequestDto requestDto) {
        return tagService.addTag(photoId, requestDto.getTagName());
    }
}
