package com.rephoto.rephoto_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 회원가입
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 로그인 ID입니다."),
    JOIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 처리 중 서버 오류가 발생했습니다."),

    // 로그인
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로그인 처리 중 서버 오류가 발생했습니다."),
    LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃 처리 중 서버 오류가 발생했습니다."),

    // User
    UNAUTHORIZED_UPDATE_ACCESS(HttpStatus.FORBIDDEN, "다른 사용자의 정보를 수정할 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    UPDATE_INFO_INVALID(HttpStatus.BAD_REQUEST, "비밀번호와 사용자 이름은 필수입니다."),

    // 탈퇴
    UNAUTHORIZED_DELETE(HttpStatus.FORBIDDEN, "다른 사용자의 탈퇴 요청은 불가능합니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회원 탈퇴 처리 중 오류가 발생했습니다."),

    // JWT 관련
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),

    // AWS S3 관련
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "파일이 비어 있습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 500MB를 초과합니다."),
    UNSUPPORTED_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),

    // 지도
    MAP_PARAMS_REQUIRED(HttpStatus.BAD_REQUEST, "위도, 경도 값은 필수입니다."),
    INVALID_COORDINATE_FORMAT(HttpStatus.BAD_REQUEST, "올바른 좌표 형식을 입력해주세요."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),

    // Kakao 관련 오류
    KAKAO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "카카오 인증에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
