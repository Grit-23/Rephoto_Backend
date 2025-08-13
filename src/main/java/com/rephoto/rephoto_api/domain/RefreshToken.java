package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String jti; // JWT의 고유 ID (UUID)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 어떤 유저의 토큰인지

    @Column(nullable = false, length = 64)
    private String tokenHash; // Refresh Token 해시 (SHA-256)

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 만료 시각

    @Column(nullable = false)
    private boolean revoked = false; // 폐기 여부

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
