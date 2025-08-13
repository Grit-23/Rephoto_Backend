package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {

    Optional<Description> findByPhoto(Photo photo);

    // 설명이 null인 사진 조회(ai 전달용)
    List<Description> findByDescriptionIsNull();
}
