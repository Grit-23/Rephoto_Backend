package com.rephoto.rephoto_api.init;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestUserInitializer implements CommandLineRunner{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByLoginId("test_user_id").isEmpty()) {
            log.info("🔧 테스트 사용자 생성 중...");

            User testUser = User.builder()
                    .username("테스트유저")
                    .loginId("test_user_id")
                    .password(passwordEncoder.encode("testpassword00"))
                    .build();

            userRepository.save(testUser);
            log.info("테스트 사용자 생성 완료");
        }
    }
}
