package io.github.adampyramide.BlogAPI.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ExceptionResponse(
        String message,
        HttpStatus httpStatus,
        ZonedDateTime timeStamp
) {}
