package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logout")
@Tag(name = "로그아웃 API", description = "시용자 로그아웃 기능 수행")
public class LogoutController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그아웃", description = "로그아웃 수행. RefreshToken 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "이미 로그아웃된 사용자", content = @Content),
            @ApiResponse(responseCode = "401", description = "잘못된 JWT 토큰 사용", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (로그아웃 처리 실패)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearer) {
        String refreshToken = jwtUtil.resolveToken(bearer);
        loginService.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }
}
