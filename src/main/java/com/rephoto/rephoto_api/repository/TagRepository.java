package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameIn(List<String> tagNames);
    boolean existsByTagName(String tagName);
    void deleteByTagName(String tagName);
    void

}
