package io.github.adampyramide.BlogAPI.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(

        int status,
        String error,
        String message,
        String errorCode,
        String path,
        ZonedDateTime timestamp,

        List<Map<String, String>> fieldErrors

) {}
