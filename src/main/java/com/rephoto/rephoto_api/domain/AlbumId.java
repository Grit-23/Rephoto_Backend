package com.rephoto.rephoto_api.domain;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AlbumId implements Serializable {
    private Long user;
    private Long tag;
}
