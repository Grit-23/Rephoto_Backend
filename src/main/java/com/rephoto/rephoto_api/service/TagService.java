package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;
    private final PhotoRepository photoRepository;

    public TagResponseDto addTag(Long photoId, String tagName) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

        Tag tag = tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName))); // 없으면 저장

        PhotoTag photoTag = new PhotoTag(photo, tag); // 생성자에서 처리
        photoTagRepository.save(photoTag);

        return TagResponseDto.builder()
                .tagName(tag.getTagName())
                .photo(photo)
                .build();

    }

    public void deleteTag(Long photoId, String tagName) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(()-> new CustomException(ErrorCode.TAG_NOT_FOUND));
        photoTagRepository.deleteByPhotoAndTag(photo, tag);
        //연결된 사진 없으면 tag repository 에서도 그냥 없애는 로직 만들까 생각중...
    }

    public TagResponseDto replaceTag(Long photoId, Long tagId, String tagName) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Tag tag = tagRepository.findByTagId(tagId)
                .orElseThrow(()-> new CustomException(ErrorCode.TAG_NOT_FOUND));
        //일단 원래 tag 를 phototag 테이블에서 삭제
        photoTagRepository.deleteByPhotoAndTag(photo, tag);

        Tag newTag = tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

        photoTagRepository.save(photo, newTag);

        return TagResponseDto.builder()
                .tagName(newTag.getTagName())
                .photo(photo)
                .build();
    }

}
