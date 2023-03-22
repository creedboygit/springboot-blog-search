package com.creed.search.service;

import com.creed.search.dto.request.BlogSearchRequest;
import com.creed.search.dto.response.BlogSearchResponse;
import org.springframework.data.domain.Page;

public interface BlogSearchClientService {

    Page<BlogSearchResponse> search(BlogSearchRequest request);
}
