package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public List<Album> getAllAlbums(Long userId) {
        albumRepository.
    }
}
