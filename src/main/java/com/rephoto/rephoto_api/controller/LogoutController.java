package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "로그아웃 API", description = "로그아웃 관련 기능")
public class LogoutController {

    private final LoginService loginService;

    @Operation(summary = "로그아웃", description = "클라이언트 측에서 JWT 토큰 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (로그아웃 처리 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 클라이언트 측에서 토큰 삭제
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다.(클라이언트에서 토큰 삭제 필요)"));
    }
}
