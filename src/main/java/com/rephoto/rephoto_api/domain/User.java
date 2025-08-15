package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(length = 20, nullable = false)
    String username;

    @Column(length = 20, nullable = false)
    String loginId;

    @Column(length = 70, nullable = false)
    String password;

    @Column(nullable = false)
    private boolean isLoggedIn; // 로그인 상태 여부 확인용

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

}
