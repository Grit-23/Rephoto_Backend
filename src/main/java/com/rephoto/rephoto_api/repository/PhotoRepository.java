package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.PhotoListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // 특정 사용자의 최신 동기화 이후 등록된 사진
    List<Photo> findByUserIdAndCreatedAt(Long userId, LocalDateTime createdAt);
    List<Photo> findByUserId(Long userId);
    boolean existsByUserUserIdAndHash(Long userId, String hash);

    Long user(User user);
}
