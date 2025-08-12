package com.rephoto.rephoto_api.domain;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class AlbumId implements Serializable {
    private Long user;
    private Long tag;
}


/* @Embeddable
public class AlbumId implements java.io.Serializable {
    private Long userId;
    private Long tagId;
} */
