package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.constant.*;
import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.exception.StudentProfileNotFoundException;
import com.campus_resource_management.studentservice.mapper.StudentProfileMapper;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final StudentProfileMapper studentProfileMapper;

    @Override
    public ServiceResponse<SummaryStudentProfileResponse>
    addStudentProfile(AddStudentProfileRequest addStudentProfileRequest) {

        // 1. Create new entity and map fields from request
        StudentProfile studentProfile = new StudentProfile();
        studentProfileMapper.addStudentProfileRequestBodyToStudentProfile(addStudentProfileRequest, studentProfile);

        // 2. Set dynamic / generated fields
        studentProfile.setSchoolEmail(
                generateUniqueSchoolEmail(
                        addStudentProfileRequest.getFirstName(),
                        addStudentProfileRequest.getLastName()
                )
        );
        studentProfile.setCreatedBy("SYSTEM");

        // 3. Save entity
        StudentProfile savedStudentProfile = profileRepository.save(studentProfile);

        // 4. Map saved entity to response DTO
        SummaryStudentProfileResponse summaryStudentProfileResponse = studentProfileMapper.toSummaryResponse(savedStudentProfile);

        // 5. Return response wrapped in ServiceResponse
        return ServiceResponse.<SummaryStudentProfileResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_STUDENT_PROFILE_SUCCESS))
                .data(summaryStudentProfileResponse)
                .build();
    }

    @Override
    public ServiceResponse<SummaryStudentProfileResponse>
    updateStudentProfile(UpdateStudentProfileRequest updateStudentProfileRequest) {

        // 1. Find existing profile
        StudentProfile existingProfile = profileRepository.findByIdentityId(updateStudentProfileRequest.getIdentityId())
                .orElseThrow(() -> new StudentProfileNotFoundException(updateStudentProfileRequest.getIdentityId()));

        // 2. Map fields from request to entity (only update provided fields)
        studentProfileMapper.updateStudentProfileRequestBodyToStudentProfile(updateStudentProfileRequest, existingProfile);

        // 3. Save updated profile
        StudentProfile savedStudentProfile = profileRepository.save(existingProfile);

        // 4. Map entity to summary response
        SummaryStudentProfileResponse summaryStudentProfileResponse = studentProfileMapper.toSummaryResponse(savedStudentProfile);

        // 5. Return wrapped ServiceResponse
        return ServiceResponse.<SummaryStudentProfileResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.UPDATE_STUDENT_PROFILE_SUCCESS)
                .data(summaryStudentProfileResponse)
                .build();
    }

    @Override
    public ServiceResponse<PaginationResponse>
    viewFilteredStudentProfile(FilterStudentProfileRequest filterStudentProfileRequest){
        return null;
    }

    @Override
    public ServiceResponse<DetailedStudentProfileResponse>
    viewDetailedStudentProfileByIdentityId(String identityId) {
        return null;
    }

    @Override
    public ServiceResponse<Void>
    deleteStudentProfileByIdentityId(String identityId) {
        return null;
    }

    @Override
    public ServiceResponse<Void>
    restoreStudentProfile(String identityId) {
        return null;
    }


    // ------------------------------------------------------------------------------------------------ //

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
