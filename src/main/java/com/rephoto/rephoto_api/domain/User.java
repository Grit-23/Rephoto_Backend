package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(length = 20, nullable = false)
    String username;

    @Column(length = 20, nullable = false)
    String loginId;

    @Column(length = 20, nullable = false)
    String password;

}
