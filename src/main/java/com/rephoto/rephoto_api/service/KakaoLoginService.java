package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.KakaoLoginRequestDto;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.kakao.KakaoApiClient;
import com.rephoto.rephoto_api.kakao.KakaoInfoResponse;
import com.rephoto.rephoto_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponseDto kakaoLogin(KakaoLoginRequestDto kakaoRequestDto) {
        try {
            KakaoInfoResponse kakaoInfo = kakaoApiClient.getKakaoUserInfo(kakaoRequestDto.getAccessToken());

            if (kakaoInfo == null || kakaoInfo.getId() == null ||
                    kakaoInfo.getKakao_account() == null ||
                    kakaoInfo.getKakao_account().getProfile() == null) {
                throw new CustomException(ErrorCode.KAKAO_AUTH_FAILED);
            }

            String kakaoId = String.valueOf(kakaoInfo.getId());
            String kakaoNickname = kakaoInfo.getKakao_account().getProfile().getNickname();

            User user = userRepository.findByLoginId(kakaoId)
                    .orElseGet(() -> userRepository.save(User.builder()
                            .loginId(kakaoId)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .username(kakaoNickname)
                            .build()));

            String token = jwtUtil.createToken(user.getLoginId());
            return new LoginResponseDto(token);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
    }
}