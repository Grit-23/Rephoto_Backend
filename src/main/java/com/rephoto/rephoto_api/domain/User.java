package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
