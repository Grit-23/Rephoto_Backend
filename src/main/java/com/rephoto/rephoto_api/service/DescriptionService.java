package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.client.AiClient;
import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.AiDescriptionResponseDto;
import com.rephoto.rephoto_api.dto.CaptionResponse;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;
    private final AiClient aiClient;
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

    // 테스트용 임시 설명 생성 코드
    @Transactional
    public void generateDescriptionByAi(Long photoId) {
        try{
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

            // 로그인한 유저 정보 가져옴
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!photo.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_DESCRIPTION_ACCESS);
            }
            Description description = descriptionRepository.findByPhoto(photo)
                    .orElseThrow(() -> new CustomException(ErrorCode.DESCRIPTION_NOT_FOUND));

            if (description.getDescription() != null) {
                throw new CustomException(ErrorCode.DESCRIPTION_ALREADY_EXISTS);
            }

            // AI 서버 호출
            CaptionResponse ai = aiClient.generateCaptionByUrl(photoId, photo.getImageUrl());

            // 설명 저장
            description.setDescription(ai.getCaption());


            // 벡터(임베딩) 저장
            description.setEmbedding(
                    ai.getEmbedding() != null ? ai.getEmbedding() : Collections.emptyList()
            );
            descriptionRepository.save(description);

            // 태그 적용
            if (ai.getTags() != null && !ai.getTags().isEmpty()) {
                tagCommandService.applyTagsToPhoto(photo, ai.getTags());
            }



        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e); //500 에러 처리
        }
    }
}
