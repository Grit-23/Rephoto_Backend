package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.RefreshToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findById(String jti);

    Optional<RefreshToken> findByJtiAndRevokedFalse(String jti);

    long countByUser_UserIdAndRevokedFalseAndExpiresAtAfter(Long userId, LocalDateTime now);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.user.userId = :userId") // 엔티티 필드명에 맞춰 수정
    void deleteAllByUserId(@Param("userId") Long userId);
}
