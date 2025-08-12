package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.dto.CaptionResponse;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${ai.server.url}")
    private String aiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public CaptionResponse generateCaption(MultipartFile file) {
        try {
            // 파일 ByteArrayResource로 변환
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename(); // 원래 파일명 유지
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            // AI 서버 호출
            String url = aiUrl + "/api/ai/caption/generate";
            ResponseEntity<CaptionResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    CaptionResponse.class
            );
            return response.getBody();

        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("AI 서버 422 응답: {}", e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.AI_VALIDATION_ERROR);
        } catch (Exception e) {
            log.error("AI caption 생성 실패", e);
            throw new RuntimeException("AI caption 생성 중 오류 발생", e);
        }
    }
}
