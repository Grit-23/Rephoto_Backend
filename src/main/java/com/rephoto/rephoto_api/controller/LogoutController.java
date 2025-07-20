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

    @Operation(summary = "로그아웃", description = "서버에서 로그인 상태를 해제 (클라이언트 측에서 JWT 토큰 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "이미 로그아웃된 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "잘못된 사용자 접근",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (로그아웃 처리 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{userId}")

    public ResponseEntity<?> logout(@PathVariable Long userId,
                                    @RequestHeader("Authorization") String token) {

        String extractedToken = jwtUtil.resolveToken(token);

        // 1. 토큰 일치 여부 확인
        jwtUtil.validateToken(extractedToken);

        // 2. 토큰에서  사용자 ID 추출
        String loginId = jwtUtil.getLoginIdFromToken(extractedToken);

        // 3. 로그아웃 처리
        loginService.logout(userId, loginId);
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다. (클라이언트에서 토큰 삭제 필요)"));
    }
}
