package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.JoinRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
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
        try {
            if (userRepository.existsByLoginId(requestDto.getLoginId())) {
                throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
            }

            User user = User.builder()
                    .loginId(requestDto.getLoginId())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .username(requestDto.getUsername())
                    .build();

            userRepository.save(user);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JOIN_FAILED); // 서버 예외
        }
    }
}
