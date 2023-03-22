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

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("kakao")
public class KakaoBlogSearchClient implements BlogSearchClientService {

    @Value("${kakao.api.key-prefix}")
    private String keyPrefix;

    @Value("${kakao.api.key}")
    private String key;

    @Value("${kakao.api.baseurl}")
    private String baseurl;

    @Value("${kakao.api.blog-search-path}")
    private String blogSearchPath;

    private final String apiName = ApiType.KAKAO.toString();

    @Override
    public Page<BlogSearchResponse> search(BlogSearchRequest request) {

        if (ObjectUtils.isEmpty(request.getQuery())) {
            return new PageImpl<>(Collections.emptyList());
        }

        SearchResult searchResult = execute(request);

        if (ObjectUtils.isEmpty(searchResult.getMeta())) {

            throw new CustomException(ResponseCode.NOT_FOUND_DATA, "데이터가 검색되지 않았습니다.");
        }

        List<BlogSearchResponse> blogs = searchResult.getDocuments().stream()
                .map(this::toBlogResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(blogs,
                request.pageable(),
                searchResult.getMeta().getPageableCount());
    }

    public SearchResult execute(BlogSearchRequest request) {

        Mono<SearchResult> mono = WebClient.builder()
                .baseUrl(baseurl)
                .build()
                .get()
                .uri(builder -> builder.path(blogSearchPath)
                        .queryParam("query", request.getQuery())
                        .queryParam("sort", request.getSort())
                        .queryParam("page", request.getPage())
                        .queryParam("size", request.getSize())
                        .build()
                )
                .header("Authorization", keyPrefix + " " + key)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse
                        -> Mono.error(new CustomException(ResponseCode.API_CONNECT_FAILED, connectedFailedMessage(apiName))))
                .onStatus(HttpStatus::is5xxServerError, clientResponse
                        -> Mono.error(new CustomException(ResponseCode.API_CONNECT_FAILED, connectedFailedMessage(apiName))))
                .bodyToMono(SearchResult.class);

        return mono.block();
    }

    private BlogSearchResponse toBlogResponse(SearchResult.Document document) {
        return BlogSearchResponse.builder()
                .title(document.getTitle())
                .blogName(document.getBlogName())
                .contents(document.getContents())
                .thumbnail(document.getThumbnail())
                .url(document.getUrl())
                .datetime(document.getDatetime())
                .build();
    }

    @Getter
    private static class SearchResult {

        private List<Document> documents;

        private Meta meta;

        @Getter

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Meta {

            @JsonProperty("is_end")
            private boolean end;

            @JsonProperty("pageable_count")
            private int pageableCount;

            @JsonProperty("total_count")
            private int totalCount;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Document {

            private String title;

            @JsonProperty("blogname")
            private String blogName;

            private String contents;

            private String thumbnail;

            private String url;

            private String datetime;
        }
    }
}
