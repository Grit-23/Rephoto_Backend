package com.rephoto.rephoto_api.domain;

import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode
public class PhotoAlbumId implements Serializable {
    private Long user;
    private Long tag;
}
