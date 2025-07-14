package com.rephoto.rephoto_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long albumId;


}
