package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.Search;
import com.rephoto.rephoto_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {

    List<Search> findByUserOrderByCreatedAtDesc(User user);
}
