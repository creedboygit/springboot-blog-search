package com.creed.search.repository;

import com.creed.search.domain.SearchHistory;
import com.creed.search.dto.response.SearchHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findBySearchKeyword(String searchKeyword);

    @Query(value = "select search_keyword as keyword, count(search_keyword) as count from TB_BLOG_SEARCH_HISTORY group by search_keyword order by count desc limit 10", nativeQuery = true)
    List<SearchHistoryResponse> findSearchHistoryGroupByKeyword();
}
