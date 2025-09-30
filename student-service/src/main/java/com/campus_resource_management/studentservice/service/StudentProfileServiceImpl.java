package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.constant.*;
import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.FilterStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.exception.ListEmptyException;
import com.campus_resource_management.studentservice.exception.StudentProfileNotFoundException;
import com.campus_resource_management.studentservice.mapper.StudentProfileMapper;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileMapper studentProfileMapper;
    private final StudentProfileRepository studentProfileRepository;

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
        StudentProfile savedStudentProfile = studentProfileRepository.save(studentProfile);

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
        StudentProfile existingProfile = studentProfileRepository.findByIdentityId(updateStudentProfileRequest.getIdentityId())
                .orElseThrow(() -> new StudentProfileNotFoundException(updateStudentProfileRequest.getIdentityId()));

        // 2. Map fields from request to entity (only update provided fields)
        studentProfileMapper.updateStudentProfileRequestBodyToStudentProfile(updateStudentProfileRequest, existingProfile);
        existingProfile.setModifiedBy("SYSTEM");

        // 3. Save updated profile
        StudentProfile savedStudentProfile = studentProfileRepository.save(existingProfile);

        // 4. Map entity to summary response
        SummaryStudentProfileResponse summaryStudentProfileResponse = studentProfileMapper.toSummaryResponse(savedStudentProfile);

        // 5. Return wrapped ServiceResponse
        return ServiceResponse.<SummaryStudentProfileResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.UPDATE_STUDENT_PROFILE_SUCCESS))
                .data(summaryStudentProfileResponse)
                .build();
    }

    @Override
    public ServiceResponse<PaginationResponse<FilterStudentProfileResponse>>
    viewFilteredStudentProfile(FilterStudentProfileRequest filterStudentProfileRequest){

        // 1. Default page & size if null
        int page = filterStudentProfileRequest.getPage() != null ? filterStudentProfileRequest.getPage() : 0;
        int size = filterStudentProfileRequest.getSize() != null ? filterStudentProfileRequest.getSize() : 5;

        // 2. Build list of Sort.Order based on sortBy field
        List<Sort.Order> orders = new ArrayList<>();
        String sortBy = filterStudentProfileRequest.getSortBy();
        if (sortBy != null && !sortBy.isBlank()) {
            String[] sortParts = filterStudentProfileRequest.getSortBy().split(" ");
            String property = sortParts[0];
            boolean isAscending = sortParts.length == 1 || !sortParts[1].equalsIgnoreCase("desc");
            orders.add(isAscending ? Sort.Order.asc(property) : Sort.Order.desc(property));
        } else {
            // Default sorting
            orders.add(Sort.Order.desc("createdAt"));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        // 3. Fetch paged results
        Page<StudentProfile> studentProfilePage = studentProfileRepository
                .filterStudentProfile(filterStudentProfileRequest, pageable);

        // 4. Handle empty list
        if (studentProfilePage.getContent().isEmpty()) {
            throw new ListEmptyException(MessageResponse.NO_DATA + filterStudentProfileRequest.toString());
        }

        // 5. Map to Summary DTOs and wrap in PaginationResponse
        PaginationResponse<FilterStudentProfileResponse> paginationResponse =
                PaginationResponse.<FilterStudentProfileResponse>builder()
                        .listData(studentProfilePage.getContent().stream()
                                .map(studentProfileMapper::toFilterResponse)
                                .toList())
                        .totalPages(studentProfilePage.getTotalPages())
                        .currentPage(studentProfilePage.getNumber())
                        .totalItems(studentProfilePage.getTotalElements())
                        .pageSize(studentProfilePage.getSize())
                        .build();

        // 6. Wrap in ServiceResponse
        return ServiceResponse.<PaginationResponse<FilterStudentProfileResponse>>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.VIEW_ALL_STUDENT_PROFILE_SUCCESS)
                .data(paginationResponse)
                .build();
    }

    @Override
    public ServiceResponse<DetailedStudentProfileResponse>
    viewDetailedStudentProfileByIdentityId(String identityId) {
        return null;
    }

    @Override
    public ServiceResponse<Void>
    deleteStudentProfileByIdentityId(String identityId) {

        // 1. Find existing profile
        StudentProfile studentProfile = studentProfileRepository.findByIdentityId(identityId)
                .orElseThrow(() -> new StudentProfileNotFoundException(identityId));

        // 2. Soft-delete the Student Profile
        studentProfile.setIsDeleted(true);

        // 3. Save updated profile
        studentProfileRepository.save(studentProfile);

        // 4. Return wrapped ServiceResponse
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.DELETE_STUDENT_PROFILE_SUCCESS))
                .build();

    }

    @Override
    public ServiceResponse<Void>
    restoreStudentProfile(String identityId) {

        // 1. Find existing profile
        StudentProfile studentProfile = studentProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId)
                .orElseThrow(() -> new StudentProfileNotFoundException(identityId));

        // 2. Soft-delete the Student Profile
        studentProfile.setIsDeleted(false);

        // 3. Save updated profile
        studentProfileRepository.save(studentProfile);

        // 4. Return wrapped ServiceResponse
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.RESTORE_STUDENT_PROFILE_SUCCESS))
                .build();

    }


    // ------------------------------------------------------------------------------------------------ //

    public String generateUniqueSchoolEmail(String firstName, String lastName){
        String baseEmail =
                (firstName.replaceAll("\\s+", "") + "."
                        + lastName.replaceAll("\\s+", "")).toLowerCase();
        String email = baseEmail + "@school.com";
        int counter = 1;
        
        while (studentProfileRepository.findBySchoolEmail(email).isPresent()) {
            email = baseEmail + counter + "@school.com";
            counter++;
        }
        return email;
    }

}
