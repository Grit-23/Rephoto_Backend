package com.rephoto.rephoto_api.service;

import com.rephoto.rephoto_api.dto.EmbeddingRequest;
import com.rephoto.rephoto_api.dto.EmbeddingResponse;
import com.rephoto.rephoto_api.dto.ImageCaptionResponse;
import com.rephoto.rephoto_api.exception.CustomException;
import com.rephoto.rephoto_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${ai.server.url}")
    private String aiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    //URL 다운로드 타임아웃
    private RestTemplate downloadRestTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(15000);
        return new RestTemplate(factory);
    }

    // S3 URL로 바이트로 사진 파일 다운로드
    private byte[] fetchBytesFromUrl(String imageUrl) {
        try {
            ResponseEntity<byte[]> resp = downloadRestTemplate().getForEntity(imageUrl, byte[].class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null || resp.getBody().length == 0) {
                throw new RuntimeException("이미지 다운로드 실패 또는 빈 바이트");
            }
            return resp.getBody();
        } catch (Exception e) {
            log.error("이미지 URL 다운로드 실패: {}", imageUrl, e);
            throw new RuntimeException("이미지 URL 다운로드 실패", e);
        }
    }

    // 이미지 url로 전달
    public ImageCaptionResponse generateCaptionFromUrl(String imageUrl) {
        try {
            byte[] bytes = fetchBytesFromUrl(imageUrl);

            // ByteArrayResource로 멀티파트 구성
            ByteArrayResource fileResource = new ByteArrayResource(bytes) {
                @Override public String getFilename() { return "image_from_url.jpg"; } // 파일명 임의 지정
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String captionUrl = aiUrl + "/api/ai/caption/generate";
            ResponseEntity<ImageCaptionResponse> response = restTemplate.exchange(
                    captionUrl, HttpMethod.POST, requestEntity, ImageCaptionResponse.class
            );

            ImageCaptionResponse captionResponse = response.getBody();

            // 설명이 생성되면 임베딩 호출
            if (captionResponse != null && captionResponse.getExplanation() != null) {
                EmbeddingResponse embeddingResponse = generateEmbedding(captionResponse.getExplanation());
                captionResponse.setExplanation_embedding(embeddingResponse.getEmbedding());
            }

            return captionResponse;
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("AI 서버 422 응답 (URL 캡션): {}", e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.AI_VALIDATION_ERROR);
        } catch (Exception e) {
            log.error("AI caption(URL) 생성 실패", e);
            throw new RuntimeException("AI caption(URL) 생성 중 오류 발생", e);
        }
    }

    // 파일로 전달
    public ImageCaptionResponse generateCaption(MultipartFile file) {
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
            ResponseEntity<ImageCaptionResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ImageCaptionResponse.class
            );

            ImageCaptionResponse captionResponse = response.getBody();

            //임베딩 API 호출
            if (captionResponse != null && captionResponse.getExplanation() != null) {
                EmbeddingResponse embeddingResponse = generateEmbedding(captionResponse.getExplanation());
                captionResponse.setExplanation_embedding(embeddingResponse.getEmbedding());
            }

            return response.getBody();

        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("AI 서버 422 응답: {}", e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.AI_VALIDATION_ERROR);
        } catch (Exception e) {
            log.error("AI caption 생성 실패", e);
            throw new RuntimeException("AI caption 생성 중 오류 발생", e);
        }
    }

    public EmbeddingResponse generateEmbedding(String text) {
        try {
            EmbeddingRequest embeddingRequest = new EmbeddingRequest(text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmbeddingRequest> requestEntity = new HttpEntity<>(embeddingRequest, headers);

            String url = aiUrl + "/api/ai/embedding/embed-text";
            ResponseEntity<EmbeddingResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    EmbeddingResponse.class
            );

            return response.getBody();
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("AI 서버 422 응답 (임베딩): {}", e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.AI_VALIDATION_ERROR);
        } catch (Exception e) {
            log.error("AI 임베딩 생성 실패", e);
            throw new RuntimeException("AI 임베딩 생성 중 오류 발생", e);
        }
    }
}
