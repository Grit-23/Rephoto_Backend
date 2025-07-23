package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.service.PhotoService;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import com.rephoto.rephoto_api.dto.PhotoSyncRequestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/photos/{userId}")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoService photoService;

    @PostMapping("/batch")
    public ResponseEntity<?> photoBatch(@PathVariable("userId") Long userId,
                                        @RequestBody PhotoBatchRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        photoService.savePhotos(request.toPhotoRequestDtoList(), user);

        return ResponseEntity.ok("초기 배치 동기화 완료");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> PhotoSync(@PathVariable("userId") Long userId, @RequestBody PhotoSyncRequestDto request){
        photoService.saveIncrementalPhotos(request);
        return ResponseEntity.ok("추가 동기화 완료");
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDto> PhotoDetail(@PathVariable("userId") Long userId, @PathVariable Long photo_id){
        PhotoResponseDto photoResponseDto =  photoService.getPhoto(userId, photo_id);
        return ResponseEntity.ok(photoResponseDto);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<?> PhotoDelete(@PathVariable("userId") Long userId, @PathVariable Long photo_id){
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("")
    public ResponseEntity<List<PhotoResponseDto>> PhotoList(@PathVariable("userId") Long userId){
        List<PhotoResponseDto> photoResponseDtos = photoService.getAllPhotos(userId);
        return ResponseEntity.ok(photoResponseDtos);
    }

    @GetMapping("/warning")
    public ResponseEntity<List<PhotoResponseDto>> PhotoWarning(@PathVariable("userId") Long userId){
        List<PhotoResponseDto> photoResponseDtos = photoService.getWarningPhotos(userId);
        return ResponseEntity.ok(photoResponseDtos);
       // return ResponseEntity.ok("민감한 사진 리스트 전달 완료");
    }
}
