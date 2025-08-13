package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.SearchResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.PhotoTagRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoTagRepository photoTagRepository;

    public SearchResponseDto searchPhotosByQuery(String query, Long userId) {
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

            // 4. Description에서 embedding 있는 것만 필터링
            List<Photo> validPhotos = userPhotos.stream()
                    .filter(photo -> descriptionRepository.findByPhoto(photo)
                            .map(desc -> desc.getEmbedding() != null)
                            .orElse(false))
                    .toList();

            // 5. 임시 결과 도출 (나중에 수학 계산 공식 넣기)
            List<SearchResponseDto.SearchResults> results = validPhotos.stream()
                    .sorted(Comparator.comparing(Photo::getPhotoId))
                    .limit(10)
                    .map(p -> SearchResponseDto.SearchResults.builder()
                            .photoId(p.getPhotoId())
                            .imageUrl(p.getImageUrl())
                            .build())
                    .toList();

            return SearchResponseDto.builder()
                    .query(query)
                    .searchResults(results)
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e); //500 에러 처리
        }
    }

    public SearchResponseDto searchPhotosByTags(String tagQuery, Long userId) {

        try {
            if (tagQuery == null || tagQuery.trim().isEmpty()) {
                throw new CustomException(ErrorCode.SEARCH_QUERY_REQUIRED);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // #으로 시작하는 태그 최대 20개 추출
            List<String> tagNames = Arrays.stream(tagQuery.trim().split("\\s+"))
                    .filter(word -> word.startsWith("#"))
                    .map(tag -> tag.substring(1))
                    .distinct()
                    .limit(20)
                    .toList();

            if (tagNames.isEmpty()) {
                throw new CustomException(ErrorCode.SEARCH_TAGS_REQUIRED);
            }

            Set<Photo> resultPhotos = new HashSet<>();

            for (String tagName : tagNames) {
                List<PhotoTag> matches = photoTagRepository.findAll().stream()
                        .filter(pt -> pt.getPhoto().getUser().getUserId().equals(userId))
                        .filter(pt -> pt.getTag().getTagName().equals(tagName))
                        .toList();

                for (PhotoTag pt : matches) {
                    resultPhotos.add(pt.getPhoto());
                }
            }

            List<SearchResponseDto.SearchResults> results = resultPhotos.stream()
                    .sorted(Comparator.comparing(Photo::getPhotoId))
                    .map(p -> SearchResponseDto.SearchResults.builder()
                            .photoId(p.getPhotoId())
                            .imageUrl(p.getImageUrl())
                            .build())
                    .toList();

            return SearchResponseDto.builder()
                    .query(tagQuery)
                    .searchResults(results)
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("태그 검색 중 서버 오류 발생", e);
        }
    }
}
