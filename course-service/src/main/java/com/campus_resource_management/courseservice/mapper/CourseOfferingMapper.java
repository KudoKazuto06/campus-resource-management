package com.campus_resource_management.courseservice.mapper;

import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.grpc.dto.InstructorInfo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseOfferingMapper {

    /* ================= ADD ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", source = "course")
    @Mapping(
            target = "term",
            expression = "java(com.campus_resource_management.courseservice.constant.AcademicTerm.valueOf(request.getTerm().toUpperCase()))"
    )
    @Mapping(target = "offeringCode", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "isDeleted", constant = "false")
    CourseOffering addRequestToEntity(AddCourseOfferingRequest request, Course course);

    /* ================= UPDATE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseOfferingFromRequest(UpdateCourseOfferingRequest request, @MappingTarget CourseOffering entity);

    /* ================= SUMMARY RESPONSE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "courseOfferingCode", source = "offeringCode")
    @Mapping(target = "courseCode", source = "course.courseCode")
    @Mapping(target = "term", expression = "java(courseOffering.getTerm().name())")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "section", source = "section")
    @Mapping(target = "maxStudents", source = "maxStudents")
    SummaryCourseOfferingResponse toSummaryResponse(CourseOffering courseOffering);

    /* ================= DETAILED RESPONSE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "courseOfferingCode", source = "courseOffering.offeringCode")
    @Mapping(target = "courseCode", source = "courseOffering.course.courseCode")
    @Mapping(target = "courseName", source = "courseOffering.course.courseName")
    @Mapping(target = "term", expression = "java(courseOffering.getTerm())")
    @Mapping(target = "year", source = "courseOffering.year")
    @Mapping(target = "section", source = "courseOffering.section")
    @Mapping(target = "maxStudents", source = "courseOffering.maxStudents")
    @Mapping(target = "instructorName", expression = "java(instructorInfo.getFirstName() + \" \" + instructorInfo.getLastName())")
    @Mapping(target = "instructorEmail", source = "instructorInfo.email")
    @Mapping(target = "instructorOfficeHour", source = "instructorInfo.officeHours")
    DetailedCourseOfferingResponse toDetailedResponse(CourseOffering courseOffering, InstructorInfo instructorInfo);

}
