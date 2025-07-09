package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 탈퇴
    public void deleteUser(Long targetUserId, User currentUser) {
        // 1. 존재하는 사용자 ID인지 확인
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 2. 로그인 사용자와 요청 대상 ID가 일치하는지 확인
        if (!targetUser.getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("다른 사용자의 탈퇴 요청은 불가능합니다.");
        }

        // 3. 사용자 삭제
        userRepository.deleteById(targetUserId);
    }
}
