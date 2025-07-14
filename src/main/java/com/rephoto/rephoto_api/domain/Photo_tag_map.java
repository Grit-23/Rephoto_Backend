package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Photo_tag_map")

public class Photo_tag_map {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "photo_id", nullable = false)
        private Photo photo;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tag_id", nullable = false)
        private Tag tag;
}
