package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.dto.AlbumDto;
import com.rephoto.rephoto_api.dto.PhotoDto;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public List<Album> getAllAlbums(Long userId) {
        List<Album> albums = albumRepository.findByUser_UserId(userId);
        return albums;
    }

    public AlbumDto getAlbumBySearch(Long userId, String tag) {
        Album album = albumRepository.findByUser_UserIdAndTag_TagName(userId, tag)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 앨범이 없습니다"));
        return AlbumDto.fromEntity(album);
    }
}
