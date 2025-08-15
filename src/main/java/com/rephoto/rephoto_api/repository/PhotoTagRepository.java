package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.Tag;
import com.rephoto.rephoto_api.dto.TagResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, Long> {

    @Query("""
        SELECT pt.photo FROM PhotoTag pt
        WHERE pt.photo.user.userId = :userId AND pt.tag.tagId = :tagId
    """)
    List<Photo> findPhotosByUserIdAndTagId(Long userId, Long tagId);

    @Query("""
        SELECT pt.photo FROM PhotoTag pt
        WHERE pt.photo.user.userId = :userId AND pt.tag.tagName = :tagName
    """)
    List<Photo> findPhotosByUserIdAndTagName(Long userId, String tagName);


    Optional<PhotoTag> findByPhotoAndTag(Photo photo, Tag tag);

    void deleteByPhotoAndTag(Photo photo, Tag tag);

    boolean existsByPhotoAndTag(Photo photo, Tag tag);

    // userId + tagName 조합의 사진-태그 매핑 개수
    long countByPhoto_User_UserIdAndTag_TagName(Long userId, String tagName);

    // photoId + tagId로 매핑 찾기 (수정/삭제용)
    Optional<PhotoTag> findByPhoto_PhotoIdAndTag_TagId(Long photoId, Long tagId);
}

