package com.campus_resource_management.courseservice.dto.course_offering.response;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class DetailedCourseOfferingResponse {

    private String courseCode;
    private String courseName;
    private AcademicTerm term;
    private Integer year;
    private String instructorName;
    private String instructorEmail;
    private String instructorOfficeHour;
    private Integer maxStudents;

}
