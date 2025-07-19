package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.PhotoListDto;
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
@RequestMapping("/api/photos/{user_id}")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final PhotoService photoService;

    @PostMapping("/batch")
    public ResponseEntity<?> PhotoBatch(@PathVariable("user_id") Long userId, @RequestBody PhotoBatchRequestDto request){
        photoService.saveInitialBatchPhotos(request);
        return ResponseEntity.ok("초기 배치 동기화 완료");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> PhotoSync(@PathVariable("user_id") Long userId, @RequestBody PhotoSyncRequestDto request){
        photoService.saveIncrementalPhotos(request);
        return ResponseEntity.ok("추가 동기화 완료");
    }

    @GetMapping("/{photo_id}")
    public ResponseEntity<?> PhotoDetail(@PathVariable("user_id") Long userId, @PathVariable Long photo_id){
        return null; //임시
    }

    @DeleteMapping("/{photo_id}")
    public ResponseEntity<?> PhotoDelete(@PathVariable("user_id") Long userId, @PathVariable Long photo_id){
        return null; //임시
    }

    @GetMapping("")
    public List<PhotoListDto> PhotoList(@PathVariable("user_id") Long userId){
        return photoService.getAllPhotos(userId);

    }

    @GetMapping("/warning")
    public ResponseEntity<?> PhotoWarning(@PathVariable("user_id") Long userId){
        photoService.getWarningPhotos(userId);
        return ResponseEntity.ok("민감한 사진 리스트 전달 완료");
    }
}
