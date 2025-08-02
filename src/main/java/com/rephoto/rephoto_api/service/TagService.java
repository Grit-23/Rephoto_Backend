package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;

    public TagResponseDto addTag(Long photoId, String tagName) {
        if(tagRepository.existsByTagName(tagName)){
            Photo photo =
            photoTagRepository.saveByPhotoAndTag();
        }
        else{

        }
    }
}
