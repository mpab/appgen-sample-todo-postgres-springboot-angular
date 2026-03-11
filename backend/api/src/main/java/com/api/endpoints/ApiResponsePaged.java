package com.api.endpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponsePaged<T> {
    private static final Logger logger = LoggerFactory.getLogger(ApiResponsePaged.class);

    // getters/setters needed for plumbing
    @Getter
    @Setter
    public class Version {
        public String shape;
        public int major;
        public int minor;
        public int revision;

        public Version() {
            shape = "30125261-A5CA-48E4-997E-3935C07BED5D";
            major = 0;
            minor = 0;
            revision = 1;
        }
    }

    @Getter
    @Setter
    public class Links {
        public String self;
        public String next;
        public String prev;
        public String first;
        public String last;
    }

    @Getter
    @Setter
    public class Paging {
        public Long entity_count;
        public Integer page_size;
        public Long prev_cursor;
        public Long next_cursor;
        public Long first_cursor;
        public Long last_cursor;
    }

    // getters/setters needed for plumbing
    @Getter
    @Setter
    public class Result {
        public Version version;
        public T entities;
        public Object references;
        public Paging _paging;
        public Links _links;
        public List<String> entity_fields;

        Result(T entities) {
            this.version = new Version();
            this.entities = entities;
            this.entity_fields = new ArrayList<>();
            this._links = new Links();
            this._paging = new Paging();
        }
    }

    public String status;
    public String message;
    public Result result;

    public ApiResponsePaged(String status, String message, T entities) {
        this.status = status;
        this.message = message;
        this.result = new Result(entities);
    }

    public static <T> ApiResponsePaged<List<T>> success(String request, List<T> result) {
        logger.info(request);
        return new ApiResponsePaged<List<T>>("success", request, result);
    }

    public static <T> ApiResponsePaged<List<T>> success(String request) {
        return success(request, Collections.emptyList());
    }

    public static <T> ApiResponsePaged<List<T>> error(String request, Exception e) {
        var status = "error - " + e.toString();
        logger.error(request);
        logger.error(status);
        return new ApiResponsePaged<List<T>>(status, request, Collections.emptyList());
    }
}
