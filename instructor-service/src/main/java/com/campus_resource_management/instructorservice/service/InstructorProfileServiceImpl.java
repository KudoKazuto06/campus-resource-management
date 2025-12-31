package com.campus_resource_management.instructorservice.service;

import com.campus_resource_management.instructorservice.constant.*;
import com.campus_resource_management.instructorservice.dto.PaginationResponse;
import com.campus_resource_management.instructorservice.dto.ServiceResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import com.campus_resource_management.instructorservice.exception.FieldExistedException;
import com.campus_resource_management.instructorservice.exception.InstructorProfileNotFoundException;
import com.campus_resource_management.instructorservice.exception.ListEmptyException;
import com.campus_resource_management.instructorservice.mapper.InstructorProfileMapper;
import com.campus_resource_management.instructorservice.repository.InstructorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorProfileServiceImpl implements InstructorProfileService {

    private final InstructorProfileMapper instructorProfileMapper;
    private final InstructorProfileRepository instructorProfileRepository;

    @Override
    public ServiceResponse<SummaryInstructorProfileResponse> addInstructorProfile(AddInstructorProfileRequest addInstructorProfileRequest) {

        // 1. Create entity and map (mapper might throw)
        InstructorProfile instructorProfile = new InstructorProfile();
        instructorProfileMapper.addInstructorProfileRequestBodyToInstructorProfile(addInstructorProfileRequest, instructorProfile);

        // 2. Check uniqueness
        if (instructorProfileRepository.findActiveByIdentityId(addInstructorProfileRequest.getIdentityId()).isPresent()) {
            throw new FieldExistedException("IdentityId already exists");
        }

        if (instructorProfileRepository.findActiveByEmail(addInstructorProfileRequest.getEmail()).isPresent()) {
            throw new FieldExistedException("Email already exists");
        }

        // 3. Set dynamic fields
        instructorProfile.setCreatedBy("SYSTEM");
        instructorProfile.setSchoolEmail(generateUniqueSchoolEmail(
                addInstructorProfileRequest.getFirstName(),
                addInstructorProfileRequest.getLastName()
        ));

        // 4. Save
        InstructorProfile savedProfile = instructorProfileRepository.save(instructorProfile);

        // 5. Map to response
        SummaryInstructorProfileResponse response = instructorProfileMapper.toSummaryResponse(savedProfile);

        // 6. Return
        return ServiceResponse.<SummaryInstructorProfileResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_INSTRUCTOR_PROFILE_SUCCESS))
                .data(response)
                .build();
    }



    @Override
    public ServiceResponse<SummaryInstructorProfileResponse>
    updateInstructorProfile(UpdateInstructorProfileRequest updateInstructorProfileRequest) {

        // 1. Find existing profile
        InstructorProfile existingProfile =
                instructorProfileRepository.findActiveByIdentityId(
                                updateInstructorProfileRequest.getIdentityId())
                        .orElseThrow(() ->
                                new InstructorProfileNotFoundException(
                                        updateInstructorProfileRequest.getIdentityId()));

        if (instructorProfileRepository.findActiveByEmail(updateInstructorProfileRequest.getEmail()).isPresent()) {
            throw new FieldExistedException("Email already exists");
        }

        // 2. Map fields from request to entity
        instructorProfileMapper.updateInstructorProfileRequestBodyToInstructorProfile(
                updateInstructorProfileRequest, existingProfile);
        existingProfile.setModifiedBy("SYSTEM");

        // 3. Save updated profile
        InstructorProfile savedProfile =
                instructorProfileRepository.save(existingProfile);

        // 4. Map entity to summary response
        SummaryInstructorProfileResponse response =
                instructorProfileMapper.toSummaryResponse(savedProfile);

        // 5. Return wrapped ServiceResponse
        return ServiceResponse.<SummaryInstructorProfileResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(
                        MessageResponse.UPDATE_INSTRUCTOR_PROFILE_SUCCESS))
                .data(response)
                .build();
    }

    @Override
    public ServiceResponse<PaginationResponse<FilterInstructorProfileResponse>>
    viewFilteredInstructorProfile(FilterInstructorProfileRequest filterInstructorProfileRequest) {

        // 1. Default page & size
        int page = filterInstructorProfileRequest.getPage() != null
                ? filterInstructorProfileRequest.getPage() : 0;
        int size = filterInstructorProfileRequest.getSize() != null
                ? filterInstructorProfileRequest.getSize() : 5;

        // 2. Build Sort orders
        List<Sort.Order> orders = new ArrayList<>();
        String sortBy = filterInstructorProfileRequest.getSortBy();

        if (sortBy != null && !sortBy.isBlank()) {
            String[] parts = sortBy.split(" ");
            String property = parts[0];
            boolean asc = parts.length == 1 ||
                    !parts[1].equalsIgnoreCase("desc");
            orders.add(asc ? Sort.Order.asc(property)
                    : Sort.Order.desc(property));
        } else {
            orders.add(Sort.Order.desc("createdAt"));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        // 3. Fetch paged results
        Page<InstructorProfile> instructorPage =
                instructorProfileRepository.filterInstructorProfile(
                        filterInstructorProfileRequest, pageable);

        // 4. Handle empty list
        if (instructorPage.getContent().isEmpty()) {
            throw new ListEmptyException(
                    MessageResponse.NO_DATA +
                            filterInstructorProfileRequest.toString());
        }

        // 5. Map to DTO + PaginationResponse
        PaginationResponse<FilterInstructorProfileResponse> paginationResponse =
                PaginationResponse.<FilterInstructorProfileResponse>builder()
                        .listData(
                                instructorPage.getContent().stream()
                                        .map(instructorProfileMapper::toFilterResponse)
                                        .toList())
                        .totalPages(instructorPage.getTotalPages())
                        .currentPage(instructorPage.getNumber())
                        .totalItems(instructorPage.getTotalElements())
                        .pageSize(instructorPage.getSize())
                        .build();

        // 6. Wrap in ServiceResponse
        return ServiceResponse.<PaginationResponse<FilterInstructorProfileResponse>>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(
                        MessageResponse.VIEW_ALL_INSTRUCTOR_PROFILE_SUCCESS))
                .data(paginationResponse)
                .build();
    }

    @Override
    public ServiceResponse<DetailedInstructorProfileResponse>
    viewDetailedInstructorProfileByIdentityId(String identityId) {

        // 1. Fetch instructor profile
        InstructorProfile instructorProfile =
                instructorProfileRepository.findByIdentityId(identityId)
                        .orElseThrow(() ->
                                new InstructorProfileNotFoundException(identityId));

        // 2. Map entity and return
        return ServiceResponse.<DetailedInstructorProfileResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(
                        MessageResponse.VIEW_DETAIL_INSTRUCTOR_PROFILE_SUCCESS))
                .data(instructorProfileMapper.toInstructorProfileResponse(instructorProfile))
                .build();
    }

    @Override
    public ServiceResponse<Void>
    deleteInstructorProfileByIdentityId(String identityId) {

        // 1. Find existing profile
        InstructorProfile instructorProfile =
                instructorProfileRepository.findByIdentityId(identityId)
                        .orElseThrow(() ->
                                new InstructorProfileNotFoundException(identityId));

        // 2. Soft delete
        instructorProfile.setIsDeleted(true);

        // 3. Save
        instructorProfileRepository.save(instructorProfile);

        // 4. Return response
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(
                        MessageResponse.DELETE_INSTRUCTOR_PROFILE_SUCCESS))
                .build();
    }

    @Override
    public ServiceResponse<Void> restoreInstructorProfile(String identityId) {

        // 1. Find the profile including soft-deleted ones
        InstructorProfile instructorProfile =
                instructorProfileRepository
                        .findByIdentityIdIncludeSoftDeleted(identityId)
                        .orElseThrow(() ->
                                new InstructorProfileNotFoundException(identityId));

        // 2. Check for conflicts with other active profiles (identityId)
        boolean identityConflict = instructorProfileRepository.findByIdentityId(identityId)
                .filter(p -> !p.getId().equals(instructorProfile.getId()))
                .isPresent();

        if (identityConflict) {
            throw new FieldExistedException(
                    "Cannot restore: identityId already used by another active profile");
        }

        // 3. Check for conflicts with other active profiles (email)
        boolean emailConflict = instructorProfileRepository.findByEmail(instructorProfile.getEmail())
                .filter(p -> !p.getId().equals(instructorProfile.getId()))
                .isPresent();

        if (emailConflict) {
            throw new FieldExistedException(
                    "Cannot restore: email already used by another active profile");
        }

        // 4. Restore
        instructorProfile.setIsDeleted(false);

        // 5. Save
        instructorProfileRepository.save(instructorProfile);

        // 6. Return response
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(
                        MessageResponse.RESTORE_INSTRUCTOR_PROFILE_SUCCESS))
                .build();
    }


    // ------------------------------------------------------------------------------------------------ //

    public String generateUniqueSchoolEmail(String firstName, String lastName){
        String baseEmail =
                (firstName.replaceAll("\\s+", "") + "."
                        + lastName.replaceAll("\\s+", "")).toLowerCase();
        String email = baseEmail + "@prf.byteacademy.com";
        int counter = 1;

        while (instructorProfileRepository.findBySchoolEmail(email).isPresent()) {
            email = baseEmail + counter + "@prf.byteacademy.com";
            counter++;
        }
        return email;
    }


}
