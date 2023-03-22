package com.creed.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "SearchHistoryResponse", description = "블로그 검색 Response DTO")
public interface SearchHistoryResponse {

    @Schema(name = "keyword", description = "검색한 키워드", example = "부동산시장")
    String getKeyword();

    @Schema(name = "count", description = "검색한 횟수", example = "273")
    int getCount();
}
