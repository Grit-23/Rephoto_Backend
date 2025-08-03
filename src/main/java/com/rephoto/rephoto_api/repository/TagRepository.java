package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
    Optional<Tag> findByTagId(Long tagId);
    boolean existsByTagName(String tagName);
    void deleteByTagName(String tagName);
    void saveByTagName(String tagName);


}
