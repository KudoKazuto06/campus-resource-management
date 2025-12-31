package com.campus_resource_management.courseservice.exception;

import com.campus_resource_management.courseservice.constant.StatusCode;
import com.campus_resource_management.courseservice.constant.StatusResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, String> createErrorMap(String message) {
        return Map.of("error", message);
    }

    @ExceptionHandler(ListEmptyException.class)
    public ServiceResponse<Void> handleListEmptyException(ListEmptyException ex) {
        return ServiceResponse.<Void>builder()
                .status(StatusResponse.FAILURE)
                .statusCode(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleNotFound1(CourseNotFoundException ex){
        return ServiceResponse.<Void>builder()
                .status(StatusResponse.FAILURE)
                .statusCode(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(CourseOfferingNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleNotFound2(CourseOfferingNotFoundException ex){
        return ServiceResponse.<Void>builder()
                .status(StatusResponse.FAILURE)
                .statusCode(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(CourseEnrollmentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleNotFound3(CourseEnrollmentNotFoundException ex){
        return ServiceResponse.<Void>builder()
                .status(StatusResponse.FAILURE)
                .statusCode(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InstructorNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleNotFound4(InstructorNotFoundException ex){
        return ServiceResponse.<Void>builder()
                .status(StatusResponse.FAILURE)
                .statusCode(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidAcademicTermException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleInvalidAcademicTerm(
            InvalidAcademicTermException ex) {

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.ERROR)
                .status(StatusResponse.FAILURE)
                .errorMessages(Map.of("term", ex.getMessage()))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(
                                fieldError.getDefaultMessage()).orElse("Invalid Value"),
                        (existing, replacement) -> existing));

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.ERROR)
                .status(StatusResponse.FAILURE)
                .errorMessages(errors)
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(FieldExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceResponse<Void> handleFieldExistedException(FieldExistedException ex) {
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.EXISTED)
                .status(StatusResponse.FAILURE)
                .errorMessages(createErrorMap(ex.getMessage()))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceResponse<Void> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.EXISTED)
                .status(StatusResponse.FAILURE)
                .errorMessages(Map.of("error", message))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatEx) {
            String targetType = invalidFormatEx.getTargetType().getSimpleName();
            String invalidValue = invalidFormatEx.getValue().toString();
            String allowedValues = invalidFormatEx.getTargetType().isEnum()
                    ? String.join(", ", java.util.Arrays.stream(invalidFormatEx.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .toArray(String[]::new))
                    : "N/A";

            String message = String.format(
                    "Cannot deserialize value '%s' to %s. Allowed values: [%s]",
                    invalidValue, targetType, allowedValues
            );

            return ServiceResponse.<Void>builder()
                    .statusCode(StatusCode.ERROR)
                    .status(StatusResponse.FAILURE)
                    .errorMessages(Map.of("error", message))
                    .functionThrow(getFunctionNameWithCustomException(ex))
                    .build();
        }

        // fallback for other parsing issues
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.ERROR)
                .status(StatusResponse.FAILURE)
                .errorMessages(Map.of("error", ex.getMessage()))
                .functionThrow(getFunctionNameWithCustomException(ex))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceResponse<Void> handleAllExceptions(Exception ex) {
        return ServiceResponse.<Void>builder()
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
