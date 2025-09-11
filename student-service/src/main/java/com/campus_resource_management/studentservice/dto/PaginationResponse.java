package com.campus_resource_management.studentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse {
    private Object listData;
    private Integer totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
}
