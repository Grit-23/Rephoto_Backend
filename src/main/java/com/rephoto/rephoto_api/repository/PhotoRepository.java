package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // 특정 사용자의 최신 동기화 이후 등록된 사진
    @Query("SELECT p FROM Photo p WHERE p.user.userId = :userId AND p.createdAt = :createdAt")
    List<Photo> findByUserIdAndCreatedAt(Long userId, LocalDateTime createdAt);

    @Query("SELECT p FROM Photo p WHERE p.user.userId = :userId")
    List<Photo> findByUserId(Long userId);

    List<Photo> findByUserIdAndIsPrivateTrue(Long userId);
    Optional<Photo> findByUserIdAndPhotoId(Long userId, Long photoId);
    void deleteByUserIdAndPhotoId(Long userId, Long photoId);
    List<Photo> findPhotosByUserIdAndTagId(Long userId, Long tagId);

    boolean existsByUserUserIdAndHash(Long userId, String hash);

    Long user(User user);
}
