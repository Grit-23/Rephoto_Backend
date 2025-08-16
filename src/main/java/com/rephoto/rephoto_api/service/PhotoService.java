package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoRequestDto;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoTagRepository photoTagRepository;
    private final DescriptionRepository descriptionRepository;
    private final DescriptionService descriptionService;

    public void savePhotos(List<PhotoRequestDto> dtos, User user) {
        List<Photo> photos = dtos.stream()
                .map(dto -> PhotoRequestDto.toEntity(dto, user))
                .toList();

        photoRepository.saveAll(photos);

        // 설명 테이블 자동으로 생성 (description은 null로 생성됨)
        List<Description> descriptions = photos.stream()
                .map(photo -> Description.builder()
                        .photo(photo)
                        .description(null)
                        .build())
                .toList();

        descriptionRepository.saveAll(descriptions);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 비동기 원하면 @Async 메서드 호출
                    descriptionService.generateDescriptionByAi();
                }
            });
        } else {
            // 트랜잭션이 없으면 그냥 바로 실행(최후의 안전장치)
            descriptionService.generateDescriptionByAi();
        }
    }

    /*public void saveIncrementalPhotos(PhotoSyncRequestDto request) {

    }*/

    public List<PhotoResponseDto> getAllPhotos(User user) {
        List<Photo> photos = photoRepository.findByUser_UserId(user.getUserId());
        return photos.stream()
                .map(PhotoResponseDto::fromEntity)
                .toList();
    }

    public List<PhotoResponseDto> getWarningPhotos(User user) {

        List<Photo> privatePhotos = photoRepository.findByUser_UserIdAndIsPrivateTrue(user.getUserId());
        return privatePhotos.stream()
                .map(photo -> PhotoResponseDto.builder()
                        .photoId(photo.getPhotoId())
                        .imageUrl(photo.getImageUrl())
                        .isPrivate(photo.isPrivate())
                        .latitude(photo.getLatitude())
                        .longitude(photo.getLongitude())
                        .createdAt(photo.getCreatedAt())
                        .build()
                )
                .toList();

    }

    public PhotoResponseDto getPhoto(Long photoId) {
        Photo photo = photoRepository.findByPhotoId(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        return PhotoResponseDto.fromEntity(photo);
    }


    @Transactional(readOnly = false)
    public void deletePhoto(Long photoId) {
        photoRepository.deleteByPhotoId(photoId);
    }

    public List<PhotoResponseDto> getPhotosByUserAndTag(User user, Long tagId) {
        List<Photo> photos = photoTagRepository.findPhotosByUserIdAndTagId(user.getUserId(), tagId);
        return photos.stream()
                .map(PhotoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
