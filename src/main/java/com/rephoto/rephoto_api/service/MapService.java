package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.domain.Photo;
import com.rephoto.rephoto_api.dto.MapRequestDto;
import com.rephoto.rephoto_api.dto.MapResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MapService {
    /*

    private final PhotoRepository photoRepository;

    public List<MapResponseDto> getPhotoClusters(MapRequestDto request) {
        try {
            if (request.getUserId() == null ||
                    request.getMinLat() == 0 || request.getMaxLat() == 0 ||
                    request.getMinLng() == 0 || request.getMaxLng() == 0) {
                throw new CustomException(ErrorCode.MAP_PARAMS_REQUIRED);
            }

            List<Photo> photosInBounds = photoRepository.findByUser_UserIdAndLatitudeBetweenAndLongitudeBetween(
                    request.getUserId(),
                    request.getMinLat(), request.getMaxLat(),
                    request.getMinLng(), request.getMaxLng()
            );

            if (photosInBounds.isEmpty()) {
                throw new CustomException(ErrorCode.PHOTO_NOT_FOUND);
            }

            double gridSize = getGridSize(request.getZoomLevel());

            Map<String, List<Photo>> clusterMap = new HashMap<>();
            for (Photo photo : photosInBounds) {
                double lat = photo.getLatitude();
                double lng = photo.getLongitude();

                int latKey = (int) (lat / gridSize);
                int lngKey = (int) (lng / gridSize);
                String key = latKey + "_" + lngKey;

                clusterMap.computeIfAbsent(key, k -> new ArrayList<>()).add(photo);
            }

            List<MapResponseDto> result = new ArrayList<>();
            for (List<Photo> cluster : clusterMap.values()) {
                Photo latest = cluster.stream()
                        .filter(p -> p.getImageUrl() != null)
                        .max(Comparator.comparing(Photo::getCreatedAt))
                        .orElse(null);

                if (latest != null) {
                    double avgLat = cluster.stream().mapToDouble(Photo::getLatitude).average().orElse(0);
                    double avgLng = cluster.stream().mapToDouble(Photo::getLongitude).average().orElse(0);

                    result.add(MapResponseDto.builder()
                            .latitude(avgLat)
                            .longitude(avgLng)
                            .thumbnailUrl(latest.getImageUrl())
                            .photoCount(cluster.size())
                            .build());
                }
            }

            return result;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double getGridSize(int zoomLevel) {
        if (zoomLevel >= 14) return 0.005;
        if (zoomLevel >= 11) return 0.02;
        if (zoomLevel >= 8) return 0.05;
        return 0.1;
    }

     */
}
