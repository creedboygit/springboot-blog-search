package com.creed.search.service.client;

import com.creed.search.common.exception.CustomException;
import com.creed.search.constant.ApiType;
import com.creed.search.constant.ResponseCode;
import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import com.creed.search.service.BlogSearchClientService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.creed.search.service.BlogSearchService.connectedFailedMessage;

@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier("naver")
public class NaverBlogSearchClient implements BlogSearchClientService {

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    @Value("${naver.api.baseurl}")
    private String baseurl;

    @Value("${naver.api.blog-search-path}")
    private String blogSearchPath;

    private final String apiName = ApiType.KAKAO.toString();

    @Override
    public Page<BlogSearchResponse> search(BlogSearchRequest request) {

        if (ObjectUtils.isEmpty(request.getQuery())) {
            return new PageImpl<>(Collections.emptyList());
        }

        SearchResult searchResult = execute(request);

        if (ObjectUtils.isEmpty(searchResult.getTotal())) {
            throw new CustomException(ResponseCode.NOT_FOUND_DATA, "데이터가 검색되지 않았습니다.");
        }

        List<BlogSearchResponse> blogs = searchResult.getItems().stream()
                .map(this::toBlogResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(blogs,
                request.pageable(),
                searchResult.getTotal());
    }

    public SearchResult execute(BlogSearchRequest request) {

        String toSort = getSort(request.getSort());

        Mono<SearchResult> mono = WebClient.builder()
                .baseUrl(baseurl)
                .build()
                .get()
                .uri(builder -> builder.path(blogSearchPath)
                        .queryParam("query", request.getQuery())
                        .queryParam("sort", toSort)
                        .queryParam("start", request.getPage())
                        .queryParam("display", request.getSize())
                        .build()
                )
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse
                        -> Mono.error(new CustomException(ResponseCode.API_CONNECT_FAILED, connectedFailedMessage(apiName))))
                .onStatus(HttpStatus::is5xxServerError, clientResponse
                        -> Mono.error(new CustomException(ResponseCode.API_CONNECT_FAILED, connectedFailedMessage(apiName))))
                .bodyToMono(SearchResult.class);

        return mono.block();
    }

    private static String getSort(String sort) {

        if (ObjectUtils.isEmpty(sort))
            return "sim";

        switch (sort) {
            case "accuracy":
                return "sim";
            case "recency":
                return "date";
            default:
                return "sim";
        }
    }

    private BlogSearchResponse toBlogResponse(SearchResult.Item item) {
        return BlogSearchResponse.builder()
                .title(item.getTitle())
                .blogName(item.getBlogName())
                .contents(item.getContents())
                .thumbnail("")
                .url(item.getUrl())
                .datetime(item.getDatetime())
                .build();
    }

    @Getter
    private static class SearchResult {

        private Integer total;

        private Integer start;

        private Integer display;

        private List<Item> items;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Item {

            private String title;

            @JsonProperty("link")
            private String url;

            @JsonProperty("description")
            private String contents;

            @JsonProperty("bloggername")
            private String blogName;

            @JsonProperty("bloggerlink")
            private String bloggerLink;

            @JsonProperty("postdate")
            private String datetime;
        }
    }
}
