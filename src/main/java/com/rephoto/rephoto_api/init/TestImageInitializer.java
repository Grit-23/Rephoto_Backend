package com.rephoto.rephoto_api.init;

import com.rephoto.rephoto_api.domain.Description;
import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.service.TagCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

//테스트용 사진 생성 코드

@Slf4j
@Component
@RequiredArgsConstructor
public class TestImageInitializer implements CommandLineRunner{

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final DescriptionRepository descriptionRepository;
    private final TagCommandService tagCommandService;

    @Override
    public void run(String... args) {
        userRepository.findByLoginId("test_user_id").ifPresent(user -> {
            boolean exists = photoRepository.existsByUser_UserIdAndFileName(user.getUserId(), "test-photo-name");
            // 대체 방법: 해당 유저의 동일한 이미지 URL로 존재 여부 확인
            //boolean exists = photoRepository.existsByUserUserIdAndImageUrl(user.getUserId(), "https://example.com/test_photo.jpg");

            if (!exists) {
                log.info("📸 테스트 사진 생성 중...");

                Photo photo = new Photo();
                photo.setUser(user);
                photo.setImageUrl("https://example.com/test_photo.jpg");
                photo.setPrivate(true);
                photo.setLatitude(37.5665);
                photo.setLongitude(126.9780);
                photo.setFileName("test-photo-name");
                photo.setCreatedAt(LocalDateTime.now());

                photoRepository.save(photo);
                log.info("✅ 테스트 사진 생성 완료");

                Description description = Description.builder()
                        .photo(photo)
                        .description("테스트용으로 생성된 설명입니다.")
                        .embedding(Arrays.asList(0.1, 0.2, 0.3))
                        .build();
                descriptionRepository.save(description);
                log.info("📝 테스트 설명 저장 완료: descriptionId={}", description.getDescriptionId());

                List<String> targetTags = Arrays.asList("서울", "야경", "테스트");
                tagCommandService.applyTagsToPhoto(photo, targetTags);
                log.info("🏷️ 태그 매핑 요청 완료: {}", targetTags);

                // 🔁 로그 검증용으로 다시 조회해 실제 매핑된 태그 확인
                Hibernate.initialize(photo.getPhotoTags());

                List<String> savedTags = photo.getPhotoTags().stream()
                        .map(pt -> pt.getTag().getTagName())
                        .toList();

                log.info("📦 최종 사진 상태 -> {{ photoId: {}, imageUrl: {}, latitude: {}, longitude: {}, createdAt: {}, fileName: {}, private: {}, tags: {} }}",
                        photo.getPhotoId(),
                        photo.getImageUrl(),
                        photo.getLatitude(),
                        photo.getLongitude(),
                        photo.getCreatedAt(),
                        photo.getFileName(),
                        photo.isPrivate(),
                        savedTags
                );


            } else {
                log.info("✅ 이미 테스트 사진이 존재합니다.");
            }
        });
    }

}
