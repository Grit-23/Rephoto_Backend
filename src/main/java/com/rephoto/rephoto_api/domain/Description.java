package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Description")
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "description_id")
    private Long descriptionId;

    @Column(length = 2000, nullable = true)
    private String description;

    @Column(length = 1000, nullable = true)
    private Float vector; // AI로 설명을 벡터화한 값

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false, unique = true)
    private Photo photo;
}
