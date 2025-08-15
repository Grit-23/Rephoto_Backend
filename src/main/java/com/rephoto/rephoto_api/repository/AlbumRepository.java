package com.rephoto.rephoto_api.repository;
import com.rephoto.rephoto_api.domain.Album;
import com.rephoto.rephoto_api.domain.AlbumId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, AlbumId> {

    //해당 유저가 갖고 있는 앨범들(전체)
    List<Album> findByUser_UserId(Long userId);

    // tag 이름인 앨범 반환
    Optional<Album> findByUser_UserIdAndTag_TagName(Long userId, String tagName);

    //tag 취소로 인한 앨범 삭제
    void deleteByUser_UserIdAndTag_TagName(Long userId, String tagName);

    //현재 그 tag 의 앨범이 존재하는지
    boolean existsByUser_UserIdAndTag_TagName(Long userId, String tagName);

    //

}
