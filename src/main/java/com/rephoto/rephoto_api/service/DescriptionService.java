package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.ImageCaptionResponse;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;
    private final AiService aiService;
    private final TagCommandService tagCommandService;

    public String getDescription(Long photoId) {
        try {
            // 인증된 사용자 정보 가져오기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof User currentUser)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_DESCRIPTION_ACCESS);
            }

            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

            Description description = descriptionRepository.findByPhoto(photo)
                    .orElseThrow(() -> new CustomException(ErrorCode.DESCRIPTION_NOT_FOUND));

            return description.getDescription() != null ? description.getDescription() : "아직 설명이 생성되지 않았습니다.";

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            // 500에러 처리
            throw new RuntimeException("서버 오류가 발생했습니다.", e);
        }
    }

    // AI 설명 생성 요청
    @Transactional
    public void generateDescriptionByAi() {
            List<Description> nullDescriptions = descriptionRepository.findByDescriptionIsNull();

            if (nullDescriptions.isEmpty()) {
                return; // 생성할 대상 없음
            }

            for (Description description : nullDescriptions) {
                Photo photo = description.getPhoto();

                try {
                    // AI 호출 (AiService의 URL 기반 메서드 사용)
                    ImageCaptionResponse aiResponse =
                            aiService.generateCaptionFromUrl(photo.getImageUrl());

                    // 설명 저장
                    description.setDescription(aiResponse.getExplanation());

                    // 임베딩 저장
                    description.setEmbedding(
                            aiResponse.getExplanation_embedding() != null
                                    ? aiResponse.getExplanation_embedding()
                                    : Collections.emptyList()
                    );

                    descriptionRepository.save(description);

                    // 태그 저장
                    if (aiResponse.getTags() != null && !aiResponse.getTags().isEmpty()) {
                        tagCommandService.applyTagsToPhoto(photo, aiResponse.getTags());
                    }
                    log.info("AI 응답 태그: {}", aiResponse.getTags());


                } catch (Exception e) {
                    log.error("AI 설명 생성 실패: photoId={}", photo.getPhotoId(), e);
                    // 실패한 건 넘어가고 다음 Description 처리
                }
            }
    }
}

