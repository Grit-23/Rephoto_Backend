package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    long countByUser_UserId(Long userId);
    //Optional<Photo> findFirstByUser_UserIdOrderByCreatedAtDesc(Long userId);
    //현재 저장된 DB에서 가장 최신 날짜를 불러옴

    List<Photo> findByUser_UserId(Long userId);
    //전체 사진 리스트
    List<Photo> findByUser_UserIdAndIsPrivateTrue(Long userId);
    //민감 정보 사진 리스트
    Optional<Photo> findByPhotoId(Long photoId);
    //사진 상세 정보
    void deleteByPhotoId(Long photoId);
    // 사진 삭제
    //List<Photo> findPhotosByUserIdAndTagId(Long userId, Long tagId);
    //앨범 상세 정보 -> 앨범에 저장된 사진 리스트

    boolean existsByUser_UserIdAndFileName(Long userId, String fileName);


    // 지도 범위 내의 사진들을 가져오기 위한 메서드
    List<Photo> findByUser_UserIdAndLatitudeBetweenAndLongitudeBetween(
            Long userId, Double minLat, Double maxLat, Double minLng, Double maxLng
    );
    boolean existsByUserUserIdAndImageUrl(Long userId, String imageUrl);


}

