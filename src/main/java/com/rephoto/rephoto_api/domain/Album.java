package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(AlbumId.class)
@Getter @Setter
@Table(
        name = "album",
        uniqueConstraints = @UniqueConstraint(name = "uk_album_user_tag", columnNames = {"user_id", "tag_id"})
)
public class Album {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 다른 필드들(ex. title, coverUrl, createdAt 등)
}

