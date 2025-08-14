package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.domain.PhotoTag;
import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.EmbeddingResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DescriptionRepository descriptionRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PhotoTagRepository photoTagRepository;
    private final AiService aiService;

    public SearchResponseDto searchPhotosByQuery(String query, Long userId) {
        try {
            // 1. 쿼리 유효성 체크
            if (query == null || query.trim().isEmpty()) {
                throw new CustomException(ErrorCode.SEARCH_QUERY_REQUIRED);
            }

            // 2. 유저 존재 여부 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 3. 쿼리를 ai로 전송하여 벡터화(임베딩 값 생성)
            EmbeddingResponse queryEmbeddingResponse = aiService.generateEmbedding(query);
            List<Double> queryEmbedding = queryEmbeddingResponse.getEmbedding();
            if (queryEmbedding == null || queryEmbedding.isEmpty()) {
                throw new CustomException(ErrorCode.AI_EMBEDDING_EMPTY);
            }

            // 4. 해당 유저의 모든 사진 조회
            List<Photo> userPhotos = photoRepository.findByUser_UserId(userId);

            // 5. 그 중 embedding 필드에 값이 있는 것만 필터링
            List<Photo> photosWithEmbedding = userPhotos.stream()
                    .filter(photo -> photo.getDescription() != null
                            && photo.getDescription().getEmbedding() != null
                            && !photo.getDescription().getEmbedding().isEmpty())
                    .collect(Collectors.toList());

            // 6. 코사인 유사도 계산
            List<SearchResponseDto.SearchResults> results = photosWithEmbedding.stream()
                    .map(photo -> {
                        List<Double> photoVec = photo.getDescription().getEmbedding();
                        double sim = cosineSimilarity(queryEmbedding, photoVec); // v1, v2 차원 다르면 CustomException 발생
                        return new AbstractMap.SimpleEntry<>(photo, sim);
                    })
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // 유사도 내림차순
                    .limit(10)
                    .map(entry -> SearchResponseDto.SearchResults.builder()
                            .photoId(entry.getKey().getPhotoId())
                            .imageUrl(entry.getKey().getImageUrl())
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

    // 코사인 유사도 계산식
    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new CustomException(ErrorCode.VECTOR_DIMENSION_MISMATCH);
        }
        double dot = 0.0, normV1 = 0.0, normV2 = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            normV1 += Math.pow(v1.get(i), 2);
            normV2 += Math.pow(v2.get(i), 2);
        }
        return dot / (Math.sqrt(normV1) * Math.sqrt(normV2));
    }
}
