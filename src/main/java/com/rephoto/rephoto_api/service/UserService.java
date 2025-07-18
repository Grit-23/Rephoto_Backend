package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.UserUpdateRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴
    public void deleteUser(Long targetUserId, User currentUser) {
        try {
            // 1. 존재하는 사용자 ID인지 확인
            User targetUser = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 2. 로그인 사용자와 요청 대상 ID가 일치하는지 확인
            if (!targetUser.getUserId().equals(currentUser.getUserId())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
            }

            // 3. 사용자 삭제
            userRepository.deleteById(targetUserId);

        } catch (CustomException e) {
            throw e; // 그대로 던짐 (컨트롤러 혹은 @ControllerAdvice에서 처리)
        } catch (Exception e) {
            // 예상치 못한 예외 처리
            throw new CustomException(ErrorCode.DELETE_FAILED);
        }
    }

    // 회원 정보 수정
    @Transactional
    public void updateUser(Long userId, User currentUser, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE_ACCESS);
        }

        String password = requestDto.getPassword();
        String username = requestDto.getUsername();

        if ((password != null && password.isBlank()) || (username != null && username.isBlank())) {
            throw new CustomException(ErrorCode.UPDATE_INFO_INVALID);
        }

        // 실제 값이 null이 아니고 비어 있지 않다면 수정
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (username != null) {
            user.setUsername(username);
        }

        userRepository.save(user);
    }
}
