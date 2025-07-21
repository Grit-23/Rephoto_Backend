package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.dto.AlbumDto;
import com.rephoto.rephoto_api.dto.PhotoDto;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import com.rephoto.rephoto_api.service.AlbumService;
import com.rephoto.rephoto_api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    @GetMapping("/{user_id}")
    public List<Album> AlbumList(@PathVariable Long user_id) {
        return albumService.getAllAlbums(user_id);
    }

    @GetMapping("/albums")
    public ResponseEntity<AlbumDto> searchAlbum(
            @RequestParam String tag,
            @RequestParam Long userId) {

        Optional<Album> album = albumRepository.findByUser_UserIdAndTag_TagName(userId, tag);

        if (album.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AlbumDto dto = AlbumDto.fromEntity(album.get());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}/{tagId}/photos")
    public ResponseEntity<List<PhotoDto>> getAlbumPhotos(
            @PathVariable Long userId,
            @PathVariable Long tagId) {

        List<PhotoDto> photos = photoService.getPhotosByUserAndTag(userId, tagId);
        return ResponseEntity.ok(photos);
    }

}
