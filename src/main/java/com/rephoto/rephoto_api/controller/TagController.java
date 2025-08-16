package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.TagRequestDto;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import com.rephoto.rephoto_api.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Tag API", description = "사진 태그 조회/추가/수정/삭제 API")
@SecurityRequirement(name = "bearerAuth")
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "사진에 달린 태그 목록 조회",
            description = "특정 사진의 태그 목록을 조회합니다. 응답에는 photoTagId(매핑ID)가 포함되어, 수정/삭제시 활용할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagResponseDto.class)))),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "인가 실패(권한 없음)"),
                    @ApiResponse(responseCode = "404", description = "사진 없음")
            }
    )
    @GetMapping("/photos/{photoId}/tags")
    public List<TagResponseDto> listTagsOfPhoto(
            @Parameter(description = "사진 ID", example = "1") @PathVariable Long photoId
    ) {
        return tagService.listTagsOfPhoto(photoId);
    }

    @Operation(
            summary = "태그 추가",
            description = "특정 사진에 새로운 태그를 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TagRequestDto.class),
                            examples = @ExampleObject(value = "{\"tagName\":\"여행\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "추가 성공",
                            content = @Content(schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청(태그명 누락 등)"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "인가 실패(권한 없음)"),
                    @ApiResponse(responseCode = "404", description = "사진 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 해당 태그가 연결됨")
            }
    )
    @PostMapping("/photos/{photoId}/tags")
    public TagResponseDto addTag(
            @Parameter(description = "사진 ID", example = "1") @PathVariable Long photoId,
            @RequestBody TagRequestDto dto
    ) {
        return tagService.addTag(photoId, dto.getTagName());
    }

    @Operation(
            summary = "태그 교체(수정)",
            description = "photoTagId(사진-태그 매핑 ID)를 지정하여, 해당 연결을 새 태그명으로 교체합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TagRequestDto.class),
                            examples = @ExampleObject(value = "{\"tagName\":\"가족\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "교체 성공",
                            content = @Content(schema = @Schema(implementation = TagResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청(태그명 누락 등)"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "인가 실패(권한 없음)"),
                    @ApiResponse(responseCode = "404", description = "매핑 또는 사진/태그 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 동일 태그로 연결되어 있음")
            }
    )
    @PutMapping("/photo-tags/{photoTagId}")
    public TagResponseDto replaceTag(
            @Parameter(description = "사진-태그 매핑 ID", example = "10") @PathVariable Long photoTagId,
            @RequestBody TagRequestDto dto
    ) {
        return tagService.replacePhotoTag(photoTagId, dto.getTagName());
    }

    @Operation(
            summary = "태그 삭제(매핑 기준)",
            description = "photoTagId(사진-태그 매핑 ID)를 지정해 해당 연결을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "인가 실패(권한 없음)"),
                    @ApiResponse(responseCode = "404", description = "매핑 없음")
            }
    )
    @DeleteMapping("/photo-tags/{photoTagId}")
    public void deleteTagByMapping(
            @Parameter(description = "사진-태그 매핑 ID", example = "10") @PathVariable Long photoTagId
    ) {
        tagService.deleteTagByMapping(photoTagId);
    }
}