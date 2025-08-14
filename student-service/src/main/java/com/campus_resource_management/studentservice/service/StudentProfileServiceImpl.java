package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.constant.*;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository profileRepository;

    @Override
    public ServiceResponse<StudentProfile> addStudentProfile(AddStudentProfileRequest addStudentProfileRequest) {

        String schoolEmail = generateUniqueSchoolEmail(addStudentProfileRequest.getFirstName(), addStudentProfileRequest.getLastName());

        StudentProfile studentProfile = StudentProfile
                .builder()
                .identityId(addStudentProfileRequest.getIdentityId())
                .email(addStudentProfileRequest.getEmail())
                .firstName(addStudentProfileRequest.getFirstName())
                .lastName(addStudentProfileRequest.getLastName())
                .schoolEmail(schoolEmail)
                .gender(Gender.valueOf(addStudentProfileRequest.getGender().toUpperCase()))
                .dateOfBirth(addStudentProfileRequest.getDateOfBirth())
                .phoneNumber(addStudentProfileRequest.getPhoneNumber())
                .degreeType(DegreeType.valueOf(addStudentProfileRequest.getDegreeType().toUpperCase()))
                .major(addStudentProfileRequest.getMajor())
                .studentNote(addStudentProfileRequest.getStudentNote())
                .build();

        StudentProfile savedStudentProfile = profileRepository.save(studentProfile);

        return ServiceResponse.<StudentProfile>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_STUDENT_PROFILE_SUCCESS))
                .data(savedStudentProfile)
                .build();
    }


    public String generateUniqueSchoolEmail(String firstName, String lastName){
        String baseEmail =
                (firstName.replaceAll("\\s+", "") + "."
                        + lastName.replaceAll("\\s+", "")).toLowerCase();
        String email = baseEmail + "@school.com";
        int counter = 1;

        while (profileRepository.findBySchoolEmail(email).isPresent()) {
            email = baseEmail + counter + "@school.com";
            counter++;
        }
        return email;
    }

}
