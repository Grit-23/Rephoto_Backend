package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoDto;
import com.rephoto.rephoto_api.dto.PhotoSyncRequestDto;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public void saveInitialBatchPhotos(PhotoBatchRequestDto request) {

    }

    public void saveIncrementalPhotos(PhotoSyncRequestDto request) {

    }

    public List<PhotoDto> getAllPhotos(Long userId) {
        List<Photo> photos = photoRepository.findByUserId(userId);

        return photos.stream()
                .map(photo -> PhotoDto.builder()
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

    public List<PhotoDto> getWarningPhotos(Long userId) {
        List<Photo> photos = photoRepository.findByUserIdAndIsPrivateTrue(userId);
        return photos.stream()
                .map(photo -> PhotoDto.builder()
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

    public PhotoDto getPhoto(Long userId, Long photoId) {
        Photo photo = photoRepository.findByUserIdAndPhotoId(userId, photoId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사진이 없습니다."));
        return PhotoDto.fromEntity(photo);
    }



    public void deletePhoto(Long userId, Long photoId) {
        photoRepository.deleteByUserIdAndPhotoId(userId, photoId);
    }

    public List<PhotoDto> getPhotosByUserAndTag(Long userId, Long tagId) {
        List<Photo> photos = photoRepository.findPhotosByUserIdAndTagId(userId, tagId);
        return photos.stream()
                .map(PhotoDto::fromEntity)
                .collect(Collectors.toList());
    }
}
