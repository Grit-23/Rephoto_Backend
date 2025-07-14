package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;

@Entity
@IdClass(PhotoAlbumId.class)
public class Photo_album_map {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // other fields...
}
