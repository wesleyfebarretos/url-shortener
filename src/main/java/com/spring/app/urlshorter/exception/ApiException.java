package com.spring.app.urlshorter.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private HttpStatus statusCode;

    public ApiException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public static ApiException unaunthorizedException(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message);
    }

    public static ApiException internalServerException(String message) {
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ApiException badRequestException(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
