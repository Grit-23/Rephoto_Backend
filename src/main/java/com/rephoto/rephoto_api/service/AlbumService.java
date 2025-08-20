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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AlbumRepository albumRepository;
    private final PhotoTagRepository photoTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    //userId + tagName 기준으로 현재 매핑 개수로 판단하게
    // 그 태그에 사진 10개 이상인데 + 현재 앨범 없으면 -> 앨범 생성
    // 그 태그에 사진 10개 미만인데 + 현재 앨범 있으면 -> 앨범 삭제(태그 수정하거나 삭제하는 경우에 해당)
    private static final long THRESHOLD = 3L;


    @Transactional(readOnly = true)
    public List<AlbumResponseDto> getAllAlbums(User user) {

        List<Album> albums = albumRepository.findByUser_UserId(user.getUserId());

        if (albums == null || albums.isEmpty()) {
            throw new CustomException(ErrorCode.ALBUM_NOT_FOUND);
        }

        return albums.stream()
                .map(AlbumResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlbumResponseDto getAlbumBySearch(User user, String tag) {
        Album album = albumRepository.findByUser_UserIdAndTag_TagName(user.getUserId(), tag)
                .orElseThrow(() -> new CustomException(ErrorCode.ALBUM_NOT_FOUND));
        return AlbumResponseDto.fromEntity(album);
    }

    //앨범에 사진 등록하기
    public void addPhotoToAlbum(Long albumId, Long photoId) {

    }

    //Tag 가 10개 이상이면 앨범 생성
    @Transactional
    public void manageAlbumForTag(Long userId, Long tagId) {
        entityManager.flush(); // 변경사항 반영

        long count = photoTagRepository.countByPhoto_User_UserIdAndTag_TagId(userId, tagId);
        boolean exists = albumRepository.existsByUser_UserIdAndTag_TagId(userId, tagId);

        if (count >= THRESHOLD && !exists) {
            User user = userRepository.getReferenceById(userId);
            Tag tag = tagRepository.getReferenceById(tagId);

            Album album = new Album();
            album.setUser(user);
            album.setTag(tag);
            albumRepository.save(album);
        }

        if (count < THRESHOLD && exists) {
            albumRepository.deleteByUser_UserIdAndTag_TagId(userId, tagId);
        }
    }
}
