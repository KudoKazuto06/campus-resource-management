package com.campus_resource_management.studentservice.dto.student_profile.response;

import com.campus_resource_management.studentservice.constant.DegreeType;
import com.campus_resource_management.studentservice.constant.Gender;
import com.campus_resource_management.studentservice.constant.StudentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedStudentProfileResponse {

    private UUID id;
    private String identityId;
    private String firstName;
    private String lastName;
    private Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String email;
    private String schoolEmail;
    private String address;
    private String phoneNumber;
    private DegreeType degreeType;
    private String major;
    private Double gpa;
    private Integer creditsCompleted;
    private StudentStatus studentStatus;
    private Integer yearOfStudy;
    private String studentNote;

    @JsonFormat(pattern = "HH:mm:ss MM/dd/yyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "HH:mm:ss MM/dd/yyy")
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    private Boolean isDeleted;

}
