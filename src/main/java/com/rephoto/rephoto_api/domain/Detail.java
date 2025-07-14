package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Detail")

public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long detailId;

    @Column(length = 2000, nullable = false)
    String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;
}
