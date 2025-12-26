package com.campus_resource_management.instructorservice.controller;

import com.campus_resource_management.instructorservice.dto.PaginationResponse;
import com.campus_resource_management.instructorservice.dto.ServiceResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import com.campus_resource_management.instructorservice.service.InstructorProfileService;
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
public class InstructorProfileControllerTest {

    @Mock
    private InstructorProfileService instructorProfileService;

    @InjectMocks
    private InstructorProfileController instructorProfileController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(instructorProfileController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddInstructorProfile_ValidRequest() throws Exception {
        // 1. Create a fully populated mock AddInstructorProfileRequest
        AddInstructorProfileRequest request = AddInstructorProfileRequest.builder()
                .identityId("086207150707")
                .firstName("firstName")
                .lastName("lastName")
                .gender("gender")
                .email("email")
                .department("department")
                .employmentStatus("employmentStatus")
                .phoneNumber("phoneNumber")
                .dateOfBirth(LocalDate.now())
                .officeLocation("officeLocation")
                .phoneNumber("0912345678")
                .hireDate(LocalDate.now())
                .salaryBand("salaryBand")
                .officeHours("officeHours")
                .instructorNote("instructorNote")
                .build();

        // 2. Mock service response
        SummaryInstructorProfileResponse responseDto = SummaryInstructorProfileResponse.builder().build();
        when(instructorProfileService.addInstructorProfile(any()))
                .thenReturn(ServiceResponse.<SummaryInstructorProfileResponse>builder().data(responseDto).build());

        // 3. Perform POST request
        mockMvc.perform(post("/instructor-profile/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testUpdateInstructorProfile_ValidRequest() throws Exception {
        // 1. Create a fully populated mock UpdateInstructorProfileRequest
        UpdateInstructorProfileRequest request = UpdateInstructorProfileRequest.builder()
                .identityId("086207150707")
                .firstName("firstName")
                .lastName("lastName")
                .gender("gender")
                .email("email")
                .department("department")
                .employmentStatus("employmentStatus")
                .phoneNumber("phoneNumber")
                .dateOfBirth(LocalDate.now())
                .officeLocation("officeLocation")
                .phoneNumber("0912345678")
                .hireDate(LocalDate.now())
                .salaryBand("salaryBand")
                .officeHours("officeHours")
                .instructorNote("instructorNote")
                .build();

        // 2. Mock service response
        SummaryInstructorProfileResponse responseDto = SummaryInstructorProfileResponse.builder().build();
        when(instructorProfileService.updateInstructorProfile(any()))
                .thenReturn(ServiceResponse.<SummaryInstructorProfileResponse>builder().data(responseDto).build());

        // 3. Perform POST request
        mockMvc.perform(post("/instructor-profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testViewAllInstructorProfile() throws Exception {
        FilterInstructorProfileRequest request = new FilterInstructorProfileRequest();
        PaginationResponse<FilterInstructorProfileResponse> pagination = PaginationResponse.<FilterInstructorProfileResponse>builder()
                .listData(List.of()).build();

        when(instructorProfileService.viewFilteredInstructorProfile(any())).thenReturn(
                ServiceResponse.<PaginationResponse<FilterInstructorProfileResponse>>builder()
                        .data(pagination).build()
        );

        mockMvc.perform(get("/instructor-profile/viewAll")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testViewDetailedInstructorProfileByIdentityId() throws Exception {
        String identityId = "1";
        DetailedInstructorProfileResponse response = DetailedInstructorProfileResponse.builder().build();
        when(instructorProfileService.viewDetailedInstructorProfileByIdentityId(identityId))
                .thenReturn(ServiceResponse.<DetailedInstructorProfileResponse>builder().data(response).build());

        mockMvc.perform(get("/instructor-profile")
                        .param("identityId", identityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testDeleteInstructorProfile() throws Exception {
        String identityId = "1";
        when(instructorProfileService.deleteInstructorProfileByIdentityId(identityId))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(delete("/instructor-profile/delete")
                        .param("identityId", identityId))
                .andExpect(status().isOk());
    }

    @Test
    void testRestoreInstructorProfile() throws Exception {
        String identityId = "1";
        when(instructorProfileService.restoreInstructorProfile(identityId))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(put("/instructor-profile/restore")
                        .param("identityId", identityId))
                .andExpect(status().isOk());
    }

}
