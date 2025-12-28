package com.campus_resource_management.courseservice.dto.course.response;

import com.campus_resource_management.courseservice.constant.Department;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DetailedCourseResponse {

    private String courseCode;
    private Integer courseNumber;
    private Department department;

    private String courseName;
    private Double credit;
    private String description;

    private Boolean isDeleted;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
