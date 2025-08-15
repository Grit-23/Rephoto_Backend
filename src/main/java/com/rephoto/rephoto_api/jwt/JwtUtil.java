package com.rephoto.rephoto_api.jwt;

import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "GRIT_SECRET_123456789012345678901234"; // 최소 32바이트 권장
    private static final long ACCESS_EXP_MS  = 1000L * 60 * 30;         // 30분
    private static final long REFRESH_EXP_MS = 1000L * 60 * 60 * 24 * 30; // 30일

    private static final String CLAIM_TOKEN_TYPE = "token_type";

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Base64 필요 없음, 자동 처리
    }

    // 토큰 발급
    public String createAccessToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .claim(CLAIM_TOKEN_TYPE, "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String loginId) {
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(jti) // jti를 넣어두면 Redis등에서 관리하기 좋음
                .setSubject(loginId)
                .claim(CLAIM_TOKEN_TYPE, "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 파싱 조회
    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String getLoginId(String token) {
        return parse(token).getSubject();
    }

    public String getTokenType(String token) {
        Object v = parse(token).get(CLAIM_TOKEN_TYPE);
        return v == null ? null : v.toString();
    }

    public String getJti(String token) {
        return parse(token).getId();
    }

    // 검증
    public void validate(String token) {
        try {
            parse(token); // 예외 없으면 OK
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
        }
    }

    public void validateType(String token, String expectedType) {
        validate(token);
        if (!expectedType.equals(getTokenType(token))) {
            throw new CustomException(ErrorCode.JWT_TOKEN_INVALID); // 타입 불일치
        }
    }

    // 파싱
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
    }

}