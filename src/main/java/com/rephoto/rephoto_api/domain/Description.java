package com.rephoto.rephoto_api.domain;

import com.rephoto.rephoto_api.converter.DoubleListJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Convert(converter = DoubleListJsonConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private List<Double> embedding; // AI로 설명을 벡터화한 값

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false, unique = true)
    private Photo photo;
}
