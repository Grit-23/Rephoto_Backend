package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.RefreshToken;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.jwt.TokenHash;
import com.rephoto.rephoto_api.repository.RefreshTokenRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenHash tokenHash;

    @Transactional
    public LoginResponseDto refresh(String refreshToken) {
        try {
            // 1) 형식/타입/서명/만료 검증
            jwtUtil.validateType(refreshToken, "REFRESH");

            // 2) 정보 추출
            String jti = jwtUtil.getJti(refreshToken);
            String loginId = jwtUtil.getLoginId(refreshToken);

            // 3) DB에서 RT 조회 (미폐기 상태만)
            RefreshToken row = refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                    .orElseThrow(() -> new CustomException(ErrorCode.JWT_TOKEN_INVALID));

            // 4) 해시 일치 확인 + 만료 확인
            if (!row.getTokenHash().equals(tokenHash.sha256(refreshToken)) || row.isExpired()) {
                throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
            }

            // 5) 유저 조회
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 6) 회전: 기존 RT 폐기 → 새 RT 저장
            row.setRevoked(true);

            String newAccess = jwtUtil.createAccessToken(loginId);
            String newRefresh = jwtUtil.createRefreshToken(loginId);

            RefreshToken newRow = RefreshToken.builder()
                    .jti(jwtUtil.getJti(newRefresh))
                    .user(user)
                    .tokenHash(tokenHash.sha256(newRefresh))
                    .expiresAt(jwtUtil.parse(newRefresh).getExpiration()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(newRow);

            // 7) 응답
            return new LoginResponseDto(newAccess, newRefresh);

        } catch (CustomException e) {
            throw e; // 비즈니스 예외 그대로
        } catch (Exception e) {
            throw new RuntimeException(e); // 500
        }
    }
}
