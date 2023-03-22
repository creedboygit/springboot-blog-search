package com.creed.search.common.util.converter;

import com.creed.search.constant.ApiType;
import org.springframework.core.convert.converter.Converter;

public class ApiTypeConverter implements Converter<String, ApiType> {

    public ApiType convert(String value) {
        return ApiType.code(value);
    }
}
