package com.campus_resource_management.studentservice.controller;

import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.FilterStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.service.StudentProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentProfileControllerTest {

    @Mock
    private StudentProfileService studentProfileService;

    @InjectMocks
    private StudentProfileController studentProfileController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddStudentProfile_ValidRequest() throws Exception {
        // 1. Create a fully populated mock AddStudentProfileRequest
        AddStudentProfileRequest request = AddStudentProfileRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")          // Must be Gmail
                .identityId("080304146228")           // Must pass @ValidIdentityId
                .gender("MALE")                        // Assuming enum or string matches allowed
                .phoneNumber("0961234567")             // Must match allowed prefixes
                .major("Computer Science")
                .degreeType("BACHELOR")                // Must match enum/pattern
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        // 2. Mock service response
        SummaryStudentProfileResponse responseDto = SummaryStudentProfileResponse.builder().build();
        when(studentProfileService.addStudentProfile(any()))
                .thenReturn(ServiceResponse.<SummaryStudentProfileResponse>builder().data(responseDto).build());

        // 3. Perform POST request
        mockMvc.perform(post("/student-profile/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testUpdateStudentProfile_ValidRequest() throws Exception {
        // 1. Create a fully populated mock UpdateStudentProfileRequest
        UpdateStudentProfileRequest request = UpdateStudentProfileRequest.builder()
                .identityId("080304146228")           // Must pass @ValidIdentityId
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@gmail.com")      // Gmail required
                .phoneNumber("0961234567")           // Valid phone prefix
                .gender("FEMALE")
                .major("Mathematics")
                .degreeType("BACHELOR")              // Must match pattern
                .dateOfBirth(LocalDate.of(2001, 5, 20))
                .build();

        // 2. Mock service response
        SummaryStudentProfileResponse responseDto = SummaryStudentProfileResponse.builder().build();
        when(studentProfileService.updateStudentProfile(any()))
                .thenReturn(ServiceResponse.<SummaryStudentProfileResponse>builder().data(responseDto).build());

        // 3. Perform POST request
        mockMvc.perform(post("/student-profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testViewAllStudentProfile() throws Exception {
        FilterStudentProfileRequest request = new FilterStudentProfileRequest();
        PaginationResponse<FilterStudentProfileResponse> pagination = PaginationResponse.<FilterStudentProfileResponse>builder()
                .listData(List.of()).build();

        when(studentProfileService.viewFilteredStudentProfile(any())).thenReturn(
                ServiceResponse.<PaginationResponse<FilterStudentProfileResponse>>builder()
                        .data(pagination).build()
        );

        mockMvc.perform(get("/student-profile/viewAll")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testViewDetailedStudentProfileByIdentityId() throws Exception {
        String identityId = "1";
        DetailedStudentProfileResponse response = DetailedStudentProfileResponse.builder().build();
        when(studentProfileService.viewDetailedStudentProfileByIdentityId(identityId))
                .thenReturn(ServiceResponse.<DetailedStudentProfileResponse>builder().data(response).build());

        mockMvc.perform(get("/student-profile")
                        .param("identityId", identityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testDeleteStudentProfile() throws Exception {
        String identityId = "1";
        when(studentProfileService.deleteStudentProfileByIdentityId(identityId))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(delete("/student-profile/delete")
                        .param("identityId", identityId))
                .andExpect(status().isOk());
    }

    @Test
    void testRestoreStudentProfile() throws Exception {
        String identityId = "1";
        when(studentProfileService.restoreStudentProfile(identityId))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(put("/student-profile/restore")
                        .param("identityId", identityId))
                .andExpect(status().isOk());
    }

}
