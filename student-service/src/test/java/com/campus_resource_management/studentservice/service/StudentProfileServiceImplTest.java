package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.constant.MessageResponse;
import com.campus_resource_management.studentservice.constant.StatusCode;
import com.campus_resource_management.studentservice.constant.StatusResponse;
import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.FilterStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.exception.*;
import com.campus_resource_management.studentservice.mapper.StudentProfileMapper;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
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
public class StudentProfileServiceImplTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private StudentProfileMapper studentProfileMapper;

    @InjectMocks
    private StudentProfileServiceImpl studentProfileService;

    @Test
    void testAddStudentProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request DTO
        AddStudentProfileRequest request = new AddStudentProfileRequest();
        request.setFirstName("Cong Thinh");
        request.setLastName("Nguyen");

        // 2. Prepare mocked saved entity and response DTO
        StudentProfile savedProfile = new StudentProfile();
        savedProfile.setIdentityId("id-123");

        SummaryStudentProfileResponse summaryResponse = new SummaryStudentProfileResponse();
        summaryResponse.setIdentityId("id-123");

        // 3. Mock mapper behavior for mapping request -> entity
        doNothing().when(studentProfileMapper)
                .addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));

        // 4. Mock repository save
        when(studentProfileRepository.save(any(StudentProfile.class))).thenReturn(savedProfile);

        // 5. Mock mapper behavior for entity -> summary DTO
        when(studentProfileMapper.toSummaryResponse(any(StudentProfile.class))).thenReturn(summaryResponse);

        // 6. Call the service method
        ServiceResponse<SummaryStudentProfileResponse> serviceResponse =
                studentProfileService.addStudentProfile(request);

        // 7. Assertions
        assertEquals(StatusCode.CREATED, serviceResponse.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, serviceResponse.getStatus());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.ADD_STUDENT_PROFILE_SUCCESS));
        assertEquals(summaryResponse, serviceResponse.getData());

        // 8. Verify interactions
        verify(studentProfileMapper).addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));
        verify(studentProfileRepository).save(any(StudentProfile.class));
        verify(studentProfileMapper).toSummaryResponse(any(StudentProfile.class));
    }

    @Test
    void testAddStudentProfile_error_RepositoryThrowsException(){
        // 1. Prepare request DTO
        AddStudentProfileRequest request = new AddStudentProfileRequest();
        request.setFirstName("Cong Thinh");
        request.setLastName("Nguyen");


        // 2. Mock mapper to do nothing
        doNothing().when(studentProfileMapper)
                .addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));

        // 3. Mock repository to throw exception
        when(studentProfileRepository.save(any(StudentProfile.class)))
                .thenThrow(new RuntimeException());

        // 4. Call service method and assert exception
        Exception exception = assertThrows(RuntimeException.class, () ->
                studentProfileService.addStudentProfile(request));

        assertNotNull(exception);

        // 5. Verify mapper was called, repository was called
        verify(studentProfileMapper).addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));
        verify(studentProfileRepository).save(any(StudentProfile.class));
    }

    @Test
    void testAddStudentProfile_error_MapperThrowsException(){
        // 1. Prepare request DTO
        AddStudentProfileRequest request = new AddStudentProfileRequest();
        request.setFirstName("Nguyen");
        request.setLastName("Cong Thinh");

        // 2. Mock mapper to throw exception
        doThrow(new IllegalArgumentException("Invalid data"))
                .when(studentProfileMapper)
                .addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));

        // 3. Assert that exception is propagated
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                studentProfileService.addStudentProfile(request));

        assertTrue(exception.getMessage().contains("Invalid data"));

        // 4. Verify mapper was called
        verify(studentProfileMapper).addStudentProfileRequestBodyToStudentProfile(any(AddStudentProfileRequest.class), any(StudentProfile.class));
        // 5. Repository should never be called because mapper failed
        verifyNoInteractions(studentProfileRepository);
    }

    @Test
    void testUpdateStudentProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request DTO
        Long profileId = 1L;
        UpdateStudentProfileRequest request = UpdateStudentProfileRequest.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 2. Prepare existing entity and updated entity
        StudentProfile existingProfile = StudentProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Old Name")
                .lastName("Kiyotaka")
                .email("kudokazuto06@gmail.com")
                .build();

        StudentProfile updatedProfile = StudentProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 3. Prepare expected response DTO
        SummaryStudentProfileResponse responseDto = SummaryStudentProfileResponse.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("thinhnguyen@gmail.com")
                .build();

        // 4. Mock repository + mapper behaviour
        when(studentProfileRepository.findByIdentityId(String.valueOf(profileId)))
                .thenReturn(Optional.of(existingProfile));

        doNothing().when(studentProfileMapper)
                .updateStudentProfileRequestBodyToStudentProfile(any(UpdateStudentProfileRequest.class), eq(existingProfile));

        when(studentProfileRepository.save(existingProfile))
                .thenReturn(updatedProfile);

        when(studentProfileMapper.toSummaryResponse(updatedProfile))
                .thenReturn(responseDto);

        // 5. Call the service method
        ServiceResponse<SummaryStudentProfileResponse> serviceResponse =
                studentProfileService.updateStudentProfile(request);

        // 6. Verify returned response
        assertEquals(StatusCode.SUCCESS, serviceResponse.getStatusCode());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.UPDATE_STUDENT_PROFILE_SUCCESS));
        assertEquals(responseDto, serviceResponse.getData());

        // 7. Verify interactions with mocks
        verify(studentProfileRepository).findByIdentityId(String.valueOf(profileId));
        verify(studentProfileMapper).updateStudentProfileRequestBodyToStudentProfile(request, existingProfile);
        verify(studentProfileRepository).save(existingProfile);
        verify(studentProfileMapper).toSummaryResponse(updatedProfile);
    }

    @Test
    void testUpdateStudentProfile_error_StudentProfileNotFound() {
        // 1. Prepare request with non-existent ID
        Long identityId = 1L;
        UpdateStudentProfileRequest request = UpdateStudentProfileRequest.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        // 2. Mock repository to return empty (simulate not found in database)
        when(studentProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.empty());

        // 3. Call service and expect exception
        StudentProfileNotFoundException ex = assertThrows(StudentProfileNotFoundException.class, () -> {
            studentProfileService.updateStudentProfile(request);
        });

        // 4. Verify repository interaction
        verify(studentProfileRepository).findByIdentityId(String.valueOf(identityId));
        assertEquals("Student profile not found with id: " + identityId, ex.getMessage());

        // 5. Ensure no save or mapper was called
        verify(studentProfileRepository, never()).save(any());
        verify(studentProfileMapper, never()).toSummaryResponse(any());
    }

    @Test
    void testUpdateStudentProfile_error_RepositoryThrowsException() {
        // 1. Prepare request and existing profile
        Long identityId = 1L;
        UpdateStudentProfileRequest request = UpdateStudentProfileRequest.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        StudentProfile existingProfile = StudentProfile.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Old")
                .lastName("Name")
                .build();

        // 2. Mock repository and mapper
        when(studentProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.of(existingProfile));

        doNothing().when(studentProfileMapper)
                .updateStudentProfileRequestBodyToStudentProfile(any(UpdateStudentProfileRequest.class), eq(existingProfile));

        // Simulate database save failure
        when(studentProfileRepository.save(existingProfile))
                .thenThrow(new RuntimeException("Database write error"));

        // 3. Call service and expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentProfileService.updateStudentProfile(request);
        });

        // 4. Verify exception message
        assertEquals("Database write error", exception.getMessage());

        // 5. Verify repository interactions (DB hit and save attempt)
        verify(studentProfileRepository).findByIdentityId(String.valueOf(identityId));
        verify(studentProfileRepository).save(existingProfile);
    }

    @Test
    void testDeleteStudentProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare request
        Long profileId = 1L;

        // 2. Prepare existing student profile
        StudentProfile existingProfile = StudentProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        StudentProfile deletedProfile = StudentProfile.builder()
                .identityId(String.valueOf(profileId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. Mock repository
        when(studentProfileRepository.findByIdentityId(String.valueOf(profileId)))
            .thenReturn(Optional.of(existingProfile));

        when(studentProfileRepository.save(existingProfile))
            .thenReturn(deletedProfile);

        // 4. Call service method
        ServiceResponse<Void> response = studentProfileService.deleteStudentProfileByIdentityId(String.valueOf(profileId));

        // 5. Assert successful response
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.DELETE_STUDENT_PROFILE_SUCCESS));

        // 6. Verify repository interactions (simulate DB)
        verify(studentProfileRepository).findByIdentityId(String.valueOf(profileId));
        verify(studentProfileRepository).save(existingProfile);

    }

    @Test
    void testDeleteStudentProfile_error_StudentProfileNotFound() {
        // 1. Prepare input
        Long identityId = 1L;

        // 2. Mock repository to return empty
        when(studentProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.empty());

        // 3. Call service and expect not found exception
        assertThrows(StudentProfileNotFoundException.class, () -> {
            studentProfileService.deleteStudentProfileByIdentityId(String.valueOf(identityId));
        });

        // 4. Verify repository check
        verify(studentProfileRepository).findByIdentityId(String.valueOf(identityId));

        // 5. Verify save was never called
        verify(studentProfileRepository, never()).save(any());
    }

    @Test
    void testDeleteStudentProfile_error_RepositoryThrowsException() {
        // 1. Prepare input
        Long identityId = 1L;

        // 2. Prepare existing profile
        StudentProfile existingProfile = StudentProfile.builder()
                .identityId(String.valueOf(identityId))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        // 3. Mock repository to simulate DB error on save
        when(studentProfileRepository.findByIdentityId(String.valueOf(identityId)))
                .thenReturn(Optional.of(existingProfile));

        when(studentProfileRepository.save(existingProfile))
                .thenThrow(new RuntimeException("Database write failure"));

        // 4. Call service and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentProfileService.deleteStudentProfileByIdentityId(String.valueOf(identityId));
        });

        // 5. Check message and interactions
        assertEquals("Database write failure", exception.getMessage());
        verify(studentProfileRepository).findByIdentityId(String.valueOf(identityId));
        verify(studentProfileRepository).save(existingProfile);
    }

    @Test
    void testRestoreStudentProfile_success_shouldReturnSummaryResponse() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare a soft-deleted student profile
        StudentProfile deletedProfile = StudentProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. What we expect after restoring
        StudentProfile restoredProfile = StudentProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(false)
                .build();

        // 4. Mock repository behavior
        when(studentProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.of(deletedProfile));
        when(studentProfileRepository.save(any(StudentProfile.class)))
                .thenReturn(restoredProfile);

        // 5. Call service method
        ServiceResponse<Void> response = studentProfileService.restoreStudentProfile(identityId);

        // 6. Assert response is successful
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.RESTORE_STUDENT_PROFILE_SUCCESS));

        // 7. Verify repository interactions
        verify(studentProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(studentProfileRepository).save(deletedProfile);
    }

    @Test
    void testRestoreStudentProfile_error_StudentProfileNotFound() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Mock repository to return empty (no such student)
        when(studentProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.empty());

        // 3. Expect StudentProfileNotFoundException
        assertThrows(StudentProfileNotFoundException.class, () -> {
            studentProfileService.restoreStudentProfile(identityId);
        });

        // 4. Verify repository calls
        verify(studentProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(studentProfileRepository, never()).save(any());
    }

    @Test
    void testRestoreStudentProfile_error_RepositoryThrowsException() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare existing deleted profile
        StudentProfile deletedProfile = StudentProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .isDeleted(true)
                .build();

        // 3. Mock repository to throw DB error when saving
        when(studentProfileRepository.findByIdentityIdIncludeSoftDeleted(identityId))
                .thenReturn(Optional.of(deletedProfile));

        when(studentProfileRepository.save(deletedProfile))
                .thenThrow(new RuntimeException("Database error while restoring"));

        // 4. Assert the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentProfileService.restoreStudentProfile(identityId);
        });

        // 5. Verify message and repository calls
        assertEquals("Database error while restoring", exception.getMessage());
        verify(studentProfileRepository).findByIdentityIdIncludeSoftDeleted(identityId);
        verify(studentProfileRepository).save(deletedProfile);
    }

    @Test
    void testViewDetailedStudentProfileByIdentityId_success_shouldReturnDetailResponse() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Prepare existing student profile
        StudentProfile studentProfile = StudentProfile.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("congthinh.nguyen@gmail.com")
                .build();

        DetailedStudentProfileResponse responseDto = DetailedStudentProfileResponse.builder()
                .identityId(identityId)
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .email("congthinh.nguyen@gmail.com")
                .build();

        // 3. Mock repository and mapper
        when(studentProfileRepository.findByIdentityId(identityId))
                .thenReturn(Optional.of(studentProfile));

        when(studentProfileMapper.toStudentProfileResponse(studentProfile))
                .thenReturn(responseDto);

        // 4. Call service method
        ServiceResponse<DetailedStudentProfileResponse> serviceResponse =
                studentProfileService.viewDetailedStudentProfileByIdentityId(identityId);

        // 5. Assert response
        assertEquals(StatusCode.SUCCESS, serviceResponse.getStatusCode());
        assertTrue(serviceResponse.getMessage().contains(MessageResponse.VIEW_DETAIL_STUDENT_PROFILE_SUCCESS));
        assertEquals(responseDto, serviceResponse.getData());

        // 6. Verify repository and mapper interactions
        verify(studentProfileRepository).findByIdentityId(identityId);
        verify(studentProfileMapper).toStudentProfileResponse(studentProfile);
    }

    @Test
    void testViewDetailedStudentProfileByIdentityId_error_StudentProfileNotFound() {
        // 1. Prepare identityId
        Long profileId = 1L;
        String identityId = String.valueOf(profileId);

        // 2. Mock repository to return empty
        when(studentProfileRepository.findByIdentityId(identityId))
                .thenReturn(Optional.empty());

        // 3. Expect StudentProfileNotFoundException
        assertThrows(StudentProfileNotFoundException.class, () -> {
            studentProfileService.viewDetailedStudentProfileByIdentityId(identityId);
        });

        // 4. Verify repository interaction
        verify(studentProfileRepository).findByIdentityId(identityId);
        verify(studentProfileMapper, never()).toStudentProfileResponse(any());
    }

    @Test
    void testViewDetailedStudentProfileByIdentityId_error_RepositoryThrowsException() {
        // 1. Prepare identityId
        Long identityId = 1L;
        String identityIdStr = String.valueOf(identityId);

        // 2. Mock repository to throw exception
        when(studentProfileRepository.findByIdentityId(identityIdStr))
                .thenThrow(new RuntimeException("Database error"));

        // 3. Call service and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentProfileService.viewDetailedStudentProfileByIdentityId(identityIdStr);
        });

        // 4. Assert exception message
        assertEquals("Database error", exception.getMessage());

        // 5. Verify repository call
        verify(studentProfileRepository).findByIdentityId(identityIdStr);
        verify(studentProfileMapper, never()).toStudentProfileResponse(any());
    }

    @Test
    void testViewFilteredStudentProfile_success1() {
        // 1. Prepare filter request
        FilterStudentProfileRequest filterRequest = FilterStudentProfileRequest.builder()
                .page(0)
                .size(5)
                .sortBy("firstName asc")
                .build();

        // 2. Prepare repository return
        StudentProfile studentProfile = StudentProfile.builder()
                .identityId(String.valueOf(1L))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        List<StudentProfile> profileList = List.of(studentProfile);
        Page<StudentProfile> pageResult = new PageImpl<>(profileList);

        when(studentProfileRepository.filterStudentProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.asc("firstName")))))
                .thenReturn(pageResult);

        // 3. Prepare mapper
        FilterStudentProfileResponse mappedResponse = FilterStudentProfileResponse.builder()
                .identityId(String.valueOf(1L))
                .fullName("Cong Thinh Nguyen")
                .build();

        when(studentProfileMapper.toFilterResponse(studentProfile))
                .thenReturn(mappedResponse);

        // 4. Call service
        ServiceResponse<PaginationResponse<FilterStudentProfileResponse>> response =
                studentProfileService.viewFilteredStudentProfile(filterRequest);

        // 5. Assert response
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertTrue(response.getMessage().contains(MessageResponse.VIEW_ALL_STUDENT_PROFILE_SUCCESS));

        PaginationResponse<FilterStudentProfileResponse> pagination = response.getData();
        assertNotNull(pagination);
        assertEquals(1, pagination.getTotalItems());
        assertEquals(1, pagination.getTotalPages());
        assertEquals(0, pagination.getCurrentPage());
        assertEquals(mappedResponse, pagination.getListData().get(0));

        // 6. Verify interactions
        verify(studentProfileRepository).filterStudentProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.asc("firstName"))));
        verify(studentProfileMapper).toFilterResponse(studentProfile);
    }

    @Test
    void testViewFilteredStudentProfile_success2_DefaultPageSize() {
        // 1. Filter request with null page & size
        FilterStudentProfileRequest filterRequest = FilterStudentProfileRequest.builder()
                .sortBy("firstName desc")
                .build();

        StudentProfile studentProfile = StudentProfile.builder()
                .identityId(String.valueOf(1L))
                .firstName("Cong Thinh")
                .lastName("Nguyen")
                .build();

        Page<StudentProfile> pageResult = new PageImpl<>(List.of(studentProfile));

        // 2. Mock repository
        when(studentProfileRepository.filterStudentProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.desc("firstName")))))
                .thenReturn(pageResult);

        // 3. Mapper
        FilterStudentProfileResponse mappedResponse = FilterStudentProfileResponse.builder()
                .identityId(String.valueOf(1L))
                .fullName("Cong Thinh Nguyen")
                .build();

        when(studentProfileMapper.toFilterResponse(studentProfile)).thenReturn(mappedResponse);

        // 4. Call service
        ServiceResponse<PaginationResponse<FilterStudentProfileResponse>> response =
                studentProfileService.viewFilteredStudentProfile(filterRequest);

        // 5. Assert
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(1, response.getData().getTotalItems());
        assertEquals(mappedResponse, response.getData().getListData().get(0));

        // 6. Verify
        verify(studentProfileRepository).filterStudentProfile(filterRequest,
                PageRequest.of(0,5, Sort.by(Sort.Order.desc("firstName"))));
        verify(studentProfileMapper).toFilterResponse(studentProfile);
    }

    @Test
    void testViewFilteredStudentProfile_success3_ReturnEmptyList() {
        // 1. Filter request
        FilterStudentProfileRequest filterRequest = FilterStudentProfileRequest.builder()
                .page(0)
                .size(5)
                .build();

        // 2. Mock empty page
        when(studentProfileRepository.filterStudentProfile(any(FilterStudentProfileRequest.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        // 3. Call service and expect exception
        assertThrows(ListEmptyException.class, () -> {
            studentProfileService.viewFilteredStudentProfile(filterRequest);
        });

        // 4. Verify repository called
        verify(studentProfileRepository).filterStudentProfile(any(FilterStudentProfileRequest.class), any(Pageable.class));
        verifyNoMoreInteractions(studentProfileMapper);
    }

    @Test
    void testViewFilteredStudentProfile_error_RepositoryThrowException() {
        // 1. Filter request
        FilterStudentProfileRequest filterRequest = FilterStudentProfileRequest.builder()
                .page(0)
                .size(5)
                .build();

        // 2. Mock repository to throw
        when(studentProfileRepository.filterStudentProfile(any(FilterStudentProfileRequest.class), any(Pageable.class)))
                .thenThrow(new RuntimeException("Database failure"));

        // 3. Call service and assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentProfileService.viewFilteredStudentProfile(filterRequest);
        });

        // 4. Check message
        assertEquals("Database failure", exception.getMessage());

        // 5. Verify repository
        verify(studentProfileRepository).filterStudentProfile(any(FilterStudentProfileRequest.class), any(Pageable.class));
        verifyNoInteractions(studentProfileMapper);
    }

    @Test
    void testGenerateUniqueSchoolEmail_Unique() {
        // 1. Prepare names
        String firstName = "Cong Thinh";
        String lastName = "Nguyen";

        // 2. Mock repository: no existing email
        when(studentProfileRepository.findBySchoolEmail("congthinh.nguyen@school.com"))
                .thenReturn(Optional.empty());

        // 3. Call method
        String email = studentProfileService.generateUniqueSchoolEmail(firstName, lastName);

        // 4. Assert
        assertEquals("congthinh.nguyen@school.com", email);

        // 5. Verify repository call
        verify(studentProfileRepository).findBySchoolEmail("congthinh.nguyen@school.com");
    }

    @Test
    void testGenerateUniqueSchoolEmail_CollisionResolved() {
        // 1. Prepare names
        String firstName = "Cong Thinh";
        String lastName = "Nguyen";

        // 2. Mock repository: first two emails exist
        when(studentProfileRepository.findBySchoolEmail("congthinh.nguyen@school.com"))
                .thenReturn(Optional.of(new StudentProfile()));
        when(studentProfileRepository.findBySchoolEmail("congthinh.nguyen1@school.com"))
                .thenReturn(Optional.of(new StudentProfile()));
        when(studentProfileRepository.findBySchoolEmail("congthinh.nguyen2@school.com"))
                .thenReturn(Optional.empty());

        // 3. Call method
        String email = studentProfileService.generateUniqueSchoolEmail(firstName, lastName);

        // 4. Assert
        assertEquals("congthinh.nguyen2@school.com", email);

        // 5. Verify repository calls
        verify(studentProfileRepository).findBySchoolEmail("congthinh.nguyen@school.com");
        verify(studentProfileRepository).findBySchoolEmail("congthinh.nguyen1@school.com");
        verify(studentProfileRepository).findBySchoolEmail("congthinh.nguyen2@school.com");
    }


}
