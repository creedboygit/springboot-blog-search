package com.creed.search.service;

import com.creed.search.constant.ApiType;
import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import com.creed.search.dto.response.SearchHistoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {

    Page<BlogSearchResponse> getBlogsByKeyword(ApiType apiType, BlogSearchRequest request);

    List<SearchHistoryResponse> findSearchHistoryGroupByKeyword();
}
