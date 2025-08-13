package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "토큰 재발급 API")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "Access Token 만료 여부 미리 체크 후 만료시 api 호출하여 토큰 재발급")
    public ResponseEntity<LoginResponseDto> refresh(@RequestHeader("Authorization") String bearer) {
        String refreshToken = jwtUtil.resolveToken(bearer);
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
