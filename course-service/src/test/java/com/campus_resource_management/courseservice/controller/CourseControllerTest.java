package com.campus_resource_management.courseservice.controller;

import com.campus_resource_management.courseservice.controller.CourseController;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.exception.CourseNotFoundException;
import com.campus_resource_management.courseservice.service.CourseEnrollmentService;
import com.campus_resource_management.courseservice.service.CourseOfferingService;
import com.campus_resource_management.courseservice.service.CourseService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseEnrollmentService courseEnrollmentService;

    @Mock
    private CourseOfferingService courseOfferingService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CourseController courseController;
    private CourseEnrollmentController courseEnrollmentController;
    private CourseOfferingController courseOfferingController;

    @BeforeEach
    void setUp() {
        courseController = new CourseController(courseService);
        courseEnrollmentController = new CourseEnrollmentController(courseEnrollmentService);
        courseOfferingController = new CourseOfferingController(courseOfferingService);

        mockMvc = MockMvcBuilders.standaloneSetup(
                courseController,
                courseEnrollmentController,
                courseOfferingController
        ).build();

        objectMapper = new ObjectMapper();
    }

    // ===================== ADD =====================
    @Test
    void addCourse_success() throws Exception {
        AddCourseRequest request = AddCourseRequest.builder()
                .courseCode("101")
                .courseName("Intro to CS")
                .department("COMPUTER_SCIENCE")
                .credit(3.0)
                .description("desc")
                .build();

        when(courseService.addCourse(any()))
                .thenReturn(ServiceResponse.<SummaryCourseResponse>builder().build());

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ===================== UPDATE =====================
    @Test
    void updateCourse_success() throws Exception {
        UpdateCourseRequest request = UpdateCourseRequest.builder()
                .courseCode("CSC101")
                .courseName("Updated")
                .department("COMPUTER_SCIENCE")
                .credit(4.0)
                .build();

        when(courseService.updateCourse(any()))
                .thenReturn(ServiceResponse.<SummaryCourseResponse>builder().build());

        mockMvc.perform(post("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ===================== VIEW ALL =====================
    @Test
    void viewAllCourses_success() throws Exception {
        PaginationResponse<SummaryCourseResponse> pagination =
                PaginationResponse.<SummaryCourseResponse>builder()
                        .listData(List.of())
                        .build();

        when(courseService.viewFilteredCourses(any()))
                .thenReturn(ServiceResponse.<PaginationResponse<SummaryCourseResponse>>builder()
                        .data(pagination)
                        .build());

        mockMvc.perform(get("/course/viewAll")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    // ===================== VIEW DETAIL =====================
    @Test
    void viewDetail_success() throws Exception {
        when(courseService.viewDetailedCourseByCourseCode("CSC201"))
                .thenReturn(ServiceResponse.<DetailedCourseResponse>builder().build());

        mockMvc.perform(get("/course")
                        .param("courseCode", "CSC201"))
                .andExpect(status().isOk());
    }

    // ===================== DELETE SUCCESS =====================
    @Test
    void deleteCourse_success() throws Exception {
        when(courseService.deleteCourseByCourseCode("CSC201"))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(delete("/course/delete")
                        .param("courseCode", "CSC201"))
                .andExpect(status().isOk());
    }

    // ===================== DELETE NOT FOUND =====================
    @Test
    void deleteCourse_notFound() {
        when(courseService.deleteCourseByCourseCode("CSC999"))
                .thenThrow(new CourseNotFoundException("CSC999"));

        assertThrows(CourseNotFoundException.class, () ->
                courseController.deleteCourse("CSC999"));
    }

    // ===================== RESTORE =====================
    @Test
    void restoreCourse_success() throws Exception {
        when(courseService.restoreCourseByCourseCode("CSC201"))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(put("/course/restore")
                        .param("courseCode", "CSC201"))
                .andExpect(status().isOk());
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

    // ===================== ADD =====================
    @Test
    void addCourseEnrollment_success() throws Exception {
        AddCourseEnrollmentRequest request = AddCourseEnrollmentRequest.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .build();

        when(courseEnrollmentService.addCourseEnrollment(any()))
                .thenReturn(ServiceResponse.<SummaryCourseEnrollmentResponse>builder().build());

        mockMvc.perform(post("/course-enrollment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ===================== UPDATE =====================
    @Test
    void updateCourseEnrollment_success() throws Exception {
        UpdateCourseEnrollmentRequest request = UpdateCourseEnrollmentRequest.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .finalGrade(85.0)
                .letterGrade("A")
                .build();

        when(courseEnrollmentService.updateCourseEnrollment(any()))
                .thenReturn(ServiceResponse.<SummaryCourseEnrollmentResponse>builder().build());

        mockMvc.perform(post("/course-enrollment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ===================== WITHDRAW =====================
    @Test
    void withdrawCourseEnrollment_success() throws Exception {
        when(courseEnrollmentService.withdrawCourseEnrollment(
                "CSC101_F25_A", "STU000012345"))
                .thenReturn(ServiceResponse.<Void>builder().build());

        mockMvc.perform(post("/course-enrollment/withdraw")
                        .param("offeringCode", "CSC101_F25_A")
                        .param("studentIdentityId", "STU000012345"))
                .andExpect(status().isOk());
    }

    // ===================== VIEW ALL =====================
    @Test
    void viewAllCourseEnrollments_success() throws Exception {
        PaginationResponse<SummaryCourseEnrollmentResponse> pagination =
                PaginationResponse.<SummaryCourseEnrollmentResponse>builder()
                        .listData(List.of())
                        .build();

        when(courseEnrollmentService.filterCourseEnrollments(any()))
                .thenReturn(ServiceResponse.<PaginationResponse<SummaryCourseEnrollmentResponse>>builder()
                        .data(pagination)
                        .build());

        mockMvc.perform(get("/course-enrollment/viewAll")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    // ===================== VIEW DETAIL =====================
    @Test
    void viewDetailedCourseEnrollment_success() throws Exception {
        when(courseEnrollmentService.viewDetailedCourseEnrollment(
                "CSC101_F25_A", "STU000012345"))
                .thenReturn(ServiceResponse.<DetailedCourseEnrollmentResponse>builder().build());

        mockMvc.perform(get("/course-enrollment")
                        .param("offeringCode", "CSC101_F25_A")
                        .param("studentIdentityId", "STU000012345"))
                .andExpect(status().isOk());

    }

}
