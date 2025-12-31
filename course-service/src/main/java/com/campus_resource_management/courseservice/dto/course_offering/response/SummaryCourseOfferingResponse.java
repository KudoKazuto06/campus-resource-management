package com.campus_resource_management.courseservice.dto.course_offering.response;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class SummaryCourseOfferingResponse {

    private String courseOfferingCode;
    private String courseCode;
    private String term;
    private Integer year;
    private String section;
    private Integer maxStudents;

}
