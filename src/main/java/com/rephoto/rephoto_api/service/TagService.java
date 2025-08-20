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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 토큰 사용자 ID
    private Long currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        User principal = (User) auth.getPrincipal();
        return principal.getUserId();
    }

    private void assertOwner(Long currentUserId, Photo photo) {
        if (currentUserId == null || !photo.getUser().getUserId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ACCESS);
        }
    }

    // [조회]
    @Transactional(readOnly = true)
    public List<TagResponseDto> listTagsOfPhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        assertOwner(currentUserId(), photo);

        return photoTagRepository.findByPhoto(photo).stream()
                .map(TagResponseDto::of)
                .toList();
    }

    // [추가]
    @Transactional
    public TagResponseDto addTag(Long photoId, String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new CustomException(ErrorCode.TAG_IS_NULL);
        }
        String norm = tagName.trim();

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Long uid = currentUserId();
        assertOwner(uid, photo);

        Tag tag = tagRepository.findByTagName(norm)
                .orElseGet(() -> tagRepository.save(new Tag(norm)));

        if (photoTagRepository.existsByPhotoAndTag(photo, tag)) {
            throw new CustomException(ErrorCode.TAG_ALREADY_EXISTS);
        }

        PhotoTag saved = photoTagRepository.save(new PhotoTag(photo, tag));
        // 앨범 반영
        albumService.manageAlbumForTag(uid, tag.getTagId());
        return TagResponseDto.of(saved);
    }

    // [삭제] photoId + tagId
    @Transactional
    public void deleteTag(Long photoId, Long tagId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Long uid = currentUserId();
        assertOwner(uid, photo);

        Tag tag = tagRepository.findByTagId(tagId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

        PhotoTag mapping = photoTagRepository.findByPhotoAndTag(photo, tag)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

        photoTagRepository.delete(mapping);
        // 앨범 반영
        albumService.manageAlbumForTag(uid, tag.getTagId());
    }

    // [삭제] photoTagId
    @Transactional
    public void deleteTagByMapping(Long photoTagId) {
        PhotoTag mapping = photoTagRepository.findById(photoTagId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

        Photo photo = mapping.getPhoto();
        Long uid = currentUserId();
        assertOwner(uid, photo);

        String tagName = mapping.getTag().getTagName();

        photoTagRepository.delete(mapping);
        albumService.manageAlbumForTag(uid, mapping.getTag().getTagId());
    }

    // [수정/교체] photoTagId 기준
    @Transactional
    public TagResponseDto replacePhotoTag(Long photoTagId, String newTagName) {
        PhotoTag mapping = photoTagRepository.findById(photoTagId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));

        Photo photo = mapping.getPhoto();
        Long uid = currentUserId();
        assertOwner(uid, photo);

        String oldTagName = mapping.getTag().getTagName();
        Tag oldTag = mapping.getTag();

        String norm = (newTagName == null) ? "" : newTagName.trim();
        if (norm.isEmpty()) throw new CustomException(ErrorCode.TAG_IS_NULL);
        if (oldTagName.equals(norm)) return TagResponseDto.of(mapping); // no-op

        // 기존 연결 삭제
        photoTagRepository.delete(mapping);

        // 새 태그 확보/연결
        Tag newTag = tagRepository.findByTagName(norm)
                .orElseGet(() -> {
                            try {
                                return tagRepository.save(new Tag(norm));
                            } catch (DataIntegrityViolationException e) {
                                // 경쟁 상황: 이미 다른 트랜잭션이 먼저 만들었음
                                return tagRepository.findByTagName(norm)
                                        .orElseThrow(() -> e);
                            }
                        });

        PhotoTag newMapping = photoTagRepository.findByPhotoAndTag(photo, newTag)
                .orElseGet(() -> photoTagRepository.save(new PhotoTag(photo, newTag)));

        // 앨범 반영 (감소/증가)
        albumService.manageAlbumForTag(uid, oldTag.getTagId());
        albumService.manageAlbumForTag(uid, newTag.getTagId());

        return TagResponseDto.of(newMapping);
    }
}
