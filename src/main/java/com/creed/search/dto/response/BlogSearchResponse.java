package com.creed.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Schema(title = "BlogSearchResponse", description = "블로그 검색 Response DTO")
@Getter
@Builder
public class BlogSearchResponse implements Serializable {

    @Schema(title = "title", description = "문서 제목", example = "부동산 시세를 알려드립니다.")
    private String title;

    @Schema(title = "blogName", description = "블로그명", example = "부동산시세 마스터")
    private String blogName;

    @Schema(title = "contents", description = "문서 본문 중 일부", example = "부동산시세란 말입니다.")
    private String contents;

    @Schema(title = "thumbnail", description = "문서 썸네일", example = "https://search2.kakaocdn.net/argon/130x130_85_c/97zRm6RyNES")
    private String thumbnail;

    @Schema(title = "url", description = "문서 URL", example = "https://series.tistory.com")
    private String url;

    @Schema(title = "datetime", description = "문서 작성 시간", example = "2023-03-01T21:59:17.000+09:00")
    private String datetime;
}
