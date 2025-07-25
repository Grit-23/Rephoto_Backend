package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.SearchResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.SearchRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public SearchResponseDto searchPhotos(String query, Long userId) {
        try {
            // 1. 쿼리 유효성 체크
            if (query == null || query.trim().isEmpty()) {
                throw new CustomException(ErrorCode.SEARCH_QUERY_REQUIRED);
            }

            // 2. 유저 존재 여부 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 3. 해당 유저의 모든 사진 조회
            List<Photo> userPhotos = photoRepository.findByUser_UserId(userId);

            // 4. Description에서 vector가 있는 것만 필터링
            List<Photo> validPhotos = userPhotos.stream()
                    .filter(photo -> descriptionRepository.findByPhoto(photo)
                            .map(desc -> desc.getVector() != null)
                            .orElse(false))
                    .toList();

            // 5. 임시 결과 도출 (나중에 수학 계산 공식 넣기)
            List<Long> photoIds = validPhotos.stream()
                    .map(Photo::getPhotoId)
                    .sorted()
                    .limit(10)
                    .toList();

            return new SearchResponseDto(query, photoIds);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e); //500 에러 처리
        }
    }

}
