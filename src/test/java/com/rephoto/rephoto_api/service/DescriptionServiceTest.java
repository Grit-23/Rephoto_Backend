package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DescriptionServiceTest {

    @Autowired
    private DescriptionService descriptionService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void generateDescriptionManually_ShouldSaveDummyDescription() {
        // 테스트용 유저 생성
        User user = User.builder()
                .loginId("testuser")
                .password("password")
                .username("테스트유저")
                .build();
        userRepository.save(user);

        // 테스트용 사진 생성
        Photo photo = new Photo();
        photo.setUser(user);
        photo.setImageUrl("https://example.com/test.jpg");
        photo.setLatitude(37.5665);
        photo.setLongitude(126.9780);
        photo.setFileName("test-photo-name");
        photo.setCreatedAt(LocalDateTime.now());

        photoRepository.save(photo);

        // 설명 엔티티는 사진 등록 시 자동 생성되므로 수동 생성
        Description description = Description.builder()
                .photo(photo)
                .description(null)
                .build();
        descriptionRepository.save(description);

        // 테스트 실행
        descriptionService.generateDescriptionByAi(photo.getPhotoId());

        // 결과 확인
        Description updated = descriptionRepository.findByPhoto(photo)
                .orElseThrow(() -> new RuntimeException("설명 없음"));

        assertNotNull(updated.getDescription());
        System.out.println("✅ 생성된 설명: " + updated.getDescription());
    }
}
