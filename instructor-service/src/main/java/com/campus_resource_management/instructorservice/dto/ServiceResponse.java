package com.campus_resource_management.instructorservice.dto;

import com.campus_resource_management.instructorservice.constant.StatusResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> {

    private String message;
    private StatusResponse status;
    private T data;
    private int statusCode;
    private Object errorMessages;
    private String functionThrow;

    @Builder.Default
    @JsonFormat(pattern = "HH:mm:ss MM/dd/yyyy")
    private LocalDateTime timestamp = LocalDateTime.now();
}
