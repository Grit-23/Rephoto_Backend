package com.rephoto.rephoto_api.repository;

import com.rephoto.rephoto_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);

}