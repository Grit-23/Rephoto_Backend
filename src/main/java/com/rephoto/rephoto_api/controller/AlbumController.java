package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.dto.AlbumResponseDto;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import com.rephoto.rephoto_api.service.AlbumService;
import com.rephoto.rephoto_api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    @GetMapping("/{userId}")
    public List<Album> AlbumList(@PathVariable Long userId) {
        return albumService.getAllAlbums(userId);
    }

    @GetMapping("")
    public ResponseEntity<AlbumResponseDto> searchAlbum(
            @RequestParam String tag,
            @RequestParam Long userId) {

        AlbumResponseDto albumResponseDto = albumService.getAlbumBySearch(userId, tag);
        return ResponseEntity.ok(albumResponseDto);
    }

    @GetMapping("/{userId}/{tagId}/photos")
    public ResponseEntity<List<PhotoResponseDto>> getAlbumPhotos(
            @PathVariable Long userId,
            @PathVariable Long tagId) {

        List<PhotoResponseDto> photos = photoService.getPhotosByUserAndTag(userId, tagId);
        return ResponseEntity.ok(photos);
    }//앨범 상세 -> 앨범 속 사진 반환 (photoservice 에 함수 만들어둠)

}
