package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;

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
            throw new RuntimeException("AI 설명 생성 중 오류가 발생했습니다.", e);
        }
    }

    // 테스트용 임시 설명 생성 코드
    @Transactional
    public void generateDescriptionManually(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

        Description description = descriptionRepository.findByPhoto(photo)
                .orElseThrow(() -> new CustomException(ErrorCode.DESCRIPTION_NOT_FOUND));

        // 실제 AI 연동은 빠진 상태이므로, 임의 설명을 삽입
        String aiDescription = "이것은 AI가 자동으로 생성한 예시 설명입니다.";

        description.setDescription(aiDescription);
        descriptionRepository.save(description);
    }
}
