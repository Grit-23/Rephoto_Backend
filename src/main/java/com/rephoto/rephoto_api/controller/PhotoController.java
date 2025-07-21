package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.PhotoDto;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.service.PhotoService;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoSyncRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;

import java.util.List;


@RestController
@RequestMapping("/api/photos/{userId}")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final PhotoService photoService;

    @PostMapping("/batch")
    public ResponseEntity<?> PhotoBatch(@PathVariable("userId") Long userId, @RequestBody PhotoBatchRequestDto request){
        photoService.saveInitialBatchPhotos(request);
        return ResponseEntity.ok("초기 배치 동기화 완료");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> PhotoSync(@PathVariable("userId") Long userId, @RequestBody PhotoSyncRequestDto request){
        photoService.saveIncrementalPhotos(request);
        return ResponseEntity.ok("추가 동기화 완료");
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> PhotoDetail(@PathVariable("userId") Long userId, @PathVariable Long photo_id){
        PhotoDto photoDto =  photoService.getPhoto(userId, photo_id);
        return ResponseEntity.ok(photoDto);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<?> PhotoDelete(@PathVariable("userId") Long userId, @PathVariable Long photo_id){
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("")
    public ResponseEntity<List<PhotoDto>> PhotoList(@PathVariable("userId") Long userId){
        List<PhotoDto> photoDtos = photoService.getAllPhotos(userId);
        return ResponseEntity.ok(photoDtos);
    }

    @GetMapping("/warning")
    public ResponseEntity<List<PhotoDto>> PhotoWarning(@PathVariable("userId") Long userId){
        List<PhotoDto> photoDtos = photoService.getWarningPhotos(userId);
        return ResponseEntity.ok(photoDtos);
       // return ResponseEntity.ok("민감한 사진 리스트 전달 완료");
    }
}
