package com.campus_resource_management.instructorservice.validation.identity_id;

import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentityMatchValidator
        implements ConstraintValidator<ValidIdentityMatch, Object> {

    private static final Pattern CCCD_PATTERN = Pattern.compile("^(\\d{3})(\\d)(\\d{2})(\\d{6})$");

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        String identityId;
        String gender;
        LocalDate dateOfBirth;

        if (obj instanceof AddInstructorProfileRequest addDto) {
            identityId = addDto.getIdentityId();
            gender = addDto.getGender();
            dateOfBirth = addDto.getDateOfBirth();

        } else if (obj instanceof InstructorProfile patient) {
            identityId = patient.getIdentityId();
            gender = String.valueOf(patient.getGender());
            dateOfBirth = patient.getDateOfBirth();
        } else {
            return true;
        }

        if (identityId == null || gender == null || dateOfBirth == null) {
            return true;
        }

        Matcher matcher = CCCD_PATTERN.matcher(identityId);
        if (!matcher.matches()) return false;

        int genderCode = Integer.parseInt(matcher.group(2));
        int yearSuffix = Integer.parseInt(matcher.group(3));

        int birthYear = dateOfBirth.getYear();
        int calculatedGenderCode =
                switch (birthYear / 100) {
                    case 19 -> gender.equals("MALE") ? 0 : 1;
                    case 20 -> gender.equals("MALE") ? 2 : 3;
                    default -> -1;
                };

        if (calculatedGenderCode != genderCode) return false;
        return birthYear % 100 == yearSuffix;
    }

}
