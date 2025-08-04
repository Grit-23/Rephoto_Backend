package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoRequestDto;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    }

    /*public void saveIncrementalPhotos(PhotoSyncRequestDto request) {

    }*/

    public List<PhotoResponseDto> getAllPhotos(Long userId) {
        List<Photo> photos = photoRepository.findByUser_UserId(userId);

        return photos.stream()
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

    public List<PhotoResponseDto> getWarningPhotos(Long userId) {
        List<Photo> photos = photoRepository.findByUser_UserIdAndIsPrivateTrue(userId);
        return photos.stream()
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
                .orElseThrow(() -> new EntityNotFoundException("해당 사진이 없습니다."));
        return PhotoResponseDto.fromEntity(photo);
    }



    public void deletePhoto(Long photoId) {
        photoRepository.deleteByPhotoId(photoId);
    }

    public List<PhotoResponseDto> getPhotosByUserAndTag(Long userId, Long tagId) {
        List<Photo> photos = photoTagRepository.findPhotosByUserIdAndTagId(userId, tagId);
        return photos.stream()
                .map(PhotoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
