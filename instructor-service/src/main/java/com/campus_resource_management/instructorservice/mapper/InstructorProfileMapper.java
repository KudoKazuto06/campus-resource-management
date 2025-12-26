package com.campus_resource_management.instructorservice.mapper;

import com.campus_resource_management.instructorservice.constant.AcademicRank;
import com.campus_resource_management.instructorservice.constant.Department;
import com.campus_resource_management.instructorservice.constant.EmploymentStatus;
import com.campus_resource_management.instructorservice.constant.Gender;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {Gender.class, AcademicRank.class, Department.class, EmploymentStatus.class, LocalDate.class}
)
public interface InstructorProfileMapper {

    /* ================= ADD ================= */
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void addInstructorProfileRequestBodyToInstructorProfile(
            AddInstructorProfileRequest request,
            @MappingTarget InstructorProfile instructorProfile
    );

    /* ================= UPDATE ================= */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "gender", expression = "java(request.getGender() != null ? Gender.valueOf(request.getGender().toUpperCase()) : instructorProfile.getGender())")
    @Mapping(target = "academicRank", expression = "java(request.getAcademicRank() != null ? AcademicRank.valueOf(request.getAcademicRank().toUpperCase()) : instructorProfile.getAcademicRank())")
    @Mapping(target = "department", expression = "java(request.getDepartment() != null ? Department.valueOf(request.getDepartment().toUpperCase()) : instructorProfile.getDepartment())")
    @Mapping(target = "employmentStatus", expression = "java(request.getEmploymentStatus() != null ? EmploymentStatus.valueOf(request.getEmploymentStatus().toUpperCase()) : instructorProfile.getEmploymentStatus())")
    void updateInstructorProfileRequestBodyToInstructorProfile(
            UpdateInstructorProfileRequest request,
            @MappingTarget InstructorProfile instructorProfile
    );

    /* ================= RESPONSE ================= */

    SummaryInstructorProfileResponse toSummaryResponse(InstructorProfile instructorProfile);

    @Mapping(target = "fullName", expression = "java(instructorProfile.getFirstName() + \" \" + instructorProfile.getLastName())")
    FilterInstructorProfileResponse toFilterResponse(InstructorProfile instructorProfile);

    DetailedInstructorProfileResponse toInstructorProfileResponse(InstructorProfile instructorProfile);
}
