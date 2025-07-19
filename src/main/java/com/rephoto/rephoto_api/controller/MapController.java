package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos/map")
@RequiredArgsConstructor
public class MapController {

    private final PhotoService photoService;

}
