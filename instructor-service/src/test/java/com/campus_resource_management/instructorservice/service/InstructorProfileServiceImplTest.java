package com.campus_resource_management.instructorservice.service;

import com.campus_resource_management.instructorservice.constant.MessageResponse;
import com.campus_resource_management.instructorservice.constant.StatusCode;
import com.campus_resource_management.instructorservice.constant.StatusResponse;
import com.campus_resource_management.instructorservice.dto.PaginationResponse;
import com.campus_resource_management.instructorservice.dto.ServiceResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import com.campus_resource_management.instructorservice.exception.*;
import com.campus_resource_management.instructorservice.mapper.InstructorProfileMapper;
import com.campus_resource_management.instructorservice.repository.InstructorProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorProfileServiceImplTest {

    @Mock
    private InstructorProfileRepository instructorProfileRepository;

    @Mock
    private InstructorProfileMapper instructorProfileMapper;

    @InjectMocks
    private InstructorProfileServiceImpl instructorProfileService;

    @Test
    void testAddInstructorProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request DTO
        AddInstructorProfileRequest request = new AddInstructorProfileRequest();
        request.setFirstName("Cong Thinh");
        request.setLastName("Nguyen");

        // 2. Prepare mocked saved entity and response DTO
        InstructorProfile savedProfile = new InstructorProfile();
        savedProfile.setIdentityId("id-123");

        SummaryInstructorProfileResponse summaryResponse = SummaryInstructorProfileResponse.builder()
                .identityId("id-123")
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .schoolEmail("congthinh@school.com")
                .department("abc")
                .build();

        // 3. Mock mapper behavior for mapping request -> entity
        doNothing().when(instructorProfileMapper)
                .addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));

        // 4. Mock repository save
        when(instructorProfileRepository.save(any(InstructorProfile.class))).thenReturn(savedProfile);

        // 5. Mock mapper behavior for entity -> summary DTO
        when(instructorProfileMapper.toSummaryResponse(any(InstructorProfile.class))).thenReturn(summaryResponse);

        // 6. Call the service method
        ServiceResponse<SummaryInstructorProfileResponse> serviceResponse =
                instructorProfileService.addInstructorProfile(request);

        // 7. Assertions
        assertEquals(StatusCode.CREATED, serviceResponse.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, serviceResponse.getStatus());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.ADD_INSTRUCTOR_PROFILE_SUCCESS));
        assertEquals(summaryResponse, serviceResponse.getData());

        // 8. Verify interactions
        verify(instructorProfileMapper).addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));
        verify(instructorProfileRepository).save(any(InstructorProfile.class));
        verify(instructorProfileMapper).toSummaryResponse(any(InstructorProfile.class));
    }

    @Test
    void testAddInstructorProfile_error_RepositoryThrowsException(){
        // 1. Prepare request DTO
        AddInstructorProfileRequest request = new AddInstructorProfileRequest();
        request.setFirstName("Cong Thinh");
        request.setLastName("Nguyen");


        // 2. Mock mapper to do nothing
        doNothing().when(instructorProfileMapper)
                .addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));

        // 3. Mock repository to throw exception
        when(instructorProfileRepository.save(any(InstructorProfile.class)))
                .thenThrow(new RuntimeException());

        // 4. Call service method and assert exception
        Exception exception = assertThrows(RuntimeException.class, () ->
                instructorProfileService.addInstructorProfile(request));

        assertNotNull(exception);

        // 5. Verify mapper was called, repository was called
        verify(instructorProfileMapper).addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));
        verify(instructorProfileRepository).save(any(InstructorProfile.class));
    }

    @Test
    void testAddInstructorProfile_error_MapperThrowsException(){
        // 1. Prepare request DTO
        AddInstructorProfileRequest request = new AddInstructorProfileRequest();
        request.setIdentityId("id-123");
        request.setFirstName("Nguyen");
        request.setLastName("Cong Thinh");
        request.setEmail("test@gmail.com");

        // 2. Mock repository checks (must pass first)
        when(instructorProfileRepository.findByIdentityId(any()))
                .thenReturn(Optional.empty());
        when(instructorProfileRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        // 3. Mock mapper to throw exception
        doThrow(new IllegalArgumentException("Invalid data"))
                .when(instructorProfileMapper)
                .addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));

        // 4. Assert that exception is propagated
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                instructorProfileService.addInstructorProfile(request));

        assertEquals("Invalid data", exception.getMessage());

        // 4. Verify mapper was called
        verify(instructorProfileMapper).addInstructorProfileRequestBodyToInstructorProfile(any(AddInstructorProfileRequest.class), any(InstructorProfile.class));

        // 5. Verify interactions
        verify(instructorProfileRepository).findByIdentityId(any());
        verify(instructorProfileRepository).findByEmail(any());
        verify(instructorProfileMapper)
                .addInstructorProfileRequestBodyToInstructorProfile(any(), any());

        // 6. Save must NEVER be called
        verify(instructorProfileRepository, never()).save(any());
    }

    @Test
    void testUpdateInstructorProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request DTO
        Long profileId = 1L;
        UpdateInstructorProfileRequest request = UpdateInstructorProfileRequest.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 2. Prepare existing entity and updated entity
        InstructorProfile existingProfile = InstructorProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Old Name")
                .lastName("Kiyotaka")
                .email("kudokazuto06@gmail.com")
                .build();

        InstructorProfile updatedProfile = InstructorProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 3. Prepare expected response DTO
        SummaryInstructorProfileResponse responseDto = SummaryInstructorProfileResponse.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 4. Mock repository + mapper behaviour
        when(instructorProfileRepository.findByIdentityId(String.valueOf(profileId)))
                .thenReturn(Optional.of(existingProfile));

        doNothing().when(instructorProfileMapper)
                .updateInstructorProfileRequestBodyToInstructorProfile(any(UpdateInstructorProfileRequest.class), eq(existingProfile));

        when(instructorProfileRepository.save(existingProfile))
                .thenReturn(updatedProfile);

        when(instructorProfileMapper.toSummaryResponse(updatedProfile))
                .thenReturn(responseDto);

        // 5. Call the service method
        ServiceResponse<SummaryInstructorProfileResponse> serviceResponse =
                instructorProfileService.updateInstructorProfile(request);

        // 6. Verify returned response
        assertEquals(StatusCode.SUCCESS, serviceResponse.getStatusCode());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.UPDATE_INSTRUCTOR_PROFILE_SUCCESS));
        assertEquals(responseDto, serviceResponse.getData());

        // 7. Verify interactions with mocks
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(profileId));
        verify(instructorProfileMapper).updateInstructorProfileRequestBodyToInstructorProfile(request, existingProfile);
        verify(instructorProfileRepository).save(existingProfile);
        verify(instructorProfileMapper).toSummaryResponse(updatedProfile);
    }

    @Test
    void testUpdateInstructorProfile_error_InstructorProfileNotFound() {
        // 1. Prepare request with non-existent ID
        Long identityId = 1L;
        UpdateInstructorProfileRequest request = UpdateInstructorProfileRequest.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        // 2. Mock repository to return empty (simulate not found in database)
        when(instructorProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.empty());

        // 3. Call service and expect exception
        InstructorProfileNotFoundException ex = assertThrows(InstructorProfileNotFoundException.class, () -> {
            instructorProfileService.updateInstructorProfile(request);
        });

        // 4. Verify repository interaction
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(identityId));
        assertEquals("Instructor profile not found with id: " + identityId, ex.getMessage());

        // 5. Ensure no save or mapper was called
        verify(instructorProfileRepository, never()).save(any());
        verify(instructorProfileMapper, never()).toSummaryResponse(any());
    }

    @Test
    void testUpdateInstructorProfile_error_RepositoryThrowsException() {
        // 1. Prepare request and existing profile
        Long identityId = 1L;
        UpdateInstructorProfileRequest request = UpdateInstructorProfileRequest.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        InstructorProfile existingProfile = InstructorProfile.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Old")
                .lastName("Name")
                .build();

        // 2. Mock repository and mapper
        when(instructorProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.of(existingProfile));

        doNothing().when(instructorProfileMapper)
                .updateInstructorProfileRequestBodyToInstructorProfile(any(UpdateInstructorProfileRequest.class), eq(existingProfile));

        // Simulate database save failure
        when(instructorProfileRepository.save(existingProfile))
                .thenThrow(new RuntimeException("Database write error"));

        // 3. Call service and expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            instructorProfileService.updateInstructorProfile(request);
        });

        // 4. Verify exception message
        assertEquals("Database write error", exception.getMessage());

        // 5. Verify repository interactions (DB hit and save attempt)
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(identityId));
        verify(instructorProfileRepository).save(existingProfile);
    }

    @Test
    void testDeleteInstructorProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request
        Long profileId = 1L;

        // 2. Prepare existing Instructor profile
        InstructorProfile existingProfile = InstructorProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        InstructorProfile deletedProfile = InstructorProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. Mock repository
        when(instructorProfileRepository.findByIdentityId(String.valueOf(profileId)))
                .thenReturn(Optional.of(existingProfile));

        when(instructorProfileRepository.save(existingProfile))
                .thenReturn(deletedProfile);

        // 4. Call service method
        ServiceResponse<Void> response = instructorProfileService.deleteInstructorProfileByIdentityId(String.valueOf(profileId));

        // 5. Assert successful response
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.DELETE_INSTRUCTOR_PROFILE_SUCCESS));

        // 6. Verify repository interactions (simulate DB)
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(profileId));
        verify(instructorProfileRepository).save(existingProfile);

    }

    @Test
    void testDeleteInstructorProfile_error_InstructorProfileNotFound() {
        // 1. Prepare input
        Long identityId = 1L;

        // 2. Mock repository to return empty
        when(instructorProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.empty());

        // 3. Call service and expect not found exception
        assertThrows(InstructorProfileNotFoundException.class, () -> {
            instructorProfileService.deleteInstructorProfileByIdentityId(String.valueOf(identityId));
        });

        // 4. Verify repository check
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(identityId));

        // 5. Verify save was never called
        verify(instructorProfileRepository, never()).save(any());
    }

    @Test
    void testDeleteInstructorProfile_error_RepositoryThrowsException() {
        // 1. Prepare input
        Long identityId = 1L;

        // 2. Prepare existing profile
        InstructorProfile existingProfile = InstructorProfile.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        // 3. Mock repository to simulate DB error on save
        when(instructorProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.of(existingProfile));

        when(instructorProfileRepository.save(existingProfile))
                .thenThrow(new RuntimeException("Database write failure"));

        // 4. Call service and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            instructorProfileService.deleteInstructorProfileByIdentityId(String.valueOf(identityId));
        });

        // 5. Check message and interactions
        assertEquals("Database write failure", exception.getMessage());
        verify(instructorProfileRepository).findByIdentityId(String.valueOf(identityId));
        verify(instructorProfileRepository).save(existingProfile);
    }

    @Test
    void testRestoreInstructorProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare a soft-deleted Instructor profile
        InstructorProfile deletedProfile = InstructorProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. What we expect after restoring
        InstructorProfile restoredProfile = InstructorProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        // 4. Mock repository behavior
        when(instructorProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.of(deletedProfile));
        when(instructorProfileRepository.save(any(InstructorProfile.class)))
                .thenReturn(restoredProfile);

        // 5. Call service method
        ServiceResponse<Void> response = instructorProfileService.restoreInstructorProfile(identityId);

        // 6. Assert response is successful
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.RESTORE_INSTRUCTOR_PROFILE_SUCCESS));

        // 7. Verify repository interactions
        verify(instructorProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(instructorProfileRepository).save(deletedProfile);
    }

    @Test
    void testRestoreInstructorProfile_error_InstructorProfileNotFound() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Mock repository to return empty (no such Instructor)
        when(instructorProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.empty());

        // 3. Expect InstructorProfileNotFoundException
        assertThrows(InstructorProfileNotFoundException.class, () -> {
            instructorProfileService.restoreInstructorProfile(identityId);
        });

        // 4. Verify repository calls
        verify(instructorProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(instructorProfileRepository, never()).save(any());
    }

    @Test
    void testRestoreInstructorProfile_error_RepositoryThrowsException() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare existing deleted profile
        InstructorProfile deletedProfile = InstructorProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. Mock repository to throw DB error when saving
        when(instructorProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.of(deletedProfile));

        when(instructorProfileRepository.save(deletedProfile))
                .thenThrow(new RuntimeException("Database error while restoring"));

        // 4. Assert the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            instructorProfileService.restoreInstructorProfile(identityId);
        });

        // 5. Verify message and repository calls
        assertEquals("Database error while restoring", exception.getMessage());
        verify(instructorProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(instructorProfileRepository).save(deletedProfile);
    }

    @Test
    void testViewDetailedInstructorProfileByIdentityId_success_shouldReturnDetailResponse() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare existing Instructor profile
        InstructorProfile instructorProfile = InstructorProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("congthinh.nguyen@gmail.com")
                .build();

        DetailedInstructorProfileResponse responseDto = DetailedInstructorProfileResponse.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("congthinh.nguyen@gmail.com")
                .build();

        // 3. Mock repository and mapper
        when(instructorProfileRepository.findByIdentityId(identityId))
                .thenReturn(Optional.of(instructorProfile));

        when(instructorProfileMapper.toInstructorProfileResponse(instructorProfile))
                .thenReturn(responseDto);

        // 4. Call service method
        ServiceResponse<DetailedInstructorProfileResponse> serviceResponse =
                instructorProfileService.viewDetailedInstructorProfileByIdentityId(identityId);

        // 5. Assert response
        assertEquals(StatusCode.SUCCESS, serviceResponse.getStatusCode());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.VIEW_DETAIL_INSTRUCTOR_PROFILE_SUCCESS));
        assertEquals(responseDto, serviceResponse.getData());

        // 6. Verify repository and mapper interactions
        verify(instructorProfileRepository).findByIdentityId(identityId);
        verify(instructorProfileMapper).toInstructorProfileResponse(instructorProfile);
    }

    @Test
    void testViewDetailedInstructorProfileByIdentityId_error_InstructorProfileNotFound() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Mock repository to return empty
        when(instructorProfileRepository.findByIdentityId(identityId))
                .thenReturn(Optional.empty());

        // 3. Expect InstructorProfileNotFoundException
        assertThrows(InstructorProfileNotFoundException.class, () -> {
            instructorProfileService.viewDetailedInstructorProfileByIdentityId(identityId);
        });

        // 4. Verify repository interaction
        verify(instructorProfileRepository).findByIdentityId(identityId);
        verify(instructorProfileMapper, never()).toInstructorProfileResponse(any());
    }

    @Test
    void testViewDetailedInstructorProfileByIdentityId_error_RepositoryThrowsException() {
        // 1. Prepare identityId
        Long identityId = 1L;
        String identityIdStr = String.valueOf(identityId);

        // 2. Mock repository to throw exception
        when(instructorProfileRepository.findByIdentityId(identityIdStr))
                .thenThrow(new RuntimeException("Database error"));

        // 3. Call service and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            instructorProfileService.viewDetailedInstructorProfileByIdentityId(identityIdStr);
        });

        // 4. Assert exception message
        assertEquals("Database error", exception.getMessage());

        // 5. Verify repository call
        verify(instructorProfileRepository).findByIdentityId(identityIdStr);
        verify(instructorProfileMapper, never()).toInstructorProfileResponse(any());
    }

    @Test
    void testViewFilteredInstructorProfile_success1() {
        // 1. Prepare filter request
        FilterInstructorProfileRequest filterRequest = FilterInstructorProfileRequest.builder()
                .page(0)
                .size(5)
                .sortBy("firstName asc")
                .build();

        // 2. Prepare repository return
        InstructorProfile instructorProfile = InstructorProfile.builder()
                .identityId(String.valueOf(1L))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        List<InstructorProfile> profileList = List.of(instructorProfile);
        Page<InstructorProfile> pageResult = new PageImpl<>(profileList);

        lenient().when(instructorProfileRepository.filterInstructorProfile(filterRequest,
                        PageRequest.of(0,5, Sort.by(Sort.Order.asc("firstName")))))
                .thenReturn(pageResult);

        // 3. Prepare mapper
        FilterInstructorProfileResponse mappedResponse = FilterInstructorProfileResponse.builder()
                .identityId(String.valueOf(1L))
                .fullName("Cong Thinh Nguyen")
                .build();

        when(instructorProfileMapper.toFilterResponse(instructorProfile))
                .thenReturn(mappedResponse);

        // 4. Call service
        ServiceResponse<PaginationResponse<FilterInstructorProfileResponse>> response =
                instructorProfileService.viewFilteredInstructorProfile(filterRequest);

        // 5. Assert response
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.VIEW_ALL_INSTRUCTOR_PROFILE_SUCCESS));

        PaginationResponse<FilterInstructorProfileResponse> pagination = response.getData();
        assertNotNull(pagination);
        assertEquals(1, pagination.getTotalItems());
        assertEquals(1, pagination.getTotalPages());
        assertEquals(0, pagination.getCurrentPage());
        assertEquals(mappedResponse, pagination.getListData().get(0));

        // 6. Verify interactions
        verify(instructorProfileRepository).filterInstructorProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.asc("firstName"))));
        verify(instructorProfileMapper).toFilterResponse(instructorProfile);
    }

    @Test
    void testViewFilteredInstructorProfile_success2_DefaultPageSize() {
        // 1. Filter request with null page & size
        FilterInstructorProfileRequest filterRequest = FilterInstructorProfileRequest.builder()
                .page(0)
                .size(5)
                .sortBy("firstName desc")
                .build();

        InstructorProfile instructorProfile = InstructorProfile.builder()
                .identityId(String.valueOf(1L))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        Page<InstructorProfile> pageResult = new PageImpl<>(List.of(instructorProfile));

        // 2. Mock repository
        lenient().when(instructorProfileRepository.filterInstructorProfile(filterRequest,
                        PageRequest.of(0,5, Sort.by(Sort.Order.desc("firstName")))))
                .thenReturn(pageResult);

        // 3. Mapper
        FilterInstructorProfileResponse mappedResponse = FilterInstructorProfileResponse.builder()
                .identityId(String.valueOf(1L))
                .fullName("Cong Thinh Nguyen")
                .build();

        when(instructorProfileMapper.toFilterResponse(instructorProfile)).thenReturn(mappedResponse);

        // 4. Call service
        ServiceResponse<PaginationResponse<FilterInstructorProfileResponse>> response =
                instructorProfileService.viewFilteredInstructorProfile(filterRequest);

        // 5. Assert
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(1, response.getData().getTotalItems());
        assertEquals(mappedResponse, response.getData().getListData().get(0));

        // 6. Verify
        verify(instructorProfileRepository).filterInstructorProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.desc("firstName"))));
        verify(instructorProfileMapper).toFilterResponse(instructorProfile);
    }

    @Test
    void testViewFilteredInstructorProfile_success3_ReturnEmptyList() {
        // 1. Filter request
        FilterInstructorProfileRequest filterRequest = FilterInstructorProfileRequest.builder()
                .page(0)
                .size(5)
                .build();

        // 2. Mock empty page
        lenient().when(instructorProfileRepository.filterInstructorProfile(any(FilterInstructorProfileRequest.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        // 3. Call service and expect exception
        assertThrows(ListEmptyException.class, () -> {
            instructorProfileService.viewFilteredInstructorProfile(filterRequest);
        });

        // 4. Verify repository called
        verify(instructorProfileRepository).filterInstructorProfile(any(FilterInstructorProfileRequest.class), any(Pageable.class));
        verifyNoMoreInteractions(instructorProfileMapper);
    }

    @Test
    void testViewFilteredInstructorProfile_error_RepositoryThrowException() {
        // 1. Filter request
        FilterInstructorProfileRequest filterRequest = FilterInstructorProfileRequest.builder()
                .page(0)
                .size(5)
                .build();

        // 2. Mock repository to throw
        lenient().when(instructorProfileRepository.filterInstructorProfile(any(FilterInstructorProfileRequest.class), any(Pageable.class)))
                .thenThrow(new RuntimeException("Database failure"));

        // 3. Call service and assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            instructorProfileService.viewFilteredInstructorProfile(filterRequest);
        });

        // 4. Check message
        assertEquals("Database failure", exception.getMessage());

        // 5. Verify repository
        verify(instructorProfileRepository).filterInstructorProfile(any(FilterInstructorProfileRequest.class), any(Pageable.class));
        verifyNoInteractions(instructorProfileMapper);
    }

    @Test
    void testGenerateUniqueSchoolEmail_Unique() {
        // 1. Prepare names
        String firstName = "Cong Thinh";
        String lastName = "Nguyen";

        // 2. Mock repository: no existing email
        when(instructorProfileRepository.findBySchoolEmail("congthinh.nguyen@prf.byteacademy.com"))
                .thenReturn(Optional.empty());

        // 3. Call method
        String email = instructorProfileService.generateUniqueSchoolEmail(firstName, lastName);

        // 4. Assert
        assertEquals("congthinh.nguyen@prf.byteacademy.com", email);

        // 5. Verify repository call
        verify(instructorProfileRepository).findBySchoolEmail("congthinh.nguyen@prf.byteacademy.com");
    }

    @Test
    void testGenerateUniqueSchoolEmail_CollisionResolved() {
        // 1. Prepare names
        String firstName = "Cong Thinh";
        String lastName = "Nguyen";

        // 2. Mock repository: first two emails exist
        when(instructorProfileRepository.findBySchoolEmail("congthinh.nguyen@prf.byteacademy.com"))
                .thenReturn(Optional.of(new InstructorProfile()));
        when(instructorProfileRepository.findBySchoolEmail("congthinh.nguyen1@prf.byteacademy.com"))
                .thenReturn(Optional.of(new InstructorProfile()));
        when(instructorProfileRepository.findBySchoolEmail("congthinh.nguyen2@prf.byteacademy.com"))
                .thenReturn(Optional.empty());

        // 3. Call method
        String email = instructorProfileService.generateUniqueSchoolEmail(firstName, lastName);

        // 4. Assert
        assertEquals("congthinh.nguyen2@prf.byteacademy.com", email);

        // 5. Verify repository calls
        verify(instructorProfileRepository).findBySchoolEmail("congthinh.nguyen@prf.byteacademy.com");
        verify(instructorProfileRepository).findBySchoolEmail("congthinh.nguyen1@prf.byteacademy.com");
        verify(instructorProfileRepository).findBySchoolEmail("congthinh.nguyen2@prf.byteacademy.com");
    }


}
