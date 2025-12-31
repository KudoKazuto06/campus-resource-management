package com.campus_resource_management.courseservice.mapper;

import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseEnrollmentMapper {

    /* ================= ADD WITH COURSE OFFERING ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offeringCode", source = "offering.offeringCode")
    @Mapping(target = "studentIdentityId", source = "request.studentIdentityId")
    @Mapping(target = "isWithdrawn", constant = "false")
    @Mapping(target = "enrolledAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "withdrawnAt", ignore = true)
    @Mapping(target = "finalGrade", ignore = true)
    @Mapping(target = "letterGrade", ignore = true)
    @Mapping(target = "gradeStatus", constant = "NOT_GRADED")
    CourseEnrollment addRequestToEntity(AddCourseEnrollmentRequest request, CourseOffering offering);

    /* ================= UPDATE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "offeringCode", ignore = true) // do not update PK fields
    @Mapping(target = "studentIdentityId", ignore = true) // do not update PK fields
    @Mapping(target = "withdrawnAt", expression = "java(request.getIsWithdrawn() != null && request.getIsWithdrawn() ? java.time.LocalDate.now() : entity.getWithdrawnAt())")
    void updateCourseEnrollmentFromRequest(UpdateCourseEnrollmentRequest request, @MappingTarget CourseEnrollment entity);

    /* ================= SUMMARY RESPONSE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "enrollmentId", source = "id")
    @Mapping(target = "offeringCode", source = "offeringCode")
    @Mapping(target = "studentIdentityId", source = "studentIdentityId")
    @Mapping(target = "isWithdrawn", source = "isWithdrawn")
    @Mapping(target = "enrolledAt", source = "enrolledAt")
    SummaryCourseEnrollmentResponse toSummaryResponse(CourseEnrollment entity);

    /* ================= DETAILED RESPONSE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "offeringCode", source = "offeringCode")
    @Mapping(target = "courseName", ignore = true) // To be filled manually if needed
    @Mapping(target = "term", ignore = true)       // To be filled manually if needed
    @Mapping(target = "year", ignore = true)       // To be filled manually if needed
    @Mapping(target = "instructorName", ignore = true) // To be filled manually if needed
    @Mapping(target = "studentIdentityId", source = "studentIdentityId")
    @Mapping(target = "studentName", ignore = true) // To be filled manually if needed
    @Mapping(target = "studentEmail", ignore = true) // To be filled manually if needed
    @Mapping(target = "yearOfStudy", ignore = true)  // To be filled manually if needed
    @Mapping(target = "degreeType", ignore = true)   // To be filled manually if needed
    @Mapping(target = "isWithdrawn", source = "isWithdrawn")
    @Mapping(target = "enrolledAt", source = "enrolledAt")
    @Mapping(target = "finalGrade", source = "finalGrade")
    @Mapping(target = "letterGrade", source = "letterGrade")
    DetailedCourseEnrollmentResponse toDetailedResponse(CourseEnrollment entity);

}
