package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.LoginRequestDto;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    public String login(LoginRequestDto request) {
        try {
            // 1. 로그인 ID로 사용자 조회
            User user = userRepository.findByLoginId(request.getLoginId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PASSWORD)); // 존재하지 않아도 같은 에러

            // 2. 비밀번호 일치 여부 확인
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }

            // 3. 로그인 상태 true로 설정
            user.setLoggedIn(true);
            userRepository.save(user);

            // 4. 토큰 발급
            return jwtUtil.createToken(user.getLoginId());

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw e; // 500 에러 처리
        }
    }

    // 로그아웃
    public void logout(Long userId, String loginIdFromToken) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (!user.getLoginId().equals(loginIdFromToken)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ACCESS);
            }

            if (!user.isLoggedIn()) {
                throw new CustomException(ErrorCode.ALREADY_LOGGED_OUT);
            }

            user.setLoggedIn(false);
            userRepository.save(user);

        } catch (CustomException e) {
            throw e; // 커스텀 예외는 그대로 전파
        } catch (Exception e) {
            throw new RuntimeException(e); // 500 에러 처리
        }
    }
}
