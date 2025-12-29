package com.campus_resource_management.courseservice.mapper;

import com.campus_resource_management.courseservice.constant.Department;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.entity.Course;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {Department.class}
)
public interface CourseMapper {

    /* ================= ADD ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course addCourseRequestToCourse(AddCourseRequest request);

    /* ================= UPDATE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseRequestToCourse(UpdateCourseRequest request, @MappingTarget Course course);

    /* ================= RESPONSE ================= */

    @Mapping(
            target = "department",
            expression = "java(extractDepartment(course.getCourseCode()))"
    )
    SummaryCourseResponse toSummaryResponse(Course course);

    @Mapping(
            target = "department",
            expression = "java(extractDepartment(course.getCourseCode()))"
    )
    DetailedCourseResponse toDetailedResponse(Course course);

    /* ================= HELPER ================= */

    default Department extractDepartment(String courseCode) {
        if (courseCode == null) return null;

        // Extract prefix: CSC101 → CSC
        String prefix = courseCode.replaceAll("[^A-Za-z]", "").toLowerCase();

        return switch (prefix) {
            case "csc" -> Department.COMPUTER_SCIENCE;
            case "swe" -> Department.SOFTWARE_ENGINEERING;
            case "mat" -> Department.MATHEMATICS;
            case "phy" -> Department.PHYSICS;
            case "che" -> Department.CHEMISTRY;
            case "bio" -> Department.BIOLOGY;
            case "eco" -> Department.ECONOMICS;
            case "bus" -> Department.BUSINESS;
            case "psy" -> Department.PSYCHOLOGY;
            case "edu" -> Department.EDUCATION;
            case "his" -> Department.HISTORY;
            case "lit" -> Department.LITERATURE;
            case "phi" -> Department.PHILOSOPHY;
            case "eng" -> Department.ENGINEERING;
            case "med" -> Department.MEDICINE;
            case "nur" -> Department.NURSING;
            case "art" -> Department.ART;
            case "mus" -> Department.MUSIC;
            case "law" -> Department.LAW;
            case "soc" -> Department.SOCIOLOGY;
            default -> null;
        };
    }

}
