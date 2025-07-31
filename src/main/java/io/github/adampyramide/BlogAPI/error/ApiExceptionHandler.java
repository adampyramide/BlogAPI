package io.github.adampyramide.BlogAPI.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final ZoneId zoneId = ZoneOffset.UTC;;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiException e, HttpServletRequest request) {
        logger.warn("API Error [{}]: {}", e.getErrorCode(), e.getMessage());

        HttpStatus httpStatus = e.getHttpStatus();
        ApiError apiError = new ApiError(
                httpStatus.value(),
                httpStatus.getReasonPhrase().toUpperCase().replace(" ", "_"),
                e.getErrorCode(),
                e.getMessage(),
                request.getRequestURI(),
                ZonedDateTime.now(zoneId),
                null
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "VALIDATION_ERROR",
                "Validation failed for " + fieldErrors.size() + " field(s).",
                request.getRequestURI(),
                ZonedDateTime.now(zoneId),
                fieldErrors
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String param = ex.getName();
        String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";

        String message = "Invalid value for parameter '" + param + "': " + invalidValue +
                ". Expected type: " + expectedType;

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "INVALID_PARAMETER_TYPE",
                message,
                request.getRequestURI(),
                ZonedDateTime.now(zoneId),
                null
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
