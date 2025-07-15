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

            // 3. 토큰 발급
            return jwtUtil.createToken(user.getLoginId());

        } catch (CustomException e) {
            throw e; // 위에서 직접 발생시킨 CustomException은 그대로 전달
        } catch (Exception e) {
            // 예상치 못한 서버 오류
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
    }

    // 로그아웃
    public Map<String, String> logout() {
        try {
            // 로그아웃 시 클라이언트 측에서 토큰 삭제
            return Map.of("message", "로그아웃 되었습니다. (클라이언트 측에서 토큰 삭제 필요)");
        } catch (Exception e) {
            // 서버 오류 발생 시
            throw new CustomException(ErrorCode.LOGOUT_FAILED);
        }
    }
}
