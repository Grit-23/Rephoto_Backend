package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.JoinRequestDto;
import com.rephoto.rephoto_api.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/join")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping
    public ResponseEntity<String> join(@RequestBody @Valid JoinRequestDto requestDto) {
        joinService.join(requestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
