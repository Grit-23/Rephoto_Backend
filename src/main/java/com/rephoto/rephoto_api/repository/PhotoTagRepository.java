package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoTagRepository extends JpaRepository<PhotoTag, Long> {

    @Query("SELECT pt.photo FROM PhotoTag pt WHERE pt.photo.user.userId = :userId AND pt.tag.tagId = :tagId")
    List<Photo> findPhotosByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId);
}
