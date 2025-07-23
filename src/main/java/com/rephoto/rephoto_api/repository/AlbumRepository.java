package com.rephoto.rephoto_api.repository;
import com.rephoto.rephoto_api.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByUser_UserId(Long userId);
    Optional<Album> findByUser_UserIdAndTag_TagName(Long userId, String tagName);

}
