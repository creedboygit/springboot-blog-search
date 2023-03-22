package com.creed.search.common.advice;

import com.creed.search.common.exception.CustomException;
import com.creed.search.common.response.CustomResponse;
import com.creed.search.constant.ResponseCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintDeclarationException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    // custom
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> customException(CustomException e) {
        log.error("CustomException -> {}", e.getMessage());
        if (Objects.isNull(e.getData())) {
            return response(e.getCode(), e.getMessage(), e.getData());
        }
        return response(e.getCode(), e.getMessage(), e.getData());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomResponse> BadRequestException(Exception e) {
        log.error("BadRequestException -> {}", e.getMessage());
        return response(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<CustomResponse> FileUploadException(FileUploadException e) {
        log.error("FileUploadException -> {}", e.getMessage());
        return response(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CustomResponse> invalidParams(InvalidParameterException e) {
        log.error("InvalidParameterException -> {}", e.getMessage());
        return response(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonParseException.class, JsonMappingException.class})
    public ResponseEntity<CustomResponse> httpMessageNotReadable(HttpServletRequest req, HttpMessageNotReadableException e) {

        String msg = "";
        Throwable throwable = e.getMostSpecificCause();

        if (throwable instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) throwable;
            if (ife.getTargetType().isEnum()) {
                msg = ((InvalidFormatException) throwable).getPath().get(0).getFieldName() + " is possible " + Arrays.toString(ife.getTargetType().getEnumConstants());
            } else {
                msg = "Please check the error type of reverse serialization.";
            }
        } else {
            msg = "Please check your json grammar.";
        }
        log.error("exception -> {}", msg);
        return response(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> SqlException(Exception e) {
        log.error("exception -> {}", e.getMessage());
        e.printStackTrace();
        return response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CustomResponse> HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException -> " + e.getMessage());
        return response(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomResponse> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException -> {}", e.getMessage());
        return response(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<CustomResponse> ConstraintDeclarationException(ConstraintDeclarationException cde) {
        log.error("ConstraintDeclarationException");
        return response(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<CustomResponse> EmptyResultDataAccessException() {
        log.error("EmptyResultDataAccessException");
        return response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPropertyException.class)
    public ResponseEntity<CustomResponse> InvalidPropertyException(InvalidPropertyException ipe) {
        log.error("InvalidPropertyException -> " + ipe.getMessage());
        return response(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomResponse> NoHandlerFoundException() {
        log.error("NoHandlerFoundException");
        return response(HttpStatus.NOT_FOUND, ResponseCode.NOT_FOUND.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<CustomResponse> MissingServletRequestPartException(MissingServletRequestPartException exception) {
        log.error("MissingServletRequestPartException -> " + exception.getMessage());
        return response(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponse> MissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("MissingServletRequestParameterException -> " + exception.getMessage());
        return response(HttpStatus.BAD_REQUEST);
    }

    // response
    private ResponseEntity<CustomResponse> response(final int status, final String message) {
        return ResponseEntity.ok(new CustomResponse<ResponseCode>(status, message, null));
    }

    private ResponseEntity<CustomResponse> response(final int status, final String message, final Map<String, String> data) {
        return ResponseEntity.ok(new CustomResponse<>(status, message, data));
    }

    private ResponseEntity<CustomResponse> response(final HttpStatus status) {
        return response(status.value(), status.getReasonPhrase());
    }

    private ResponseEntity<CustomResponse> response(final HttpStatus status, String message) {
        return ResponseEntity.ok(new CustomResponse<ResponseCode>(status.value(), message, null));
    }
}