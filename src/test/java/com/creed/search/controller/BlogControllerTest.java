package com.creed.search.controller;

import com.creed.search.constant.ApiType;
import com.creed.search.domain.SearchHistory;
import com.creed.search.repository.SearchHistoryRepository;
import com.creed.search.service.SearchHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName("검색테스트")
@AutoConfigureMockMvc
class BlogControllerTest {

    @Resource
    MockMvc mockMvc;

    @Resource
    ObjectMapper objectMapper;

    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    @Autowired
    SearchHistoryService searchHistoryService;

    @Test
    @DisplayName("검색 히스토리 서비스 성공")
    void searchHistoryServiceTests_success() throws Exception {

        // given
        SearchHistory search = searchHistoryService.addSearchHistory("부동산시세", ApiType.KAKAO);
        String searchKeyword = search.getSearchKeyword();
        String apiType = search.getApiType();

        // when
        List<SearchHistory> savedHistory = searchHistoryRepository.findBySearchKeyword("부동산시세");

        // then
        for (SearchHistory history : savedHistory) {
            assertThat(history.getId()).isNotNull();
            assertThat(history.getApiType()).isEqualTo(apiType);
            assertThat(history.getSearchKeyword()).isEqualTo(searchKeyword);
        }
    }

    @Test
    @DisplayName("검색 히스토리 저장 성공")
    void searchHistoryReponsitoryTests_success() throws Exception {

        // given
        SearchHistory searchHistory = SearchHistory
                .builder()
                .apiType(ApiType.KAKAO.toString())
                .searchKeyword("부동산시세")
                .build();

        // when
        SearchHistory savedHistory = searchHistoryRepository.save(searchHistory);

        // then
        assertThat(savedHistory.getId()).isNotNull();
        assertThat(savedHistory.getApiType()).isEqualTo(ApiType.KAKAO.toString());
        assertThat(savedHistory.getSearchKeyword()).isEqualTo("부동산시세");
    }

    @Test
    @DisplayName("검색 히스토리 저장 실패")
    @Transactional
    void searchHistoryReponsitoryTests_fail() throws Exception {

        // given
        SearchHistory searchHistory = SearchHistory
                .builder()
                .apiType(ApiType.KAKAO.toString())
                .searchKeyword("부동산시세")
                .build();

        // when
        SearchHistory history = searchHistoryRepository.save(searchHistory);

        // then
        assertThat(history.getId()).isNotNull();
        assertThat(history.getApiType()).isNotEqualTo("카카오");
        assertThat(history.getSearchKeyword()).isNotEqualTo("자동차");
    }

    @Test
    @DisplayName("카카오 블로그 검색 성공")
    void kakaoBlogSearch_success() throws Exception {

        // given
        String url = "/api/search/kakao";

        // when
        ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("query", "부동산가격")
                        .param("sort", "accuracy")
                        .param("page", "1")
                        .param("size", "20")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.content[0].blogName", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("네이버 블로그 검색 성공")
    void naverBlogSearch_success() throws Exception {

        // given
        String url = "/api/search/naver";

        // when
        ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("query", "부동산가격")
                        .param("sort", "accuracy")
                        .param("page", "1")
                        .param("size", "20")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.content[0].blogName", notNullValue()))
                .andDo(print());
    }
}