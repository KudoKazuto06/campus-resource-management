package com.campus_resource_management.studentservice.dto.student_profile.request;

import com.campus_resource_management.studentservice.constant.MessageResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterStudentProfileRequest {

    @Min(value = 0, message = MessageResponse.PAGE_FORMAT_INVALID)
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = MessageResponse.SIZE_FORMAT_INVALID)
    @Builder.Default
    private Integer size = 10;

    private String identityId;
    private String fullName;

    @Pattern(regexp = "^(|MALE|FEMALE)$", message = MessageResponse.GENDER_FORMAT_INVALID)
    private String gender;

    private LocalDate dateOfBirthFrom;
    private LocalDate dateOfBirthTo;

    private String email;
    private String schoolEmail;

    private String degreeType;
    private String major;

    private Double gpaFrom;
    private Double gpaTo;

    private Integer yearOfStudyFrom;
    private Integer yearOfStudyTo;

    @Pattern(regexp = "^(|ACTIVE|INACTIVE|GRADUATED|SUSPENDED|DROPPED)$",
            message = MessageResponse.STUDENT_STATUS_FORMAT_INVALID)
    private String studentStatus;

    private String sortBy;


}
