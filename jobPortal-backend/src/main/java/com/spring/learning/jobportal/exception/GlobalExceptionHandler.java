package com.spring.learning.jobportal.exception;


import com.spring.learning.jobportal.dto.ErrorResponseDto;
import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception, WebRequest webRequest) {
        TraceContext traceContext = tracer.currentTraceContext().context();
        String traceId="";
        if(traceContext != null) {
            traceId = traceContext.traceId();
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(true), HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(), LocalDateTime.now(),traceId);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((fieldError) -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String,String>> handleException(HandlerMethodValidationException exception, WebRequest webRequest) {
        Map<String,String> errors = new HashMap<>();
        exception.getParameterValidationResults().forEach(
                result -> {
                    String parameterName = result.getMethodParameter().getParameterName();
                    String combinedMessage = result.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(
                            Collectors.joining(",")
                    );

                    errors.put(parameterName,combinedMessage);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDto> handleNullException(Exception exception, WebRequest webRequest) {
        TraceContext traceContext = tracer.currentTraceContext().context();
        String traceId="";
        if(traceContext != null) {
            traceId = traceContext.traceId();
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(true), HttpStatus.INTERNAL_SERVER_ERROR,
                "A NullPointerException occured due to "+exception.getMessage(), LocalDateTime.now(),traceId);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler(RegistrationValidationException.class)
    public ResponseEntity<Map<String, String>> handleRegistrationException(
            RegistrationValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getErrors());
    }

}
