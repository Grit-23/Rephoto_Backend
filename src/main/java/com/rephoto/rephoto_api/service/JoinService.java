package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.JoinRequestDto;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(JoinRequestDto requestDto) {
        if (userRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 로그인 아이디입니다.");
        }

        User user = User.builder()
                .loginId(requestDto.getLoginId())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .username(requestDto.getUsername())
                .build();

        userRepository.save(user);
    }
}
