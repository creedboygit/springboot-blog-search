package com.creed.search.domain;

import com.creed.search.constant.ApiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_BLOG_SEARCH_HISTORY", indexes = @Index(name = "i_search_keyword", columnList = "search_keyword"))
public class SearchHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "search_keyword", nullable = false)
    private String searchKeyword;

    @Column(name = "api_type")
    private String apiType;

    public SearchHistory(String searchKeyword, ApiType apiType) {
        this.searchKeyword = searchKeyword;
        this.apiType = apiType.toString();
    }
}
