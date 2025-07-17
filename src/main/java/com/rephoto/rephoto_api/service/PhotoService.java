package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.dto.request.PhotoBatchRequest;
import com.rephoto.rephoto_api.dto.request.PhotoSyncRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoService {

    public void saveInitialBatchPhotos(PhotoBatchRequest request) {

    }

    public void saveIncrementalPhotos(PhotoSyncRequest request) {

    }
}
