package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LogoutController {

    private final LoginService loginService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 클라이언트 측에서 토큰 삭제
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다.(클라이언트에서 토큰을 삭제 필요)"));
    }
}
