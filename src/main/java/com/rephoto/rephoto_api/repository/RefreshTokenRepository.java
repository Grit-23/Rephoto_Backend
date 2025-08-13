package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findById(String jti);

    Optional<RefreshToken> findByJtiAndRevokedFalse(String jti);

    long countByUser_UserIdAndRevokedFalseAndExpiresAtAfter(Long userId, LocalDateTime now);
}
