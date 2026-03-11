package com.api.endpoints;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponse.class);

    String status;
    String message;
    T result;

    public ApiResponse() {
    }

    public ApiResponse(String status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<List<T>> success(String request, List<T> result) {
        logger.info(request);
        return new ApiResponse<List<T>>("success", request, result);
    }

    public static <T> ApiResponse<List<T>> success(String request, T result) {
        List<T> list = Arrays.asList(result);
        return success(request, list);
    }

    public static <T> ApiResponse<List<T>> success(String request) {
        return success(request, Collections.emptyList());
    }

    public static <T> ApiResponse<List<T>> error(String request, String error) {
        var status = "error - " + error;
        logger.error(request);
        logger.error(status);
        return new ApiResponse<List<T>>(status, request, Collections.emptyList());
    }

    public static <T> ApiResponse<List<T>> error(String request, Exception e) {
        var status = "error - " + e.toString();
        logger.error(request);
        logger.error(status);
        return new ApiResponse<List<T>>(status, request, Collections.emptyList());
    }
}
