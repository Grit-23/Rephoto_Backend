package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // 특정 사용자의 최신 동기화 이후 등록된 사진
    List<Photo> findByUserUserIdAndCreatedAtAfter(Long userId, LocalDateTime createdAt);

    // 중복 방지: 같은 해시를 가진 사진이 이미 있는지
    boolean existsByUserUserIdAndHash(Long userId, String hash);
}
