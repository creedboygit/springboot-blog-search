package com.creed.search.service.impl;

import com.creed.search.common.exception.CustomException;
import com.creed.search.constant.ApiType;
import com.creed.search.constant.ResponseCode;
import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import com.creed.search.dto.response.SearchHistoryResponse;
import com.creed.search.repository.SearchHistoryRepository;
import com.creed.search.service.BlogSearchService;
import com.creed.search.service.BlogService;
import com.creed.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogSearchService searchService;

    private final SearchHistoryService searchHistoryService;

    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<BlogSearchResponse> getBlogsByKeyword(ApiType apiType, BlogSearchRequest request) {

        if (ObjectUtils.isEmpty(request.getQuery())) {
            throw new CustomException(ResponseCode.REQUIRED_PARAMS_OMITTED, "필수 파라미터 누락 : query(검색어)");
        }

        searchHistoryService.addSearchHistory(request.getQuery(), apiType);

        return searchService.search(apiType, request);
    }

    @Override
    public List<SearchHistoryResponse> findSearchHistoryGroupByKeyword() {

        return searchHistoryRepository.findSearchHistoryGroupByKeyword();
    }
}