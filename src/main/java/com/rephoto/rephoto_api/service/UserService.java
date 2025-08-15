package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.UserDeleteRequestDto;
import com.rephoto.rephoto_api.dto.UserUpdateRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.RefreshTokenRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    // 회원 정보 조회
    public User getUserInfo(User currentUser) {
        try {
            User user = userRepository.findById(currentUser.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (!user.getUserId().equals(currentUser.getUserId())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ACCESS);
            }

            return user;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteUser(User currentUser, UserDeleteRequestDto request) {
        try {
            // 1. 존재하는 사용자 ID인지 확인
            Long userId = currentUser.getUserId();
            User targetUser = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 2. 로그인 사용자와 요청 대상 ID가 일치하는지 확인
            if (!targetUser.getUserId().equals(currentUser.getUserId())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_DELETE);
            }

            // 3. 비밀번호 재입력 (본인인지 재확인)
            String password = (request != null) ? request.getPassword() : null;
            if (password == null || !passwordEncoder.matches(password, targetUser.getPassword())) {
                throw new CustomException(ErrorCode.REAUTH_REQUIRED);
            }

            refreshTokenRepository.deleteAllByUserId(userId);

            userRepository.deleteById(userId);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 회원 정보 수정
    @Transactional
    public void updateUser(User currentUser, UserUpdateRequestDto requestDto) {
        try {
            User user = userRepository.findById(currentUser.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (!user.getUserId().equals(currentUser.getUserId())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_UPDATE_ACCESS);
            }

            String password = requestDto.getPassword();
            String username = requestDto.getUsername();

            if ((password != null && password.isBlank()) || (username != null && username.isBlank())) {
                throw new CustomException(ErrorCode.UPDATE_INFO_INVALID);
            }

            if (password != null) {
                user.setPassword(passwordEncoder.encode(password));
            }

            if (username != null) {
                user.setUsername(username);
            }

            userRepository.save(user);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
