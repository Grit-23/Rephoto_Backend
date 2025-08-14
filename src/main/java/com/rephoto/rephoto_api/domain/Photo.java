package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long photoId;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPrivate;

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    //자식으로 연관된 것들 -> photo 삭제하면 자식들도 같이 삭제해
    // Description
    @OneToOne(mappedBy = "photo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Description description;

    // PhotoTag
    @OneToMany(mappedBy = "photo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PhotoTag> photoTags = new ArrayList<>();

    // PhotoAlbum
    @OneToMany(mappedBy = "photo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PhotoAlbum> photoAlbums = new ArrayList<>();

}
