package com.creed.search.controller;

import com.creed.search.constant.ApiType;
import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import com.creed.search.dto.response.SearchHistoryResponse;
import com.creed.search.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "블로그 검색 API", description = "카카오/네이버 블로그 검색")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BlogSearchResponse.class)))
    @Tag(name = "01. 블로그 검색 API")
    @GetMapping(value = "/search/{apiType}")
    public ResponseEntity<Page<BlogSearchResponse>> searchBlog(
            @PathVariable(name = "apiType")
            @Schema(title = "검색 API 선택", description = "검색 API 선택<br>kakao : 카카오, naver : 네이버", type = "ApiType", implementation = ApiType.class, requiredMode = Schema.RequiredMode.REQUIRED)
            ApiType apiType, BlogSearchRequest request) {

        Page<BlogSearchResponse> list = blogService.getBlogsByKeyword(apiType, request);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "인기 검색어 조회 API", description = "인기 검색어 키워드 10개<br><font color='#ff0000'>* 블로그 검색 API로 먼저 검색하신 후에 인기 검색어가 조회됩니다.</font>")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SearchHistoryResponse.class)))
    @Tag(name = "02. 인기 검색어 조회 API")
    @GetMapping("/history")
    public ResponseEntity<List<SearchHistoryResponse>> getSearchHistoryByKeyword() {

        List<SearchHistoryResponse> list = blogService.findSearchHistoryGroupByKeyword();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
