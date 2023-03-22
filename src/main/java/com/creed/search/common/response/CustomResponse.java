package com.creed.search.common.response;

import lombok.Getter;

@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public CustomResponse(int status, String message, T data) {
        this.code = status;
        this.message = message;
        this.data = data;
    }
}
