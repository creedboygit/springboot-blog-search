package com.creed.search.service;

import com.creed.search.constant.ApiType;
import com.creed.search.domain.SearchHistory;

public interface SearchHistoryService {

    SearchHistory addSearchHistory(String searchKeyword, ApiType apiType);
}
