package com.campus_resource_management.courseservice.dto.course_offering.response;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class SummaryCourseOfferingResponse {

    private UUID offeringId;
    private String courseCode;
    private AcademicTerm term;
    private Integer year;
    private Integer maxStudents;

}
