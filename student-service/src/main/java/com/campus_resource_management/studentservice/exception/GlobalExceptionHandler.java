package com.campus_resource_management.studentservice.exception;

import com.campus_resource_management.studentservice.constant.MessageResponse;
import com.campus_resource_management.studentservice.constant.StatusCode;
import com.campus_resource_management.studentservice.constant.StatusResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, String> createErrorMap(String message) {
        return Map.of("error", message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(
                                fieldError.getDefaultMessage()).orElse("Invalid Value"),
                        (existing, replacement) -> existing));

        return ServiceResponse.builder()
                .statusCode(StatusCode.ERROR)
                .status(StatusResponse.FAILURE)
                .errorMessages(errors)
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(FieldExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceResponse handleFieldExistedException(FieldExistedException ex) {
        return ServiceResponse.builder()
                .statusCode(StatusCode.EXISTED)
                .status(StatusResponse.FAILURE)
                .errorMessages(createErrorMap(ex.getMessage()))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
        return ServiceResponse.builder()
                .statusCode(StatusCode.EXISTED)
                .status(StatusResponse.FAILURE)
                .errorMessages(Map.of("error", message))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceResponse handleAllExceptions(Exception ex) {
        return ServiceResponse.builder()
                .statusCode(StatusCode.ERROR)
                .status(StatusResponse.FAILURE)
                .errorMessages(Map.of("error", ex.getMessage()))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }


    private String getFunctionNameWithCustomException(Exception ex) {
        return ex.getStackTrace()[0].getClassName() + "(" + ex.getStackTrace()[0].getMethodName() + ":" + ex.getStackTrace()[0].getLineNumber() + ")";
    }


}
