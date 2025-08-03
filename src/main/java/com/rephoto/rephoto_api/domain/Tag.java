package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tagId;

    @Column(length = 20, nullable = false, unique = true)
    String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }//TagService에서 사용하는 인자 1개 있는 생성자

}
