package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import com.rephoto.rephoto_api.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/albums/{user_id}")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;

    @GetMapping("")
    public List<Album> AlbumList(@PathVariable Long user_id) {
        return albumService.getAllAlbums(user_id);
    }
}
