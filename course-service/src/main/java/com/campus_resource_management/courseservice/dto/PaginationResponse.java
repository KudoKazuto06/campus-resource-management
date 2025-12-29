package com.campus_resource_management.courseservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse<T> {
    private List<T> listData;
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
}