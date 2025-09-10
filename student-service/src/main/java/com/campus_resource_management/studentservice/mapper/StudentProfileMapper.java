package com.campus_resource_management.studentservice.mapper;

import com.campus_resource_management.studentservice.constant.DegreeType;
import com.campus_resource_management.studentservice.constant.Gender;
import com.campus_resource_management.studentservice.constant.StudentStatus;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", imports = {DegreeType.class, Gender.class, StudentStatus.class, LocalDate.class})
public interface StudentProfileMapper {

    // ======== ENTITY -> RESPONSE ========
    DetailedStudentProfileResponse toStudentProfileResponse(StudentProfile studentProfile);
    List<DetailedStudentProfileResponse> toStudentProfileResponseList(List<StudentProfile> studentProfileList);
    SummaryStudentProfileResponse toSummaryResponse(StudentProfile studentProfile);
    List<SummaryStudentProfileResponse> toSummaryResponseList(List<StudentProfile> studentProfileList);


    // ======== ADD REQUEST -> ENTITY ========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "identityId", target = "identityId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(expression = "java(Gender.valueOf(addStudentProfileRequest.getGender().toUpperCase()))",
            target = "gender")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(expression = "java(DegreeType.valueOf(addStudentProfileRequest.getDegreeType().toUpperCase()))",
            target = "degreeType")
    @Mapping(source = "major", target = "major")
    @Mapping(source = "studentNote", target = "studentNote")
    @Mapping(expression = "java(StudentStatus.ACTIVE)", target = "studentStatus")
    @Mapping(expression = "java(0)", target = "creditsCompleted")
    @Mapping(expression = "java(1)", target = "yearOfStudy")
    @Mapping(expression = "java(false)", target = "isDeleted")
    void addStudentProfileRequestBodyToStudentProfile(
            AddStudentProfileRequest addStudentProfileRequest, @MappingTarget StudentProfile studentProfile);

    // ======== UPDATE REQUEST -> ENTITY ========
    // void updateStudentProfileRequestBodyToStudentProfile();


}
