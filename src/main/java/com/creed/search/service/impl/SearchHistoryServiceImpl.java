package com.creed.search.service.impl;

import com.creed.search.constant.ApiType;
import com.creed.search.domain.SearchHistory;
import com.creed.search.repository.SearchHistoryRepository;
import com.creed.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Override
    public SearchHistory addSearchHistory(String searchKeyword, ApiType apiType) {

        SearchHistory searchHistory = new SearchHistory(searchKeyword, apiType);

        return searchHistoryRepository.save(searchHistory);
    }
}
