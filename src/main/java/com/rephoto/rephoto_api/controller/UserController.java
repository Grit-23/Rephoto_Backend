package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.UserInfoResponseDto;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser
    ) {
        // 1. 사용자 존재 여부 확인
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "회원 정보를 찾을 수 없습니다."));
        }

        User targetUser = optionalUser.get();

        // 2. 현재 로그인한 사용자와 동일한지 확인
        if (!targetUser.getUserId().equals(currentUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "해당 회원 정보에 접근할 수 없습니다."));
        }

        // 3. 성공 응답
        return ResponseEntity.ok(UserInfoResponseDto.from(targetUser));
    }
}
