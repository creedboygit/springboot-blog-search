package com.creed.search.service;

import com.creed.search.constant.ApiType;
import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlogSearchService {

    private Map<ApiType, BlogSearchClientService> blogSearchClients = new HashMap<>();

    public BlogSearchService(@Qualifier("kakao") BlogSearchClientService kakaoBlogSearchClient,
                             @Qualifier("naver") BlogSearchClientService naverBlogSearchClient) {
        blogSearchClients.put(ApiType.KAKAO, kakaoBlogSearchClient);
        blogSearchClients.put(ApiType.NAVER, naverBlogSearchClient);
    }

    public Page<BlogSearchResponse> search(ApiType apiType, BlogSearchRequest request) {

        BlogSearchClientService blogSearchClient = blogSearchClients.get(apiType);
        return blogSearchClient.search(request);
    }

    public static String connectedFailedMessage(String apiName) {
        return apiName + " API 접속 오류";
    }
}
