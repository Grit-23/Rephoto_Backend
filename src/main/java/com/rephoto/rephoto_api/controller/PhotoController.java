package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoResponseDto;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.service.PhotoService;
import com.rephoto.rephoto_api.dto.PhotoBatchRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
@Tag(name = "사진 API", description = "사진 업로드, 조회, 삭제, 동기화 기능을 제공하는 API")
public class PhotoController {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoService photoService;

    @PostMapping("/{userId}/batch")
    @Operation(summary = "초기 사진 일괄 업로드", description = "앱 첫 실행 시 사용자 사진을 한 번에 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초기 배치 동기화 완료"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> photoBatch(@PathVariable("userId") Long userId,
                                        @RequestBody PhotoBatchRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        photoService.savePhotos(request.toPhotoRequestDtoList(), user);
        return ResponseEntity.ok("초기 배치 동기화 완료");
    }

    /* @PostMapping("/sync")
    @Operation(summary = "사진 추가 동기화", description = "이전 동기화 이후에 추가된 사진만 서버에 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 동기화 완료"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> PhotoSync(@PathVariable("userId") Long userId,
                                       @RequestBody PhotoSyncRequestDto request) {
        photoService.saveIncrementalPhotos(request);
        return ResponseEntity.ok("추가 동기화 완료");
    } */

    @GetMapping("/{photoId}")
    @Operation(summary = "단일 사진 조회", description = "photoId를 통해 단일 사진 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 정보 반환",
                    content = @Content(schema = @Schema(implementation = PhotoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 사진을 찾을 수 없음")
    })
    public ResponseEntity<PhotoResponseDto> PhotoDetail(@PathVariable Long photoId) {
        PhotoResponseDto photoResponseDto = photoService.getPhoto(photoId);
        return ResponseEntity.ok(photoResponseDto);
    }

    @DeleteMapping("/{photoId}")
    @Operation(summary = "사진 삭제", description = "photoId를 통해 사진 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 삭제 완료"),
            @ApiResponse(responseCode = "404", description = "해당 사진을 찾을 수 없음")
    })
    public ResponseEntity<?> PhotoDelete(@PathVariable Long photoId) {
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "전체 사진 조회", description = "사용자의 모든 사진 리스트를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 목록 반환",
                    content = @Content(schema = @Schema(implementation = PhotoResponseDto.class)))
    })
    public ResponseEntity<List<PhotoResponseDto>> PhotoList(@PathVariable("userId") Long userId) {
        List<PhotoResponseDto> photoResponseDtos = photoService.getAllPhotos(userId);
        return ResponseEntity.ok(photoResponseDtos);
    }

    @GetMapping("/{userId}/warning")
    @Operation(summary = "민감 사진 조회", description = "민감(개인정보 포함)한 사진 리스트를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "민감 사진 목록 반환",
                    content = @Content(schema = @Schema(implementation = PhotoResponseDto.class)))
    })
    public ResponseEntity<List<PhotoResponseDto>> PhotoWarning(@PathVariable("userId") Long userId) {
        List<PhotoResponseDto> photoResponseDtos = photoService.getWarningPhotos(userId);
        return ResponseEntity.ok(photoResponseDtos);
    }
}
