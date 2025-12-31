package com.campus_resource_management.courseservice.course_offering.controller;

import com.campus_resource_management.courseservice.controller.CourseOfferingController;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.service.CourseOfferingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseOfferingControllerTest {

    @Mock
    private CourseOfferingService courseOfferingService;

    private CourseOfferingController courseOfferingController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        courseOfferingController =
                new CourseOfferingController(courseOfferingService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(courseOfferingController)
                .build();
        objectMapper = new ObjectMapper();
    }

    // ===================== ADD =====================
    @Test
    void addCourseOffering_success() throws Exception {
        AddCourseOfferingRequest request = AddCourseOfferingRequest.builder()
                .courseCode("CSC101")
                .term("FALL")
                .year(2025)
                .section("A")
                .maxStudents(60)
                .instructorIdentityId("INST00001234")
                .build();

        when(courseOfferingService.addCourseOffering(any()))
                .thenReturn(ServiceResponse.<SummaryCourseOfferingResponse>builder().build());

        mockMvc.perform(post("/course-offering/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ===================== UPDATE =====================
    @Test
    void updateCourseOffering_success() throws Exception {
        UpdateCourseOfferingRequest request = UpdateCourseOfferingRequest.builder()
                .courseOfferingCode("CSC101_F25_A")
                .maxStudents(80)
                .instructorIdentityId("INST00001234")
                .build();

        when(courseOfferingService.updateCourseOffering(any()))
                .thenReturn(ServiceResponse.<SummaryCourseOfferingResponse>builder().build());

        mockMvc.perform(post("/course-offering/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ===================== VIEW ALL =====================
    @Test
    void viewAllCourseOfferings_success() throws Exception {
        PaginationResponse<SummaryCourseOfferingResponse> pagination =
                PaginationResponse.<SummaryCourseOfferingResponse>builder()
                        .listData(List.of())
                        .build();

        when(courseOfferingService.filterCourseOfferings(any()))
                .thenReturn(ServiceResponse.<PaginationResponse<SummaryCourseOfferingResponse>>builder()
                        .data(pagination)
                        .build());

        mockMvc.perform(get("/course-offering/viewAll")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    // ===================== VIEW DETAIL =====================
    @Test
    void viewDetailCourseOffering_success() throws Exception {
        when(courseOfferingService.viewDetailedCourseOfferingByCode("CSC101_F25_A"))
                .thenReturn(ServiceResponse.<DetailedCourseOfferingResponse>builder().build());

        mockMvc.perform(get("/course-offering")
                        .param("courseOfferingCode", "CSC101_F25_A"))
                .andExpect(status().isOk());
    }

    // ===================== DELETE =====================
    @Test
    void deleteCourseOffering_success() throws Exception {
        when(courseOfferingService.deleteCourseOfferingByOfferingCode("CSC101_F25_A"))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(delete("/course-offering/delete")
                        .param("courseOfferingCode", "CSC101_F25_A"))
                .andExpect(status().isOk());
    }

    // ===================== RESTORE =====================
    @Test
    void restoreCourseOffering_success() throws Exception {
        when(courseOfferingService.restoreCourseOfferingByOfferingCode("CSC101_F25_A"))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(put("/course-offering/restore")
                        .param("courseOfferingCode", "CSC101_F25_A"))
                .andExpect(status().isOk());
    }
}
