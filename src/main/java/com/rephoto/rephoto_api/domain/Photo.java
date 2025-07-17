package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Photo")


public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long photoId;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPrivate;

    @Column(nullable = true)
    private double latitude;

    @Column(nullable = true)
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String hash;


}
