package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Album {

    @EmbeddedId
    private AlbumId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY) // ✅ 핵심: OneToOne → ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 다른 필드들(ex. title, coverUrl, createdAt 등)
}

