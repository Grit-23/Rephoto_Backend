package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Description")

public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long descriptionId;

    @Column(length = 2000, nullable = false)
    String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;
}
