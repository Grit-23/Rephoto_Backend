package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TagCommandService {

    private final TagRepository tagRepository;
    private final PhotoTagRepository photoTagRepository;
    private final AlbumService albumService;

    @Transactional
    public void applyTagsToPhoto(Photo photo, List<String> tagNames) {
        if (tagNames == null) return;

        // 정규화 & 중복 제거
        List<String> normalized = tagNames.stream()
                .filter(s -> s != null)
                .map(s -> s.trim())
                .filter(s -> !s.isBlank())
                .map(s -> s.toLowerCase()) // 선택: 소문자 통일
                .distinct()
                .toList();

        Map<String, Tag> tagByName = new HashMap<>();
        for (String name : normalized) {
            Tag tag = tagRepository.findByTagName(name)
                    .orElseGet(() -> {
                        try {
                            return tagRepository.save(new Tag(name));
                        } catch (DataIntegrityViolationException e) {
                            // 다른 트랜잭션/쓰레드가 같은 이름으로 먼저 만든 경우
                            return tagRepository.findByTagName(name).orElseThrow(() -> e);
                        }
                    });
            tagByName.put(name, tag);
        }

        //정규화한 태그명 해당 photo 에 저장(PhotoTag에)
        for (String name : normalized) {
            Tag tag = tagRepository.findByTagName(name)
                    .orElseGet(() -> tagRepository.save(new Tag(name)));

            if (!photoTagRepository.existsByPhotoAndTag(photo, tag)) {
                photoTagRepository.save(new PhotoTag(photo, tag));
            }
        }


        Long userId = photo.getUser().getUserId();
        // 중복 호출 방지: tagId 기준 distinct
        tagByName.values().stream()
                .map(Tag::getTagId)
                .distinct()
                .forEach(tagId -> albumService.manageAlbumForTag(userId, tagId));
    }

}
