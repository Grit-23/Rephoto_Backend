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

    @NotNull(message = "(필수항목) 1. 사용자 ID")
    @Schema(description = "해당 사진의 유저 ID", example = "5", required = true)
    private Long userId;

    @NotNull(message = "(필수항목) 2. 개인정보 포함 여부")
    @Schema(description = "개인정보 포함 여부", example = "true", required = true)
    private Boolean isPrivate;

    @NotNull(message = "(필수항목) 3. 사진 ID")
    @Schema(description = "설명을 등록할 사진", required = true )
    private Long photoId;
}
