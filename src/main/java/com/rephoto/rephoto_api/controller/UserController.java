package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.UserInfoResponseDto;
import com.rephoto.rephoto_api.dto.UserUpdateRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.security.UserDetailsImpl;
import com.rephoto.rephoto_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser
    ) {
        // 사용자 존재 여부 확인
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "회원 정보를 찾을 수 없습니다."));
        }
        User targetUser = optionalUser.get();

        // 현재 로그인한 사용자와 동일한지 확인
        if (!targetUser.getUserId().equals(currentUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "해당 회원 정보에 접근할 수 없습니다."));
        }

        return ResponseEntity.ok(UserInfoResponseDto.from(targetUser));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser
    ) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!targetUser.getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
        }
        try {
            userRepository.delete(targetUser);
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DELETE_FAILED);
        }
    }

    //회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            userService.updateUser(userId, currentUser, requestDto);
            return ResponseEntity.ok(Map.of("message", "회원 정보가 성공적으로 수정되었습니다."));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "회원 정보 수정 중 오류가 발생했습니다."));
        }
    }
}
