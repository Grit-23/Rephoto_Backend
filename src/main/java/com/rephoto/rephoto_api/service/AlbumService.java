package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.dto.AlbumResponseDto;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public List<AlbumResponseDto> getAllAlbums(Long userId) {
        return albumRepository.findByUser_UserId(userId)
                .stream()
                .map(AlbumResponseDto::fromEntity)
                .toList();
    }

    public AlbumResponseDto getAlbumBySearch(Long userId, String tag) {
        Album album = albumRepository.findByUser_UserIdAndTag_TagName(userId, tag)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 앨범이 없습니다"));
        return AlbumResponseDto.fromEntity(album);
    }
}
