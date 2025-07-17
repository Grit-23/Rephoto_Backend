package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;


@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private PhotoRepository photoRepository;

    @PostMapping("/batch")
    public ResponseEntity<?> PhotoBatch(@RequestBody PhotoBatchRequest request){
        photoService.saveInitialBatchPhotos(request);
        return ResponseEntity.ok("초기 배치 동기화 완료");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> PhotoSync(@RequestBody PhotoSyncRequest request){
        photoService.saveIncrementalPhotos(request);
        return ResponseEntity.ok("증분 동기화 완료");
    }
}
