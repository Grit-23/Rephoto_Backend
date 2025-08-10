package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagCommandService {

    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;

    @Transactional
    public void applyTagsToPhoto(Photo photo, List<String> tagNames) {
        //AI 통해 처음 태그 받을 때 사용
        if (tagNames == null) return;

        tagNames.stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isBlank())
                .distinct()
                .forEach(name -> {
                    Tag tag = tagRepository.findByTagName(name)
                            .orElseGet(() -> tagRepository.save(new Tag(name)));

                    if (!photoTagRepository.existsByPhotoAndTag(photo, tag)) {
                        photoTagRepository.save(new PhotoTag(photo, tag));
                    }
                });
    }
}
