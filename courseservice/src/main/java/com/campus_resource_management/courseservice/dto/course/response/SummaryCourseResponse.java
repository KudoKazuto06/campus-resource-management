package com.campus_resource_management.courseservice.dto.course.response;

import com.campus_resource_management.courseservice.constant.Department;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SummaryCourseResponse {

    private String courseCode;    // e.g., CMP101
    private String courseName;
    private Department department;
    private Double credit;
}
