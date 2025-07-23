package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.DescriptionResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final PhotoRepository photoRepository;
    private final DescriptionRepository descriptionRepository;
    //private final AiService aiService;

    public DescriptionResponseDto generateDescription(Long userId, Long photoId) {

        // 1. 사진 조회
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

        // 2. 본인 소유 확인
        if (!photo.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 3. 설명 조회
        Description description = descriptionRepository.findByPhoto(photo)
                .orElseThrow(() -> new CustomException(ErrorCode.DESCRIPTION_GENERATION_FAILED));

        // 4. 이미 설명이 있는 경우
        if (description.getDescription() != null) {
            return DescriptionResponseDto.builder()
                    .descriptionId(description.getDescriptionId())
                    .description(description.getDescription())
                    .photoId(photo.getPhotoId())
                    .build();
        }

        /*
        // 5. AI 설명 생성 요청
        String generatedDescription = aiService.generateDescription(photo.getImageUrl());

        // 6. 저장 및 반환
        description.setDescription(generatedDescription);
        descriptionRepository.save(description);

        return DescriptionResponseDto.builder()
                .descriptionId(description.getDescriptionId())
                .description(generatedDescription)
                .photoId(photo.getPhotoId())
                .build();

         */
        String testDescription = "testDescription_임시 설명 데이터";
        return DescriptionResponseDto.builder()
                .descriptionId(description.getDescriptionId())
                .description(testDescription)
                .photoId(photo.getPhotoId())
                .build();
    }
}
