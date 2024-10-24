package com.spring.app.urlshorter.filter;

import com.spring.app.urlshorter.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionFilterHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Map<String, String>> handle(ApiException error) {
        log.error("application log ->", error);

        return new ResponseEntity<>(Map.of(
                "code", String.valueOf(error.getStatusCode().value()),
                "msg", error.getMessage()
        ), error.getStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception error) {
        return new ResponseEntity<>(Map.of(
                "code", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "msg", error.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
