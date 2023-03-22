package com.creed.search.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Schema(description = "블로그 검색 Request DTO")
@ParameterObject
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BlogSearchRequest implements Serializable {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MIN_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE = 50;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MIN_SIZE = 1;
    private static final int DEFAULT_MAX_SIZE = 50;

    @NotBlank
    @Schema(name = "query", description = "검색어", example = "부동산시장")
    private String query;

    @Schema(name = "sort", title = "정렬 조건", type = "String", description = "정렬 조건<br>accuracy : 정확도순, recency : 최신순", defaultValue = "accuracy", allowableValues = {"accuracy", "recency"})
    private String sort;

    @Schema(name = "page", description = "검색할 페이지", example = "1")
    private int page;

    @Schema(name = "size", description = "한 페이지에 보여질 문서 수", example = "50")
    private int size;

    public BlogSearchRequest() {
        this.page = DEFAULT_PAGE;
        this.size = DEFAULT_SIZE;
    }

    public void setPage(int page) {
        this.page = page < DEFAULT_MIN_PAGE || page > DEFAULT_MAX_PAGE ? DEFAULT_PAGE : page;
    }

    public void setSize(int size) {
        this.size = size < DEFAULT_MIN_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public Pageable pageable() {
        return PageRequest.of(page - 1, size);
    }
}
