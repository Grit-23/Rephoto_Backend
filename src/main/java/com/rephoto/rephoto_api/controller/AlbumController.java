package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.dto.AlbumResponseDto;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import com.rephoto.rephoto_api.service.AlbumService;
import com.rephoto.rephoto_api.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "앨범 API", description = "앨범 리스트, 검색, 상세 사진 조회 API")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    @GetMapping("/{userId}")
    @Operation(summary = "사용자의 앨범 리스트 조회", description = "userId에 해당하는 사용자의 앨범 전체를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = Album.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public List<Album> AlbumList(@PathVariable Long userId) {
        return albumService.getAllAlbums(userId);
    }

    @GetMapping("")
    @Operation(summary = "태그 기반 앨범 검색", description = "userId와 tag 키워드로 앨범을 검색하여 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 검색 성공",
                    content = @Content(schema = @Schema(implementation = AlbumResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 조건의 앨범이 없음")
    })
    public ResponseEntity<AlbumResponseDto> searchAlbum(
            @RequestParam String tag,
            @RequestParam Long userId) {

        AlbumResponseDto albumResponseDto = albumService.getAlbumBySearch(userId, tag);
        return ResponseEntity.ok(albumResponseDto);
    }

    @GetMapping("/{userId}/{tagId}/photos")
    @Operation(summary = "앨범의 사진 조회", description = "userId와 tagId를 기반으로 앨범에 포함된 사진들을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 속 사진 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = PhotoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "앨범 또는 사진이 없음")
    })
    public ResponseEntity<List<PhotoResponseDto>> getAlbumPhotos(
            @PathVariable Long userId,
            @PathVariable Long tagId) {

        List<PhotoResponseDto> photos = photoService.getPhotosByUserAndTag(userId, tagId);
        return ResponseEntity.ok(photos);
    }
}
