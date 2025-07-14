package com.rephoto.rephoto_api.domain;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class AlbumId implements Serializable {
    private Long user;
    private Long tag;
}
