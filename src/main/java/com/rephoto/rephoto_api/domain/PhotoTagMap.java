package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Photo_Tag_map")
@Getter
@Setter
public class PhotoTagMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoTagId;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "photo_id", nullable = false)
    //private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
