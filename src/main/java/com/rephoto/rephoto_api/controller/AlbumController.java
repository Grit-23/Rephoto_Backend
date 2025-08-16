package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.domain.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "앨범 API", description = "앨범 리스트, 검색, 상세 사진 조회 API")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    @GetMapping
    @Operation(summary = "사용자의 앨범 리스트 조회", description = "userId에 해당하는 사용자의 앨범 전체를 조회")
    public ResponseEntity<List<AlbumResponseDto>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(albumService.getAllAlbums(user));
    }

    @GetMapping("/search")
    @Operation(summary = "태그 기반 앨범 검색", description = "userId와 tag 키워드로 앨범을 검색하여 반환")
    public ResponseEntity<AlbumResponseDto> search(
            @AuthenticationPrincipal User user,
            @RequestParam String tag) {
        return ResponseEntity.ok(albumService.getAlbumBySearch(user, tag));
    }

    @GetMapping("/{tagId}/photos")
    @Operation(summary = "앨범의 사진 조회", description = "userId와 tagId를 기반으로 앨범에 포함된 사진들을 조회")
    public ResponseEntity<List<PhotoResponseDto>> getAlbumPhotos(
            @AuthenticationPrincipal User user,
            @PathVariable Long tagId) {
        return ResponseEntity.ok(photoService.getPhotosByUserAndTag(user, tagId));
    }
}
