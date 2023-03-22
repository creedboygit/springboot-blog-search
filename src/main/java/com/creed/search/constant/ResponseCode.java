package com.creed.search.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(0, "OK"),
    NOT_FOUND(404, "찾을 수 없는 요청입니다."),
    NOT_FOUND_DATA(10001, "요청하신 데이터를 찾을 수 없습니다."),
    REQUIRED_PARAMS_OMITTED(10002, "필수 파라미터가 입력되지 않았습니다."),
    API_CONNECT_FAILED(10003, "API 접속 오류가 발생하였습니다.");

    private final int code;

    private final String message;
}

