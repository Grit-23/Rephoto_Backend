package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.ClusterRequestDto;
import com.rephoto.rephoto_api.dto.MapRequestDto;
import com.rephoto.rephoto_api.dto.MapResponseDto;
import com.rephoto.rephoto_api.dto.PhotoRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {
    private final PhotoRepository photoRepository;

    public List<MapResponseDto> getClusteredPhotos(Long userId, MapRequestDto request) {
        try {
            // 1. 파라미터 검증
            if (request.getMinLat() == 0 || request.getMaxLat() == 0 ||
                    request.getMinLng() == 0 || request.getMaxLng() == 0) {
                throw new CustomException(ErrorCode.MAP_PARAMS_REQUIRED);
            }

            if (userId == null) {
                throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
            }

            // 2. 해당 유저의 사진 전체 조회
            List<Photo> userPhotos = photoRepository.findByUser_UserId(userId);

            // 3. 위치 필터링
            List<Photo> filtered = userPhotos.stream()
                    .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                    .filter(p -> p.getLatitude() >= request.getMinLat() && p.getLatitude() <= request.getMaxLat())
                    .filter(p -> p.getLongitude() >= request.getMinLng() && p.getLongitude() <= request.getMaxLng())
                    .toList();

            // 4. 클러스터링: zoomLevel 기반 셀 크기 계산
            double cellSize = getCellSizeByZoom(request.getZoomLevel());
            Map<String, List<Photo>> clusterMap = new HashMap<>();

            for (Photo photo : filtered) {
                int latIdx = (int) (photo.getLatitude() / cellSize);
                int lngIdx = (int) (photo.getLongitude() / cellSize);
                String key = latIdx + "_" + lngIdx;
                clusterMap.computeIfAbsent(key, k -> new ArrayList<>()).add(photo);
            }

            // 5. 응답 구성
            List<MapResponseDto> result = new ArrayList<>();
            for (List<Photo> cluster : clusterMap.values()) {
                double avgLat = cluster.stream().mapToDouble(Photo::getLatitude).average().orElse(0);
                double avgLng = cluster.stream().mapToDouble(Photo::getLongitude).average().orElse(0);
                String thumb = cluster.get(0).getImageUrl();

                result.add(MapResponseDto.builder()
                        .cellLat(avgLat)
                        .cellLng(avgLng)
                        .photoCount(cluster.size())
                        .thumbnailUrl(thumb)
                        .build());
            }

            return result;

        } catch (CustomException e) {
            throw e; // MAP_PARAMS_REQUIRED, JWT_TOKEN_INVALID
        } catch (Exception e) {
            throw new RuntimeException(e); // 500 내부 에러 처리
        }
    }

    public List<PhotoRequestDto> getPhotosInCluster(Long userId, ClusterRequestDto request) {
        try {
            if (userId == null) throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);

            double cellSize = getCellSizeByZoom(request.getZoomLevel());

            double minLat = request.getCellLat();
            double maxLat = minLat + cellSize;

            double minLng = request.getCellLng();
            double maxLng = minLng + cellSize;

            log.info("📍 Zoom Level: {}, Cell Size: {}", request.getZoomLevel(), cellSize);
            log.info("📦 계산된 범위: lat [{} ~ {}], lng [{} ~ {}]", minLat, maxLat, minLng, maxLng);

            // 해당 유저의 사진들 중 셀 범위 안에 있는 것만 필터링
            List<Photo> filtered = photoRepository.findByUser_UserId(userId).stream()
                    .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                    .filter(p -> p.getLatitude() >= minLat && p.getLatitude() <= maxLat)
                    .filter(p -> p.getLongitude() >= minLng && p.getLongitude() <= maxLng)
                    .toList();
            log.info("📸 유저 전체 사진 수: {}", photoRepository.findByUser_UserId(userId).size());
            log.info("🎯 필터링된 사진 수: {}", filtered.size());

            return filtered.stream()
                    .map(photo -> PhotoRequestDto.builder()
                            .imageUrl(photo.getImageUrl())
                            .isPrivate(photo.isPrivate())
                            .latitude(photo.getLatitude())
                            .longitude(photo.getLongitude())
                            .createdAt(photo.getCreatedAt())
                            .fileName(photo.getFileName())
                            .build())
                    .toList();


        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double getCellSizeByZoom(int zoomLevel) {
        if (zoomLevel >= 15) return 0.002;
        if (zoomLevel >= 13) return 0.005;
        if (zoomLevel >= 11) return 0.01;
        if (zoomLevel >= 9) return 0.02;
        if (zoomLevel >= 7) return 0.05;
        return 0.1;
    }
}