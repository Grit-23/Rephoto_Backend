package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.LoginRequestDto;
import com.rephoto.rephoto_api.dto.LoginResponseDto;
import com.rephoto.rephoto_api.jwt.JwtUtil;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = loginService.login(request);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
