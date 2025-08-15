package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;
    private final PhotoRepository photoRepository;
    private final AlbumService albumService;
    private final UserRepository userRepository;


    private void assertOwner(Long userId, Photo photo) {
        if (!photo.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ACCESS); // 적절한 에러코드 사용
        }
    }

    @Transactional
    public TagResponseDto addTag(Long userId, Long photoId, String tagName) {

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        assertOwner(userId, photo);
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName))); // 없으면 저장


        if (photoTagRepository.existsByPhotoAndTag(photo, tag)) {
            throw new CustomException(ErrorCode.TAG_ALREADY_EXISTS);
        }

        photoTagRepository.save(new PhotoTag(photo, tag));

        //album 관리 로직 추가
        albumService.manageAlbumForTag(userId, tagName);

        return TagResponseDto.builder()
                .tagName(tag.getTagName())
                .photo(photo)
                .build();

    }

    @Transactional
    public void deleteTag(Long userId, Long photoId, Long tagId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        assertOwner(userId, photo);

        Tag tag = tagRepository.findByTagId(tagId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

        // 존재 확인
        if (!photoTagRepository.existsByPhotoAndTag(photo, tag)) {
            throw new CustomException(ErrorCode.TAG_NOT_FOUND);
        }

        photoTagRepository.deleteByPhotoAndTag(photo, tag);

        // 삭제 직후 규칙 반영
        albumService.manageAlbumForTag(userId, tag.getTagName());
    }


    @Transactional
    public TagResponseDto replaceTag(Long userId, Long photoId, Long tagId, String newTagName) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        assertOwner(userId, photo);

        Tag oldTag = tagRepository.findByTagId(tagId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));
        String oldTagName = oldTag.getTagName();

        // 기존 매핑이 없으면 예외
        if (!photoTagRepository.existsByPhotoAndTag(photo, oldTag)) {
            throw new CustomException(ErrorCode.TAG_NOT_FOUND);
        }

        // 기존 매핑 삭제 → 감소 반영
        photoTagRepository.deleteByPhotoAndTag(photo, oldTag);
        albumService.manageAlbumForTag(userId, oldTagName);



        // 새 태그 확보
        Tag newTag = tagRepository.findByTagName(newTagName)
                .orElseGet(() -> tagRepository.save(new Tag(newTagName)));

        // 이미 photoTag에 연결되어 있으면 저장 생략
        if (!photoTagRepository.existsByPhotoAndTag(photo, newTag)) {
            photoTagRepository.save(new PhotoTag(photo, newTag));
        }

        // 증가 반영
        albumService.manageAlbumForTag(userId, newTag.getTagName());

        return TagResponseDto.builder()
                .tagName(newTag.getTagName())
                .photo(photo)
                .build();
    }



    /* @Transactional
    public List<TagResponseDto> addTagsFromAi(Long userId, Long photoId, List<String> tagNames) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        assertOwner(userId, photo);

        List<TagResponseDto> result = new ArrayList<>();

        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));

            // 중복 연결 방지 (이미 연결된 경우는 skip)
            if (photoTagRepository.findByPhotoAndTag(photo, tag).isEmpty()) {
                PhotoTag photoTag = new PhotoTag(photo, tag);
                photoTagRepository.save(photoTag);

                result.add(TagResponseDto.builder()
                        .tagName(tag.getTagName())
                        .photo(photo)
                        .build());
            }
            albumService.manageAlbumForTag(userId, tagName);

        }
        return result;
    } */
}
