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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        if (photoTagRepository.existsByPhotoAndTag(photo, tag)) {
            throw new CustomException(ErrorCode.TAG_ALREADY_EXISTS);
        }
        PhotoTag photoTag = new PhotoTag(photo, tag); // 생성자에서 처리
        photoTagRepository.save(photoTag);

        return TagResponseDto.builder()
                .tagName(tag.getTagName())
                .photo(photo)
                .build();

    }

    @Transactional
    public List<TagResponseDto> addTagsFromAi(Long photoId, List<String> tagNames) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

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
        }
        return result;
    }


    public void deleteTag(Long photoId, Long tagId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Tag tag = tagRepository.findByTagId(tagId)
                .orElseThrow(()-> new CustomException(ErrorCode.TAG_NOT_FOUND));
        photoTagRepository.deleteByPhotoAndTag(photo, tag);
        //연결된 사진 없으면 tag repository 에서도 그냥 없애는 로직 만들까 생각중...
    }

    @Transactional
    public TagResponseDto replaceTag(Long photoId, Long tagId, String tagName) {

        //해당 사진 찾고 , 원래 태그 찾고
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        Tag tag = tagRepository.findByTagId(tagId)
                .orElseThrow(()-> new CustomException(ErrorCode.TAG_NOT_FOUND));

        //일단 원래 tag 를 phototag 테이블에서 삭제
        photoTagRepository.deleteByPhotoAndTag(photo, tag);

        //바꾸려는 태그 검색 -> 없으면 추가
        Tag newTag = tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

        //바꾸려는 태그 저장
        PhotoTag saved = photoTagRepository.save(new PhotoTag(photo, newTag));

        return TagResponseDto.builder()
                .tagName(newTag.getTagName())
                .photo(photo)
                .build();
    }

}
