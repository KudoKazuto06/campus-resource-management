package com.campus_resource_management.instructorservice.dto.instructor_profile.request;

import com.campus_resource_management.instructorservice.constant.MessageResponse;
import com.campus_resource_management.instructorservice.validation.date_of_birth.ValidDateOfBirth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInstructorProfileRequest {

    @NotBlank(message = MessageResponse.IDENTITY_ID_IS_REQUIRED)
    private String identityId;

    @Size(max = 50, message = MessageResponse.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Size(max = 50, message = MessageResponse.LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Pattern(regexp = "MALE|FEMALE", message = MessageResponse.GENDER_FORMAT_INVALID)
    private String gender;

    @PastOrPresent(message =  MessageResponse.DATE_OF_BIRTH_MUST_BE_IN_THE_PAST)
    @ValidDateOfBirth
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = MessageResponse.EMAIL_FORMAT_INVALID)
    private String email;

    @Pattern(
            regexp =
                    "^(032|033|034|035|036|037|038|039|086|096|097|098|"
                            + // VT
                            "070|076|077|078|079|089|090|093|"
                            + // MobiPhone
                            "081|082|083|084|085|088|091|094|"
                            + // VinaPhone
                            "092|052|056|058|"
                            + // VietnamMobile
                            "059|099)\\d{7}$",
            message = "Invalid phone format")
    private String phoneNumber;

    private String officeLocation;

    @Pattern(regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID)
    private String department;

    @Pattern(regexp = "LECTURER|ASSISTANT_PROFESSOR|ASSOCIATE_PROFESSOR|PROFESSOR|GENERAL_STAFF",
            message = MessageResponse.ACADEMIC_RANK_TYPE_INVALID)
    private String academicRank;

    @Pattern(regexp = "FULL_TIME|PART_TIME|CONTRACT|ADJUNCT|ON_LEAVE|RETIRED",
            message = MessageResponse.EMPLOYMENT_STATUS_TYPE_INVALID)
    private String employmentStatus;

    private LocalDate hireDate;

    private String salaryBand;

    private String officeHours;

    private String instructorNote;

}
