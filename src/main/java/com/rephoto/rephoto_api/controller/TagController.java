package com.rephoto.rephoto_api.controller;


import com.rephoto.rephoto_api.dto.TagResponseDto;
import com.rephoto.rephoto_api.repository.TagRepository;
import com.rephoto.rephoto_api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagRepository tagRepository;

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId) {

        return ResponseEntity.ok("삭제 완료");
    }

    @PutMapping("/{tagId}")
    public TagResponseDto updateTag(@PathVariable Long tagId) {

    }

    @PostMapping("")
    public TagResponseDto saveTag(@PathVariable Long tagId) {

    }
}
