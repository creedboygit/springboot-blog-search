package com.creed.search.common.exception;

import com.creed.search.constant.ResponseCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {

    private int code;
    private String systemMessage;
    private Map<String, String> data;

    public CustomException(ResponseCode errorCode, String msg) {
        super(msg);
        this.code = errorCode.getCode();
        this.systemMessage = errorCode.getMessage();
    }
}
