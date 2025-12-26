package com.campus_resource_management.studentservice.dto.student_profile.request;

import com.campus_resource_management.studentservice.constant.MessageResponse;
import com.campus_resource_management.studentservice.validation.date_of_birth.ValidDateOfBirth;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentProfileRequest {

    @NotBlank(message = MessageResponse.IDENTITY_ID_IS_REQUIRED)
    private String identityId;

    @Size(max = 50, message = MessageResponse.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Size(max = 50, message = MessageResponse.LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = MessageResponse.EMAIL_FORMAT_INVALID)
    private String email;

    @Pattern(regexp = "MALE|FEMALE", message = MessageResponse.GENDER_FORMAT_INVALID) // Currently dont accept OTHERS
    private String gender;

    @PastOrPresent(message =  MessageResponse.DATE_OF_BIRTH_MUST_BE_IN_THE_PAST)
    @ValidDateOfBirth
    private LocalDate dateOfBirth;

    private String address;

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

    @Pattern(regexp = "DIPLOMA|BACHELOR|HONOR|MASTER|PHD", message = "Degree Type Default")
    private String degreeType;

    private String major;

    private String studentNote;

}
