package com.spring.app.urlshorter.filter;

import com.spring.app.urlshorter.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionFilterHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Map<String, String>> handle(ApiException error) {
        return new ResponseEntity<>(Map.of(
                "code", String.valueOf(error.getStatusCode().value()),
                "msg", error.getMessage()
        ), error.getStatusCode());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException error) {
        return new ResponseEntity<>(Map.of(
                "code", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "msg", error.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
