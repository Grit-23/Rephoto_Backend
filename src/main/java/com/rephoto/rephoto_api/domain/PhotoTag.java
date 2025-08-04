package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Photo_tag_map")
public class PhotoTag {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long photoTagId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "photo_id", nullable = false)
        private Photo photo;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tag_id", nullable = false)
        private Tag tag;

        public PhotoTag(Photo photo, Tag tag) {
                this.photo = photo;
                this.tag = tag;
        }//TagService 에서 사용하는 인자 두개의 생성자테
}
