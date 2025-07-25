package com.rephoto.rephoto_api.init;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//테스트용 사진 생성 코드

@Slf4j
@Component
@RequiredArgsConstructor
public class TestImageInitializer implements CommandLineRunner{

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final DescriptionRepository descriptionRepository;

    @Override
    public void run(String... args) {
        userRepository.findByLoginId("test_user_id").ifPresent(user -> {
            // 대체 방법: 해당 유저의 동일한 이미지 URL로 존재 여부 확인
            boolean exists = photoRepository.existsByUserUserIdAndImageUrl(user.getUserId(), "https://example.com/test_photo.jpg");

            if (!exists) {
                log.info("📸 테스트 사진 생성 중...");

                Photo photo = new Photo();
                photo.setUser(user);
                photo.setImageUrl("https://example.com/test_photo.jpg");
                photo.setPrivate(false);
                photo.setLatitude(37.5665);
                photo.setLongitude(126.9780);
                photo.setFileName("test-photo-name");
                photo.setCreatedAt(LocalDateTime.now());

                photoRepository.save(photo);
                log.info("✅ 테스트 사진 생성 완료");

                // 2. 설명 생성
                Description description = Description.builder()
                        .photo(photo)
                        .description("테스트용으로 생성된 설명입니다.")
                        .vector(0.5F)
                        .build();
                descriptionRepository.save(description);
                log.info("📝 테스트 설명 생성 완료");

            } else {
                log.info("✅ 이미 테스트 사진이 존재합니다.");
            }
        });
    }

}
