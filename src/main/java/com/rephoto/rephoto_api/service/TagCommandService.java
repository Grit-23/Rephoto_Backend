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

        //정규화한 태그명 해당 photo 에 저장(PhotoTag에)
        for (String name : normalized) {
            Tag tag = tagRepository.findByTagName(name)
                    .orElseGet(() -> tagRepository.save(new Tag(name)));

            if (!photoTagRepository.existsByPhotoAndTag(photo, tag)) {
                photoTagRepository.save(new PhotoTag(photo, tag));
            }
        }

        // 태그별로 앨범 관리 호출 (중복 없이)
        Long userId = photo.getUser().getUserId();
        normalized.forEach(n -> albumService.manageAlbumForTag(userId, n));
    }

}
