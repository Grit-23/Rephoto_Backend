package com.rephoto.rephoto_api.controller;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.dto.UserInfoResponseDto;
import com.rephoto.rephoto_api.dto.UserUpdateRequestDto;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import com.rephoto.rephoto_api.exception.ErrorResponse;
import com.rephoto.rephoto_api.repository.UserRepository;
import com.rephoto.rephoto_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "회원 API", description = "회원 정보 조회, 정보 수정, 탈퇴 기능")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    // ---------- 회원 정보 조회 ----------
    @GetMapping
    @Operation(summary = "회원 정보 조회", description = "JWT 토큰으로 회원 인증 후 정보 조회 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "접근 권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<UserInfoResponseDto> getUserInfo(
            @AuthenticationPrincipal User currentUser
    ) {
        User user = userService.getUserInfo(currentUser);
        return ResponseEntity.ok(UserInfoResponseDto.from(user));
    }


    // ---------- 회원 탈퇴 ----------
    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "JWT 토큰으로 회원 인증 후 탈퇴 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 접근(본인이 아닌 계정 접근)", content = @Content),
            @ApiResponse(responseCode = "404", description = "회원 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "회원 탈퇴 실패(서버 오류)", content = @Content)
    })
    public ResponseEntity<Map<String, String>> deleteUser(
            @AuthenticationPrincipal User currentUser,
            @RequestBody(required = false) Map<String, String> requestBody
    ) {
        String password = (requestBody != null) ? requestBody.get("password") : null;
        userService.deleteUser(currentUser, password);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
    }

    // ---------- 회원 정보 수정 ----------
    @PutMapping("/{userId}")
    @Operation(summary = "회원 정보 수정", description = "userId로 회원 정보를 수정. 본인 계정만 수정 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "비밀번호와 사용자 이름은 필수입니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "다른 사용자의 정보를 수정할 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "500", description = "회원 정보 수정 중 서버 오류", content = @Content)
    })
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto requestDto,
            @AuthenticationPrincipal User currentUser
    ) {
        userService.updateUser(userId, currentUser, requestDto);
        return ResponseEntity.ok(Map.of("message", "회원 정보가 성공적으로 수정되었습니다."));
    }
}
