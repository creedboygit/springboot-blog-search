package com.creed.search.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApiType {

    KAKAO("kakao", "카카오"),
    NAVER("naver", "네이버");

    private String code;
    private String name;

    ApiType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ApiType code(String code) {
        switch (code) {
            case "kakao":
                return KAKAO;
            case "naver":
                return NAVER;
            default:
                return KAKAO;
        }
    }

    @Override
    public String toString() {
        return code;
    }
}

