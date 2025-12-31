package com.campus_resource_management.courseservice.course.controller;

import com.campus_resource_management.courseservice.controller.CourseController;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.exception.CourseNotFoundException;
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

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        courseController = new CourseController(courseService);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
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
}
