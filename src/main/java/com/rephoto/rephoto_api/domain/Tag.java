package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tagId;

    @Column(length = 20, nullable = false, unique = true)
    String tagName;

}
