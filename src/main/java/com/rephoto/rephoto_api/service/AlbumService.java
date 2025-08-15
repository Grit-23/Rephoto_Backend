package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.AlbumResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.AlbumRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoTagRepository photoTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    //userId + tagName 기준으로 현재 매핑 개수로 판단하게
    // 그 태그에 사진 10개 이상인데 + 현재 앨범 없으면 -> 앨범 생성
    // 그 태그에 사진 10개 미만인데 + 현재 앨범 있으면 -> 앨범 삭제(태그 수정하거나 삭제하는 경우에 해당)
    private static final long THRESHOLD = 10L;


    @Transactional(readOnly = true)
    public List<AlbumResponseDto> getAllAlbums(Long userId) {

        List<Album> albums = albumRepository.findByUser_UserId(userId);

        if (albums == null || albums.isEmpty()) {
            throw new CustomException(ErrorCode.ALBUM_NOT_FOUND);
        }

        return albums.stream()
                .map(AlbumResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlbumResponseDto getAlbumBySearch(Long userId, String tag) {
        Album album = albumRepository.findByUser_UserIdAndTag_TagName(userId, tag)
                .orElseThrow(() -> new CustomException(ErrorCode.ALBUM_NOT_FOUND));
        return AlbumResponseDto.fromEntity(album);
    }

    //앨범에 사진 등록하기
    public void addPhotoToAlbum(Long albumId, Long photoId) {

    }

    //Tag 가 10개 이상이면 앨범 생성
    @Transactional
    public void manageAlbumForTag(Long userId, String tagName) {

        long count = photoTagRepository.countByPhoto_User_UserIdAndTag_TagName(userId, tagName);
        boolean exists = albumRepository.existsByUser_UserIdAndTag_TagName(userId, tagName);

        if (count >= THRESHOLD && !exists) {

            // 1) tagName -> Tag 조회
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

            // 2) user
            User user = userRepository.getReferenceById(userId);

            // 3) 앨범 생성 -> 연관관계 해결하고
            Album album = new Album();
            album.setUser(user);
            album.setTag(tag);

            albumRepository.save(album);

        }

        if (count < THRESHOLD && exists) {
            albumRepository.deleteByUser_UserIdAndTag_TagName(userId, tagName);
        }
    }
}
