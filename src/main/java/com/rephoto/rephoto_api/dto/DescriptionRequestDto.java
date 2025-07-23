package com.rephoto.rephoto_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사진 설명 생성 요청 DTO")
public class DescriptionRequestDto {

    @NotNull(message = "사진 ID")
    @Schema(description = "설명을 등록할 사진", example = "3", required = true )
    private Long photoId;
}
