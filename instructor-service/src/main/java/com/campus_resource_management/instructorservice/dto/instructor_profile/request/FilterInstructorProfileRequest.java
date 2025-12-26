package com.campus_resource_management.instructorservice.dto.instructor_profile.request;

import com.campus_resource_management.instructorservice.constant.MessageResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterInstructorProfileRequest {

    @Min(value = 0, message = MessageResponse.PAGE_FORMAT_INVALID)
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = MessageResponse.SIZE_FORMAT_INVALID)
    @Builder.Default
    private Integer size = 10;


    private String identityId;
    private String fullName;
    private String department;

    @Pattern(regexp = "^(|MALE|FEMALE)$", message = MessageResponse.GENDER_FORMAT_INVALID)
    private String gender;

    private String email;
    private String schoolEmail;

    private LocalDate dateOfBirthFrom;
    private LocalDate dateOfBirthTo;

    private LocalDate hireDateFrom;
    private LocalDate hireDateTo;

    @Pattern(regexp = "LECTURER|ASSISTANT_PROFESSOR|ASSOCIATE_PROFESSOR|PROFESSOR|GENERAL_STAFF",
            message = MessageResponse.ACADEMIC_RANK_TYPE_INVALID)
    private String academicRank;

    @Pattern(regexp = "FULL_TIME|PART_TIME|CONTRACT|ADJUNCT|ON_LEAVE|RETIRED",
            message = MessageResponse.EMPLOYMENT_STATUS_TYPE_INVALID)
    private String employmentStatus;

    private String sortBy;

}
