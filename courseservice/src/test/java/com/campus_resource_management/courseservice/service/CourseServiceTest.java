package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.constant.Department;
import com.campus_resource_management.courseservice.constant.StatusCode;
import com.campus_resource_management.courseservice.constant.StatusResponse;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.exception.CourseNotFoundException;
import com.campus_resource_management.courseservice.exception.FieldExistedException;
import com.campus_resource_management.courseservice.exception.ListEmptyException;
import com.campus_resource_management.courseservice.mapper.CourseMapper;
import com.campus_resource_management.courseservice.mapper.DepartmentMapper;
import com.campus_resource_management.courseservice.repository.CourseRepository;
import com.campus_resource_management.courseservice.repository.CourseRepositoryForFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseRepositoryForFilter courseRepositoryForFilter;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private SummaryCourseResponse summaryResponse;
    private DetailedCourseResponse detailedResponse;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseCode("CSC101");
        summaryResponse = SummaryCourseResponse.builder().build();
        detailedResponse = DetailedCourseResponse.builder().build();
    }

    // -------------------- ADD COURSE --------------------
    @Test
    void testAddCourse_success_shouldReturnSummaryResponse() {
        AddCourseRequest request = new AddCourseRequest();
        request.setCourseCode("101");
        request.setDepartment("COMPUTER_SCIENCE");

        when(courseRepository.existsByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(false);
        when(departmentMapper.toShortCode(any(Department.class))).thenReturn("CSC");
        when(courseMapper.addCourseRequestToCourse(any())).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toSummaryResponse(any(Course.class))).thenReturn(summaryResponse);

        ServiceResponse<SummaryCourseResponse> response = courseService.addCourse(request);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.CREATED, response.getStatusCode());
        assertNotNull(response.getData());
    }

    @Test
    void testAddCourse_error_CourseCodeExists_shouldThrowFieldExistedException() {
        AddCourseRequest request = new AddCourseRequest();
        request.setCourseCode("CSC101");

        when(courseRepository.existsByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(true);

        assertThrows(FieldExistedException.class, () -> courseService.addCourse(request));
    }

    @Test
    void testAddCourse_error_MapperThrowsException() {
        AddCourseRequest request = new AddCourseRequest();
        request.setCourseCode("101");
        request.setDepartment("COMPUTER_SCIENCE");

        when(courseRepository.existsByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(false);
        when(departmentMapper.toShortCode(any(Department.class))).thenReturn("CSC");
        when(courseMapper.addCourseRequestToCourse(any())).thenThrow(new RuntimeException("Mapper error"));

        assertThrows(RuntimeException.class, () -> courseService.addCourse(request));
    }

    // -------------------- UPDATE COURSE --------------------
    @Test
    void testUpdateCourse_success_shouldReturnSummaryResponse() {
        UpdateCourseRequest request = new UpdateCourseRequest();
        request.setCourseCode("CSC101");
        request.setDepartment("COMPUTER_SCIENCE");

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.of(course));
        when(departmentMapper.toShortCode(any(Department.class))).thenReturn("CSC");
        doNothing().when(courseMapper).updateCourseRequestToCourse(any(), any());
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toSummaryResponse(any(Course.class))).thenReturn(summaryResponse);

        ServiceResponse<SummaryCourseResponse> response = courseService.updateCourse(request);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testUpdateCourse_error_CourseNotFound_shouldThrowCourseNotFoundException() {
        UpdateCourseRequest request = new UpdateCourseRequest();
        request.setCourseCode("CSC999");

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(request));
    }

    // -------------------- VIEW FILTERED COURSES --------------------
    @Test
    void testViewFilteredCourses_success_shouldReturnPaginationResponse() {
        FilterCourseRequest filterRequest = new FilterCourseRequest();

        Page<Course> page = new PageImpl<>(List.of(course), PageRequest.of(0, 5), 1);
        when(courseRepositoryForFilter.filterCourse(any(), any(Pageable.class))).thenReturn(page);
        when(courseMapper.toSummaryResponse(any(Course.class))).thenReturn(summaryResponse);

        ServiceResponse<PaginationResponse<SummaryCourseResponse>> response = courseService.viewFilteredCourses(filterRequest);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().getTotalItems());
    }

    @Test
    void testViewFilteredCourses_error_ListEmpty_shouldThrowListEmptyException() {
        FilterCourseRequest filterRequest = new FilterCourseRequest();

        Page<Course> page = new PageImpl<>(List.of());
        when(courseRepositoryForFilter.filterCourse(any(), any(Pageable.class))).thenReturn(page);

        assertThrows(ListEmptyException.class, () -> courseService.viewFilteredCourses(filterRequest));
    }

    // -------------------- VIEW DETAILED COURSE --------------------
    @Test
    void testViewDetailedCourseByCourseCode_success_shouldReturnDetailedResponse() {
        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.of(course));
        when(courseMapper.toDetailedResponse(any(Course.class))).thenReturn(detailedResponse);

        ServiceResponse<DetailedCourseResponse> response = courseService.viewDetailedCourseByCourseCode("CSC101");

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void testViewDetailedCourseByCourseCode_error_CourseNotFound_shouldThrowCourseNotFoundException() {
        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.viewDetailedCourseByCourseCode("CSC999"));
    }

    // -------------------- DELETE COURSE --------------------
    @Test
    void testDeleteCourseByCourseCode_success_shouldReturnVoid() {
        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ServiceResponse<Void> response = courseService.deleteCourseByCourseCode("CSC101");

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
    }

    @Test
    void testDeleteCourseByCourseCode_error_CourseNotFound_shouldThrowCourseNotFoundException() {
        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourseByCourseCode("CSC999"));
    }

    // -------------------- RESTORE COURSE --------------------
    @Test
    void testRestoreCourseByCourseCode_success_shouldReturnVoid() {
        when(courseRepository.findByCourseCodeIncludeDeleted(anyString())).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ServiceResponse<Void> response = courseService.restoreCourseByCourseCode("CSC101");

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
    }

    @Test
    void testRestoreCourseByCourseCode_error_CourseNotFound_shouldThrowCourseNotFoundException() {
        when(courseRepository.findByCourseCodeIncludeDeleted(anyString())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.restoreCourseByCourseCode("CSC999"));
    }
}
