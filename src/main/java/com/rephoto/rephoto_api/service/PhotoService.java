package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoListDto;
import com.rephoto.rephoto_api.dto.PhotoSyncRequestDto;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public void saveInitialBatchPhotos(PhotoBatchRequestDto request) {

    }

    public void saveIncrementalPhotos(PhotoSyncRequestDto request) {

    }

    public List<PhotoListDto> getAllPhotos(Long userId) {
        List<Photo> photos = photoRepository.findByUserId(userId);

        return photos.stream()
                .map(photo -> PhotoListDto.builder()
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

    public List<PhotoListDto> getWarningPhotos(Long userId) {

    }

}
