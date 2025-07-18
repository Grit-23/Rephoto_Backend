package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.dto.KakaoLoginRequestDto;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//https://kauth.kakao.com/oauth/authorize?client_id=a7991882d9688bd3f0fb4a14d11f1044&redirect_uri=http://localhost:8080/callback&response_type=code
@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
@Tag(name = "카카오 로그인 API", description = "카카오 계정을 통한 로그인 처리")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰으로 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "카카오 인증 실패 (유효하지 않은 로그인 ID 또는 비밀번호)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (로그인 처리 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequestDto request) {
        try {
            LoginResponseDto response = kakaoLoginService.kakaoLogin(request);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "알 수 없는 오류로 로그인에 실패했습니다."));
        }
    }
}
