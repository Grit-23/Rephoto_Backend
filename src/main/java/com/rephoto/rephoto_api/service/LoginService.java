package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.RefreshToken;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.LoginRequestDto;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.jwt.TokenHash;
import com.rephoto.rephoto_api.repository.RefreshTokenRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHash tokenHash;

    // 로그인
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            //로그인 ID로 사용자 조회
            User user = userRepository.findByLoginId(request.getLoginId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PASSWORD)); // 존재하지 않아도 같은 에러

            // 비밀번호 일치 여부 확인
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }
            // 토큰 발급
            String accessToken = jwtUtil.createAccessToken(user.getLoginId());
            String refreshToken = jwtUtil.createRefreshToken(user.getLoginId());

            // RefreshToken DB에 저장
            RefreshToken rt = RefreshToken.builder()
                    .jti(jwtUtil.getJti(refreshToken))
                    .user(user)
                    .tokenHash(tokenHash.sha256(refreshToken))
                    .expiresAt(jwtUtil.parse(refreshToken).getExpiration()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(rt);

            // 로그인 상태 true로 설정
            user.setLoggedIn(true);
            userRepository.save(user);

            return new LoginResponseDto(accessToken, refreshToken);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw e; // 500 에러 처리
        }
    }

    // 로그아웃
    @Transactional
    public void logout(String refreshToken) {
        try {
            // RefreshToken인지 확인
            jwtUtil.validateType(refreshToken, "REFRESH");

            String jti = jwtUtil.getJti(refreshToken);
            String loginId = jwtUtil.getLoginId(refreshToken);

            //유저 조회
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 로그아웃 여부 확인
            if (!user.isLoggedIn()) {
                throw new CustomException(ErrorCode.ALREADY_LOGGED_OUT);
            }

            // RefreshToken을 조회
            RefreshToken rt = refreshTokenRepository.findById(jti)
                    .orElseThrow(() -> new CustomException(ErrorCode.JWT_TOKEN_INVALID));

            // 저장된 해시와 비교
            if (!rt.getTokenHash().equals(tokenHash.sha256(refreshToken))) {
                throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
            }

            // 토큰 삭제
            rt.setRevoked(true);

            // 남은 refreshToken 조회
            long active = refreshTokenRepository.countByUser_UserIdAndRevokedFalseAndExpiresAtAfter(
                    user.getUserId(), LocalDateTime.now());

            // 로그아웃 처리 후 로그인 여부 false로 저장
            if (active == 0) {
                user.setLoggedIn(false);
                userRepository.save(user);
            }

        } catch (CustomException e) {
            throw e; // 커스텀 예외는 그대로 전파
        } catch (Exception e) {
            throw new RuntimeException(e); // 500 에러 처리
        }
    }
}
