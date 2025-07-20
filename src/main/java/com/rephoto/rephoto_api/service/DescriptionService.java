package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.repository.DescriptionRepository;
import com.rephoto.rephoto_api.repository.PhotoRepository;
import com.rephoto.rephoto_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final DescriptionRepository repository;
    private final PhotoRepository photoRepository;


}
