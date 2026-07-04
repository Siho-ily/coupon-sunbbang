package org.coupon.couponsunbbang.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {

        ErrorCode errorCode = e.getErrorCode();
        log.warn("비즈니스 예외 발생: [{}] {}", errorCode.getCode(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode.getCode(), e.getMessage()));
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException e) {

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        log.warn("잘못된 인자: {}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    /**
     * Handle MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e
    ) {

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : errorCode.getMessage();

        log.warn("검증 예외 발생: {}", message);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode.getCode(), message));
    }

    /**
     * Handle general exceptions (unexpected errors)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception e) {

        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
