package com.rephoto.rephoto_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 회원가입
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 로그인 ID입니다."),

    // 로그인, 로그아웃
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST, "이미 로그아웃된 사용자입니다."),
    UNAUTHORIZED_USER_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // User
    UNAUTHORIZED_UPDATE_ACCESS(HttpStatus.FORBIDDEN, "다른 사용자의 정보를 수정할 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    UPDATE_INFO_INVALID(HttpStatus.BAD_REQUEST, "비밀번호와 사용자 이름은 필수입니다."),
    REAUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),

    // 탈퇴
    UNAUTHORIZED_DELETE(HttpStatus.FORBIDDEN, "다른 사용자의 탈퇴 요청은 불가능합니다."),

    // JWT 관련
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),

    // AWS S3 관련
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "파일이 비어 있습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 500MB를 초과합니다."),
    UNSUPPORTED_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),

    // 지도
    MAP_PARAMS_REQUIRED(HttpStatus.BAD_REQUEST, "지도 요청 파라미터가 누락되었습니다."),
    INVALID_COORDINATE_FORMAT(HttpStatus.BAD_REQUEST, "좌표 형식이 올바르지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),

    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진을 찾을 수 없습니다."),

    // 설명
    DESCRIPTION_PARAMS_REQUIRED(HttpStatus.BAD_REQUEST, "파라미터가 누락되었습니다.(사진 ID, 설명 내용)"),
    DESCRIPTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 사진에 대한 설명이 존재합니다."),
    DESCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진에 설명 테이블이 연동되지 않았습니다."),

    UNAUTHORIZED_DESCRIPTION_ACCESS(HttpStatus.FORBIDDEN,"해당 사진에 접근할 권한이 없습니다."),

    // Kakao 관련 오류
    KAKAO_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "카카오 인증에 실패했습니다."),

    // 검색
    SEARCH_QUERY_REQUIRED(HttpStatus.BAD_REQUEST, "검색어가 비어있습니다."),
    TOO_MANY_TAGS(HttpStatus.BAD_REQUEST, "태그는 최대 20개까지 입력할 수 있습니다."),
    SEARCH_TAGS_REQUIRED(HttpStatus.BAD_REQUEST, "유효한 태그가 없습니다. '#'으로 시작하는 태그를 입력해주세요."),
    VECTOR_DIMENSION_MISMATCH(HttpStatus.BAD_REQUEST, "벡터 차원이 다릅니다."),
    
    //태그 관련
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 태그가 존재하지 않습니다"),
    TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "이 사진에 해당 태그가 이미 존재합니다"),

    // AI 관련
    AI_VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "AI 캡션 생성 요청 값이 유효하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
