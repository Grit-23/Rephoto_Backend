package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.MapPhotoResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos/map")
@RequiredArgsConstructor
public class MapController {

    private final PhotoService photoService;

    @GetMapping
    public ResponseEntity<MapPhotoResponseDto> getPhotosByLocation(
            //@RequestParam(required = true) Double lat,
            //@RequestParam(required = true) Double lng,
            //@RequestParam(required = true) Integer radius
    ){
        /*
        if (lat == null || lng == null || radius == null) {
            throw new CustomException(ErrorCode.MAP_PARAMS_REQUIRED);
        }

        MapPhotoResponseDto response = photoService.getPhotosByLocation(lat, lng, radius);
        return ResponseEntity.ok(response);

         */
        return null;
    }
}
