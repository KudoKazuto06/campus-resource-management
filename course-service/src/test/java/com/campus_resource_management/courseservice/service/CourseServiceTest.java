package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.constant.*;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.exception.*;
import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
import com.campus_resource_management.courseservice.grpc.StudentGrpcClient;
import com.campus_resource_management.courseservice.grpc.dto.InstructorInfo;
import com.campus_resource_management.courseservice.grpc.dto.StudentInfo;
import com.campus_resource_management.courseservice.mapper.*;
import com.campus_resource_management.courseservice.repository.course.CourseRepository;
import com.campus_resource_management.courseservice.repository.course.CourseRepositoryForFilter;
import com.campus_resource_management.courseservice.repository.course_enrollment.CourseEnrollmentRepository;
import com.campus_resource_management.courseservice.repository.course_enrollment.CourseEnrollmentRepositoryForFilter;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepositoryForFilter;
import com.campus_resource_management.courseservice.service.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    // ===== CourseService mocks =====
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseRepositoryForFilter courseRepositoryForFilter;

    @Spy
    private CourseMapper courseMapper = new CourseMapperImpl();

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private InstructorGrpcClient instructorGrpcClient;

    // ===== CourseOfferingService mocks =====
    @Mock
    private CourseOfferingRepository courseOfferingRepository;

    @Mock
    private CourseOfferingRepositoryForFilter courseOfferingRepositoryForFilter;

    @Spy
    private CourseOfferingMapper courseOfferingMapper = new CourseOfferingMapperImpl();

    // ===== CourseEnrollmentService mocks =====
    @Mock
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Mock
    private CourseEnrollmentRepositoryForFilter courseEnrollmentRepositoryForFilter;

    @Spy
    private CourseEnrollmentMapper courseEnrollmentMapper = new CourseEnrollmentMapperImpl();

    @Mock
    private StudentGrpcClient studentGrpcClient;

    // ===== Services =====
    @InjectMocks
    private CourseServiceImpl courseService;

    @InjectMocks
    private CourseOfferingServiceImpl courseOfferingService;

    @InjectMocks
    private CourseEnrollmentServiceImpl courseEnrollmentService;

    // ===== Test data =====
    private Course course;
    private SummaryCourseResponse summaryResponse;
    private DetailedCourseResponse detailedResponse;
    private CourseOffering offering;
    private AddCourseOfferingRequest addCourseOfferingRequest;
    private UpdateCourseOfferingRequest updateCourseOfferingRequest;
    private CourseEnrollment enrollment;
    private AddCourseEnrollmentRequest addCourseEnrollmentRequest;
    private UpdateCourseEnrollmentRequest updateCourseEnrollmentRequest;

    @BeforeEach
    void setUp() {
        // ----- Course -----
        course = new Course();
        course.setId(UUID.randomUUID());
        course.setCourseCode("CSC101");
        summaryResponse = SummaryCourseResponse.builder().build();
        detailedResponse = DetailedCourseResponse.builder().build();

        // ----- Course Offering -----
        offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(false);

        addCourseOfferingRequest = AddCourseOfferingRequest.builder()
                .courseCode("CSC101")
                .term("FALL")
                .year(2025)
                .section("A")
                .instructorIdentityId("INST12345678")
                .maxStudents(50)
                .build();

        updateCourseOfferingRequest = UpdateCourseOfferingRequest.builder()
                .courseOfferingCode("CSC101_F25_A")
                .maxStudents(60)
                .instructorIdentityId("INST87654321")
                .build();

        // ----- Course Enrollment -----
        enrollment = CourseEnrollment.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU00012345")
                .isWithdrawn(false)
                .build();

        addCourseEnrollmentRequest = AddCourseEnrollmentRequest.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU00012345")
                .build();

        updateCourseEnrollmentRequest = UpdateCourseEnrollmentRequest.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU00012345")
                .finalGrade(90.0)
                .letterGrade("A")
                .build();
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
    void addCourse_whenCourseCodeExists_shouldThrowFieldExistedException() {
        AddCourseRequest request = new AddCourseRequest();
        request.setCourseCode("101");
        request.setDepartment("COMPUTER_SCIENCE");

        when(departmentMapper.toShortCode(any(Department.class))).thenReturn("CSC");

        when(courseRepository.existsByCourseCodeAndIsDeletedFalse("CSC101")).thenReturn(true);

        // Act & Assert
        FieldExistedException exception = assertThrows(FieldExistedException.class,
                () -> courseService.addCourse(request));

        assertEquals(MessageResponse.COURSE_CODE_ALREADY_EXISTS, exception.getMessage());

        // Verify interactions
        verify(courseRepository).existsByCourseCodeAndIsDeletedFalse("CSC101");
        verify(departmentMapper).toShortCode(any(Department.class));
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
        // Arrange
        Course course = new Course();
        course.setCourseCode("CSC101");
        course.setIsDeleted(true); // must be soft-deleted

        when(courseRepository.findByCourseCodeIncludeDeleted("CSC101")).thenReturn(Optional.of(course));
        when(courseRepository.existsByCourseCodeAndIsDeletedFalse("CSC101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        var response = courseService.restoreCourseByCourseCode("CSC101");

        // Assert
        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        verify(courseRepository).findByCourseCodeIncludeDeleted("CSC101");
        verify(courseRepository).save(course);
    }

    @Test
    void testRestoreCourseByCourseCode_error_CourseNotFound_shouldThrowCourseNotFoundException() {
        when(courseRepository.findByCourseCodeIncludeDeleted(anyString())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.restoreCourseByCourseCode("CSC999"));
    }


    // ===================== SUCCESS =====================
    @Test
    void addCourseOffering_success_shouldReturnSummaryResponse() {

        when(courseRepository.findByCourseCodeAndIsDeletedFalse("CSC101"))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        when(courseOfferingRepository
                .existsByCourse_IdAndTermAndYearAndSectionAndIsDeletedFalse(
                        any(), any(), any(), any()))
                .thenReturn(false);

        when(courseOfferingRepository.existsByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(false);

        when(courseOfferingMapper.addRequestToEntity(any(), any()))
                .thenReturn(offering);

        when(courseOfferingRepository.save(any()))
                .thenReturn(offering);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        ServiceResponse<SummaryCourseOfferingResponse> response =
                courseOfferingService.addCourseOffering(addCourseOfferingRequest);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.CREATED, response.getStatusCode());
        assertNotNull(response.getData());
    }

    // ===================== COURSE NOT FOUND =====================
    @Test
    void addCourseOffering_courseNotFound_shouldThrowException() {

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseNotFoundException.class,
                () -> courseOfferingService.addCourseOffering(addCourseOfferingRequest)
        );
    }

    // ===================== INSTRUCTOR NOT FOUND =====================
    @Test
    void addCourseOffering_instructorNotFound_shouldThrowException() {

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(false);

        assertThrows(
                InstructorNotFoundException.class,
                () -> courseOfferingService.addCourseOffering(addCourseOfferingRequest)
        );
    }

    // ===================== INVALID TERM =====================
    @Test
    void addCourseOffering_invalidTerm_shouldThrowException() {

        addCourseOfferingRequest.setTerm("INVALID_TERM");

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        assertThrows(
                InvalidAcademicTermException.class,
                () -> courseOfferingService.addCourseOffering(addCourseOfferingRequest)
        );
    }

    // ===================== DUPLICATE OFFERING =====================
    @Test
    void addCourseOffering_duplicateOffering_shouldThrowException() {

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        when(courseOfferingRepository
                .existsByCourse_IdAndTermAndYearAndSectionAndIsDeletedFalse(
                        any(), any(), any(), any()))
                .thenReturn(true);

        FieldExistedException ex = assertThrows(
                FieldExistedException.class,
                () -> courseOfferingService.addCourseOffering(addCourseOfferingRequest)
        );

        assertEquals(
                MessageResponse.COURSE_OFFERING_ALREADY_EXISTS,
                ex.getMessage()
        );
    }

    // ===================== DUPLICATE OFFERING CODE =====================
    @Test
    void addCourseOffering_duplicateOfferingCode_shouldThrowException() {

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        when(courseOfferingRepository
                .existsByCourse_IdAndTermAndYearAndSectionAndIsDeletedFalse(
                        any(), any(), any(), any()))
                .thenReturn(false);

        when(courseOfferingRepository.existsByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(true);

        assertThrows(
                FieldExistedException.class,
                () -> courseOfferingService.addCourseOffering(addCourseOfferingRequest)
        );
    }

    // ===================== TEST MAPPER =====================
    @Test
    void addRequestToEntity_shouldMapAllFieldsCorrectly() {

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .courseCode("CMPUT101")
                .isDeleted(false)
                .build();

        AddCourseOfferingRequest request = AddCourseOfferingRequest.builder()
                .courseCode("CMPUT101")
                .term("FALL")
                .year(2025)
                .section("A")
                .maxStudents(120)
                .instructorIdentityId("INST12345678")
                .description("Intro course")
                .build();

        CourseOffering offering = courseOfferingMapper.addRequestToEntity(request, course);

        assertThat(offering).isNotNull();
        assertThat(offering.getCourse()).isEqualTo(course);
        assertThat(offering.getTerm()).isEqualTo(AcademicTerm.FALL);
        assertThat(offering.getYear()).isEqualTo(2025);
        assertThat(offering.getSection()).isEqualTo("A");
        assertThat(offering.getMaxStudents()).isEqualTo(120);
        assertThat(offering.getInstructorIdentityId()).isEqualTo("INST12345678");
        assertThat(offering.getDescription()).isEqualTo("Intro course");
        assertThat(offering.getIsDeleted()).isFalse();
    }

    // ===================== TEST REPOSITORY =====================
    @Test
    void existsByOfferingCodeAndIsDeletedFalse_shouldReturnTrue_whenActiveOfferingExists() {

        when(courseOfferingRepository
                .existsByOfferingCodeAndIsDeletedFalse("CMPUT201_F25_A"))
                .thenReturn(true);

        boolean exists =
                courseOfferingRepository.existsByOfferingCodeAndIsDeletedFalse(
                        "CMPUT201_F25_A"
                );

        assertThat(exists).isTrue();
    }

    // ===================== UPDATE SUCCESS =====================
    @Test
    void updateCourseOffering_success_shouldReturnSummaryResponse() {

        UpdateCourseOfferingRequest request =
                UpdateCourseOfferingRequest.builder()
                        .courseOfferingCode("CSC101_F25_A")
                        .instructorIdentityId("INST12345678")
                        .maxStudents(60)
                        .description("Updated description")
                        .build();

        CourseOffering existing = new CourseOffering();
        existing.setOfferingCode("CSC101_F25_A");
        existing.setInstructorIdentityId("INST00000000");
        existing.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse("CSC101_F25_A"))
                .thenReturn(Optional.of(existing));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        doNothing().when(courseOfferingMapper)
                .updateCourseOfferingFromRequest(any(), any());

        when(courseOfferingRepository.save(any()))
                .thenReturn(existing);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        ServiceResponse<SummaryCourseOfferingResponse> response =
                courseOfferingService.updateCourseOffering(request);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertNotNull(response.getData());
    }

    // ===================== COURSE OFFERING NOT FOUND =====================
    @Test
    void updateCourseOffering_notFound_shouldThrowException() {

        UpdateCourseOfferingRequest request =
                UpdateCourseOfferingRequest.builder()
                        .courseOfferingCode("CSC101_F25_A")
                        .build();

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.updateCourseOffering(request)
        );
    }

    // ===================== INSTRUCTOR NOT FOUND =====================
    @Test
    void updateCourseOffering_instructorNotFound_shouldThrowException() {

        UpdateCourseOfferingRequest request =
                UpdateCourseOfferingRequest.builder()
                        .courseOfferingCode("CSC101_F25_A")
                        .instructorIdentityId("INST12345678")
                        .build();

        CourseOffering existing = new CourseOffering();
        existing.setOfferingCode("CSC101_F25_A");
        existing.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(existing));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(false);

        assertThrows(
                InstructorNotFoundException.class,
                () -> courseOfferingService.updateCourseOffering(request)
        );
    }

    // ===================== NULL INSTRUCTOR SHOULD SKIP GRPC CHECK =====================
    @Test
    void updateCourseOffering_nullInstructor_shouldSkipGrpcCheck() {

        UpdateCourseOfferingRequest request =
                UpdateCourseOfferingRequest.builder()
                        .courseOfferingCode("CSC101_F25_A")
                        .maxStudents(80)
                        .build();

        CourseOffering existing = new CourseOffering();
        existing.setOfferingCode("CSC101_F25_A");
        existing.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(existing));

        doNothing().when(courseOfferingMapper)
                .updateCourseOfferingFromRequest(any(), any());

        when(courseOfferingRepository.save(any()))
                .thenReturn(existing);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        ServiceResponse<SummaryCourseOfferingResponse> response =
                courseOfferingService.updateCourseOffering(request);

        verify(instructorGrpcClient, never())
                .existsByIdentityId(anyString());

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
    }

    // ===================== FILTER SUCCESS =====================
    @Test
    void filterCourseOfferings_success_shouldReturnPaginationResponse() {

        FilterCourseOfferingRequest filterRequest =
                FilterCourseOfferingRequest.builder()
                        .page(0)
                        .size(10)
                        .build();

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");

        Page<CourseOffering> page =
                new PageImpl<>(
                        List.of(offering),
                        PageRequest.of(0, 10),
                        1
                );

        when(courseOfferingRepositoryForFilter
                .filterCourseOffering(any(), any()))
                .thenReturn(page);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> response =
                courseOfferingService.filterCourseOfferings(filterRequest);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().getTotalItems());
        assertEquals(1, response.getData().getListData().size());
    }

    // ===================== FILTER DEFAULT PAGE SIZE =====================
    @Test
    void filterCourseOfferings_nullPageSize_shouldUseDefaults() {

        FilterCourseOfferingRequest filterRequest =
                FilterCourseOfferingRequest.builder().build();

        Page<CourseOffering> page =
                new PageImpl<>(
                        List.of(new CourseOffering()),
                        PageRequest.of(0, 10),
                        1
                );

        when(courseOfferingRepositoryForFilter
                .filterCourseOffering(any(), any()))
                .thenReturn(page);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> response =
                courseOfferingService.filterCourseOfferings(filterRequest);

        assertEquals(1, response.getData().getTotalItems());
        assertEquals(0, response.getData().getCurrentPage());
        assertEquals(10, response.getData().getPageSize());
    }

    // ===================== FILTER EMPTY RESULT =====================
    @Test
    void filterCourseOfferings_emptyResult_shouldReturnEmptyList() {

        FilterCourseOfferingRequest filterRequest =
                FilterCourseOfferingRequest.builder()
                        .page(0)
                        .size(5)
                        .build();

        Page<CourseOffering> emptyPage =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(0, 5),
                        0
                );

        when(courseOfferingRepositoryForFilter
                .filterCourseOffering(any(), any()))
                .thenReturn(emptyPage);

        ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> response =
                courseOfferingService.filterCourseOfferings(filterRequest);

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(0, response.getData().getTotalItems());
        assertTrue(response.getData().getListData().isEmpty());
    }

    // ===================== FILTER PAGEABLE PASSED CORRECTLY =====================
    @Test
    void filterCourseOfferings_shouldPassCorrectPageable() {

        FilterCourseOfferingRequest filterRequest =
                FilterCourseOfferingRequest.builder()
                        .page(2)
                        .size(20)
                        .build();

        when(courseOfferingRepositoryForFilter
                .filterCourseOffering(any(), any()))
                .thenReturn(Page.empty());

        courseOfferingService.filterCourseOfferings(filterRequest);

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(courseOfferingRepositoryForFilter)
                .filterCourseOffering(any(), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
    }

    // ===================== FILTER SHOULD MAP EACH ENTITY =====================
    @Test
    void filterCourseOfferings_shouldMapEachEntity() {

        FilterCourseOfferingRequest filterRequest =
                FilterCourseOfferingRequest.builder().build();

        Page<CourseOffering> page =
                new PageImpl<>(
                        List.of(new CourseOffering(), new CourseOffering()),
                        PageRequest.of(0, 10),
                        2
                );

        when(courseOfferingRepositoryForFilter
                .filterCourseOffering(any(), any()))
                .thenReturn(page);

        when(courseOfferingMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseOfferingResponse.builder().build());

        courseOfferingService.filterCourseOfferings(filterRequest);

        verify(courseOfferingMapper, times(2))
                .toSummaryResponse(any());
    }

    // ===================== VIEW DETAIL SUCCESS =====================
    @Test
    void viewDetailedCourseOfferingByCode_success_shouldReturnDetailedResponse() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setInstructorIdentityId("INST12345678");

        DetailedCourseOfferingResponse detailedResponse =
                DetailedCourseOfferingResponse.builder().build();

        InstructorInfo instructorInfo = InstructorInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@uni.edu")
                .officeHours("Mon 10-12")
                .build();

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse("CSC101_F25_A"))
                .thenReturn(Optional.of(offering));

        when(instructorGrpcClient
                .getInstructorInfo("INST12345678"))
                .thenReturn(instructorInfo);

        when(courseOfferingMapper
                .toDetailedResponse(offering, instructorInfo))
                .thenReturn(detailedResponse);

        ServiceResponse<DetailedCourseOfferingResponse> response =
                courseOfferingService.viewDetailedCourseOfferingByCode(
                        "CSC101_F25_A"
                );

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertNotNull(response.getData());
    }

    // ===================== VIEW DETAIL OFFERING NOT FOUND =====================
    @Test
    void viewDetailedCourseOfferingByCode_notFound_shouldThrowException() {

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.viewDetailedCourseOfferingByCode(
                        "CSC999_F25_A"
                )
        );
    }

    // ===================== VIEW DETAIL INSTRUCTOR NOT FOUND =====================
    @Test
    void viewDetailedCourseOfferingByCode_instructorNotFound_shouldThrowException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setInstructorIdentityId("INST99999999");

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(offering));

        when(instructorGrpcClient
                .getInstructorInfo("INST99999999"))
                .thenThrow(new InstructorNotFoundException("INST99999999"));

        assertThrows(
                InstructorNotFoundException.class,
                () -> courseOfferingService.viewDetailedCourseOfferingByCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== VIEW DETAIL MAPPER ERROR =====================
    @Test
    void viewDetailedCourseOfferingByCode_mapperThrowsException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setInstructorIdentityId("INST12345678");

        InstructorInfo instructorInfo = InstructorInfo.builder().build();

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(offering));

        when(instructorGrpcClient
                .getInstructorInfo(anyString()))
                .thenReturn(instructorInfo);

        when(courseOfferingMapper
                .toDetailedResponse(any(), any()))
                .thenThrow(new RuntimeException("Mapper error"));

        assertThrows(
                RuntimeException.class,
                () -> courseOfferingService.viewDetailedCourseOfferingByCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== DELETE COURSE OFFERING SUCCESS =====================
    @Test
    void deleteCourseOffering_success_shouldSoftDelete() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse("CSC101_F25_A"))
                .thenReturn(Optional.of(offering));

        ServiceResponse<Void> response =
                courseOfferingService.deleteCourseOfferingByOfferingCode("CSC101_F25_A");

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());

        assertTrue(offering.getIsDeleted());

        verify(courseOfferingRepository)
                .save(offering);
    }

    // ===================== DELETE COURSE OFFERING NOT FOUND =====================
    @Test
    void deleteCourseOffering_notFound_shouldThrowException() {

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.deleteCourseOfferingByOfferingCode(
                        "CSC999_F25_A"
                )
        );
    }

    // ===================== DELETE COURSE OFFERING ALREADY DELETED =====================
    @Test
    void deleteCourseOffering_alreadyDeleted_shouldThrowException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(true);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.deleteCourseOfferingByOfferingCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== DELETE COURSE OFFERING REPOSITORY ERROR =====================
    @Test
    void deleteCourseOffering_repositoryThrowsException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(offering));

        when(courseOfferingRepository
                .save(any()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(
                RuntimeException.class,
                () -> courseOfferingService.deleteCourseOfferingByOfferingCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== RESTORE COURSE OFFERING SUCCESS =====================
    @Test
    void restoreCourseOffering_success_shouldRestore() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(true);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedTrue("CSC101_F25_A"))
                .thenReturn(Optional.of(offering));

        ServiceResponse<Void> response =
                courseOfferingService.restoreCourseOfferingByOfferingCode("CSC101_F25_A");

        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());

        assertFalse(offering.getIsDeleted());

        verify(courseOfferingRepository)
                .save(offering);
    }

    // ===================== RESTORE COURSE OFFERING NOT FOUND =====================
    @Test
    void restoreCourseOffering_notFound_shouldThrowException() {

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedTrue(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.restoreCourseOfferingByOfferingCode(
                        "CSC999_F25_A"
                )
        );
    }

    // ===================== RESTORE COURSE OFFERING ALREADY ACTIVE =====================
    @Test
    void restoreCourseOffering_alreadyActive_shouldThrowException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(false);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedTrue(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseOfferingNotFoundException.class,
                () -> courseOfferingService.restoreCourseOfferingByOfferingCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== RESTORE COURSE OFFERING REPOSITORY ERROR =====================
    @Test
    void restoreCourseOffering_repositoryThrowsException() {

        CourseOffering offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");
        offering.setIsDeleted(true);

        when(courseOfferingRepository
                .findByOfferingCodeAndIsDeletedTrue(anyString()))
                .thenReturn(Optional.of(offering));

        when(courseOfferingRepository
                .save(any()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(
                RuntimeException.class,
                () -> courseOfferingService.restoreCourseOfferingByOfferingCode(
                        "CSC101_F25_A"
                )
        );
    }

    // ===================== ADD SUCCESS =====================
    @Test
    void addCourseEnrollment_success() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.of(offering));
        when(studentGrpcClient.existsByIdentityId(any()))
                .thenReturn(true);
        when(courseEnrollmentRepository
                .existsByOfferingCodeAndStudentIdentityIdAndIsWithdrawnFalse(any(), any()))
                .thenReturn(false);
        when(courseEnrollmentMapper.addRequestToEntity(any(), any()))
                .thenReturn(enrollment);
        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);
        when(courseEnrollmentMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        ServiceResponse<SummaryCourseEnrollmentResponse> response =
                courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest);

        assertEquals(StatusCode.CREATED, response.getStatusCode());
        assertNotNull(response.getData());
        verify(courseEnrollmentRepository).save(any());
    }

    // ===================== OFFERING NOT FOUND =====================
    @Test
    void addCourseEnrollment_offeringNotFound_throwException() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.empty());

        assertThrows(CourseOfferingNotFoundException.class,
                () -> courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest));

        verify(courseEnrollmentRepository, never()).save(any());
    }

    // ===================== STUDENT NOT FOUND =====================
    @Test
    void addCourseEnrollment_studentNotFound_throwException() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.of(offering));
        when(studentGrpcClient.existsByIdentityId(any()))
                .thenReturn(false);

        assertThrows(StudentNotFoundException.class,
                () -> courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest));

        verify(courseEnrollmentRepository, never()).save(any());
    }

    // ===================== DUPLICATE ENROLLMENT =====================
    @Test
    void addCourseEnrollment_duplicateEnrollment_throwException() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.of(offering));
        when(studentGrpcClient.existsByIdentityId(any()))
                .thenReturn(true);
        when(courseEnrollmentRepository
                .existsByOfferingCodeAndStudentIdentityIdAndIsWithdrawnFalse(any(), any()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest)
        );

        assertEquals(MessageResponse.COURSE_ENROLLMENT_ALREADY_EXISTS, exception.getMessage());
        verify(courseEnrollmentRepository, never()).save(any());
    }

    // ===================== SAVE FAILURE =====================
    @Test
    void addCourseEnrollment_repositorySaveFails_throwException() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.of(offering));
        when(studentGrpcClient.existsByIdentityId(any()))
                .thenReturn(true);
        when(courseEnrollmentRepository
                .existsByOfferingCodeAndStudentIdentityIdAndIsWithdrawnFalse(any(), any()))
                .thenReturn(false);
        when(courseEnrollmentMapper.addRequestToEntity(any(), any()))
                .thenReturn(enrollment);
        when(courseEnrollmentRepository.save(any()))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest));
    }

    // ===================== OFFERING IS DELETED =====================
    @Test
    void addCourseEnrollment_offeringDeleted_notReturnedByRepo() {
        when(courseOfferingRepository.findByOfferingCodeAndIsDeletedFalse(any()))
                .thenReturn(Optional.empty());

        assertThrows(CourseOfferingNotFoundException.class,
                () -> courseEnrollmentService.addCourseEnrollment(addCourseEnrollmentRequest));
    }

    // ===================== UPDATE =====================

    @Test
    void updateCourseEnrollment_success() {

        UpdateCourseEnrollmentRequest request =
                UpdateCourseEnrollmentRequest.builder()
                        .offeringCode("CSC101_F25_A")
                        .studentIdentityId("STU000012345")
                        .finalGrade(90.0)
                        .letterGrade("A")
                        .build();

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .id(UUID.randomUUID())
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        SummaryCourseEnrollmentResponse summary =
                SummaryCourseEnrollmentResponse.builder().build();

        when(courseEnrollmentMapper.toSummaryResponse(enrollment))
                .thenReturn(summary);

        ServiceResponse<SummaryCourseEnrollmentResponse> response =
                courseEnrollmentService.updateCourseEnrollment(request);

        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(summary, response.getData());

        verify(courseEnrollmentMapper)
                .updateCourseEnrollmentFromRequest(request, enrollment);
        verify(courseEnrollmentRepository).save(enrollment);
    }

    @Test
    void updateCourseEnrollment_notFound() {

        UpdateCourseEnrollmentRequest request =
                UpdateCourseEnrollmentRequest.builder()
                        .offeringCode("CSC101_F25_A")
                        .studentIdentityId("STU000012345")
                        .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of());

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.updateCourseEnrollment(request));

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void updateCourseEnrollment_studentMismatch() {

        UpdateCourseEnrollmentRequest request =
                UpdateCourseEnrollmentRequest.builder()
                        .offeringCode("CSC101_F25_A")
                        .studentIdentityId("STU000012345")
                        .build();

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000099999") // different student
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.updateCourseEnrollment(request));
    }

    @Test
    void updateCourseEnrollment_mapperIsCalled() {

        UpdateCourseEnrollmentRequest request =
                UpdateCourseEnrollmentRequest.builder()
                        .offeringCode("CSC101_F25_A")
                        .studentIdentityId("STU000012345")
                        .finalGrade(75.0)
                        .build();

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(any()))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        when(courseEnrollmentMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        courseEnrollmentService.updateCourseEnrollment(request);

        verify(courseEnrollmentMapper)
                .updateCourseEnrollmentFromRequest(request, enrollment);
    }

    @Test
    void updateCourseEnrollment_saveCalledOnce() {

        UpdateCourseEnrollmentRequest request =
                UpdateCourseEnrollmentRequest.builder()
                        .offeringCode("CSC101_F25_A")
                        .studentIdentityId("STU000012345")
                        .build();

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(any()))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        when(courseEnrollmentMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        courseEnrollmentService.updateCourseEnrollment(request);

        verify(courseEnrollmentRepository, times(1)).save(enrollment);
    }

    // ===================== WITHDRAW =====================

    @Test
    void withdrawCourseEnrollment_success() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .id(UUID.randomUUID())
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        ServiceResponse<Void> response =
                courseEnrollmentService.withdrawCourseEnrollment(
                        "CSC101_F25_A", "STU000012345");

        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, response.getStatus());

        assertTrue(enrollment.getIsWithdrawn());
        assertNotNull(enrollment.getWithdrawnAt());

        verify(courseEnrollmentRepository).save(enrollment);
    }

    @Test
    void withdrawCourseEnrollment_notFound() {

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of());

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.withdrawCourseEnrollment(
                        "CSC101_F25_A", "STU000012345"));

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void withdrawCourseEnrollment_studentMismatch() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000099999") // different student
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.withdrawCourseEnrollment(
                        "CSC101_F25_A", "STU000012345"));
    }

    @Test
    void withdrawCourseEnrollment_setsWithdrawnDate() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(any()))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        courseEnrollmentService.withdrawCourseEnrollment(
                "CSC101_F25_A", "STU000012345");

        assertNotNull(enrollment.getWithdrawnAt());
    }

    @Test
    void withdrawCourseEnrollment_saveCalledOnce() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(any()))
                .thenReturn(List.of(enrollment));

        when(courseEnrollmentRepository.save(any()))
                .thenReturn(enrollment);

        courseEnrollmentService.withdrawCourseEnrollment(
                "CSC101_F25_A", "STU000012345");

        verify(courseEnrollmentRepository, times(1)).save(enrollment);
    }

    // ===================== VIEW DETAIL =====================

    @Test
    void viewDetailedCourseEnrollment_success() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        StudentInfo studentInfo = StudentInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        when(studentGrpcClient.getStudentInfo("STU000012345"))
                .thenReturn(studentInfo);

        when(courseEnrollmentMapper.toDetailedResponse(enrollment))
                .thenReturn(DetailedCourseEnrollmentResponse.builder().build());

        ServiceResponse<DetailedCourseEnrollmentResponse> response =
                courseEnrollmentService.viewDetailedCourseEnrollment(
                        "CSC101_F25_A", "STU000012345");

        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, response.getStatus());

        verify(studentGrpcClient).getStudentInfo("STU000012345");
        verify(courseEnrollmentMapper).toDetailedResponse(enrollment);
    }

    @Test
    void viewDetailedCourseEnrollment_notFound() {

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of());

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.viewDetailedCourseEnrollment(
                        "CSC101_F25_A", "STU000012345"));
    }

    @Test
    void viewDetailedCourseEnrollment_studentMismatch() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .studentIdentityId("STU000099999") // different student
                .isWithdrawn(false)
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        assertThrows(CourseEnrollmentNotFoundException.class,
                () -> courseEnrollmentService.viewDetailedCourseEnrollment(
                        "CSC101_F25_A", "STU000012345"));
    }

    @Test
    void viewDetailedCourseEnrollment_mapsStudentInfo() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        StudentInfo studentInfo = StudentInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        DetailedCourseEnrollmentResponse detailedResponse =
                DetailedCourseEnrollmentResponse.builder().build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A"))
                .thenReturn(List.of(enrollment));

        when(studentGrpcClient.getStudentInfo("STU000012345"))
                .thenReturn(studentInfo);

        when(courseEnrollmentMapper.toDetailedResponse(enrollment))
                .thenReturn(detailedResponse);

        ServiceResponse<DetailedCourseEnrollmentResponse> response =
                courseEnrollmentService.viewDetailedCourseEnrollment(
                        "CSC101_F25_A", "STU000012345");

        assertEquals("John Doe", response.getData().getStudentName());
        assertEquals("john.doe@example.com", response.getData().getStudentEmail());
    }

    @Test
    void viewDetailedCourseEnrollment_callsRepositoryAndMapperOnce() {

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        StudentInfo studentInfo = StudentInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(any()))
                .thenReturn(List.of(enrollment));

        when(studentGrpcClient.getStudentInfo(any()))
                .thenReturn(studentInfo);

        when(courseEnrollmentMapper.toDetailedResponse(any()))
                .thenReturn(DetailedCourseEnrollmentResponse.builder().build());

        courseEnrollmentService.viewDetailedCourseEnrollment(
                "CSC101_F25_A", "STU000012345");

        verify(courseEnrollmentRepository, times(1))
                .findAllByOfferingCodeAndIsWithdrawnFalse("CSC101_F25_A");

        verify(courseEnrollmentMapper, times(1))
                .toDetailedResponse(enrollment);

        verify(studentGrpcClient, times(1))
                .getStudentInfo("STU000012345");
    }

    // ===================== FILTER COURSE ENROLLMENTS =====================

    @Test
    void filterCourseEnrollments_success() {

        FilterCourseEnrollmentRequest filterRequest = FilterCourseEnrollmentRequest.builder()
                .page(0)
                .size(2)
                .build();

        CourseEnrollment enrollment1 = CourseEnrollment.builder()
                .offeringCode("CSC101_F25_A")
                .studentIdentityId("STU000012345")
                .isWithdrawn(false)
                .build();

        CourseEnrollment enrollment2 = CourseEnrollment.builder()
                .offeringCode("CSC102_F25_B")
                .studentIdentityId("STU000067890")
                .isWithdrawn(false)
                .build();

        Page<CourseEnrollment> pageResult = new org.springframework.data.domain.PageImpl<>(
                List.of(enrollment1, enrollment2));

        when(courseEnrollmentRepositoryForFilter.filterCourseEnrollment(any(), any()))
                .thenReturn(pageResult);

        when(courseEnrollmentMapper.toSummaryResponse(enrollment1))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());
        when(courseEnrollmentMapper.toSummaryResponse(enrollment2))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> response =
                courseEnrollmentService.filterCourseEnrollments(filterRequest);

        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, response.getStatus());
        assertEquals(2, response.getData().getListData().size());
    }

    @Test
    void filterCourseEnrollments_emptyResult() {

        FilterCourseEnrollmentRequest filterRequest = FilterCourseEnrollmentRequest.builder()
                .page(0)
                .size(2)
                .build();

        Page<CourseEnrollment> pageResult = new org.springframework.data.domain.PageImpl<>(List.of());

        when(courseEnrollmentRepositoryForFilter.filterCourseEnrollment(any(), any()))
                .thenReturn(pageResult);

        ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> response =
                courseEnrollmentService.filterCourseEnrollments(filterRequest);

        assertEquals(0, response.getData().getListData().size());
        assertEquals(StatusCode.SUCCESS, response.getStatusCode());
    }

    @Test
    void filterCourseEnrollments_defaultPageAndSize() {

        FilterCourseEnrollmentRequest filterRequest = FilterCourseEnrollmentRequest.builder()
                .build(); // null page & size

        Page<CourseEnrollment> pageResult = new org.springframework.data.domain.PageImpl<>(List.of());

        when(courseEnrollmentRepositoryForFilter.filterCourseEnrollment(any(), any()))
                .thenReturn(pageResult);

        ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> response =
                courseEnrollmentService.filterCourseEnrollments(filterRequest);

        assertEquals(0, response.getData().getListData().size());
        verify(courseEnrollmentRepositoryForFilter).filterCourseEnrollment(any(), any());
    }

    @Test
    void filterCourseEnrollments_mapperCalledForEachEnrollment() {

        FilterCourseEnrollmentRequest filterRequest = FilterCourseEnrollmentRequest.builder()
                .page(0)
                .size(2)
                .build();

        CourseEnrollment enrollment1 = CourseEnrollment.builder().build();
        CourseEnrollment enrollment2 = CourseEnrollment.builder().build();

        Page<CourseEnrollment> pageResult = new org.springframework.data.domain.PageImpl<>(
                List.of(enrollment1, enrollment2));

        when(courseEnrollmentRepositoryForFilter.filterCourseEnrollment(any(), any()))
                .thenReturn(pageResult);

        when(courseEnrollmentMapper.toSummaryResponse(any()))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        courseEnrollmentService.filterCourseEnrollments(filterRequest);

        verify(courseEnrollmentMapper, times(2)).toSummaryResponse(any());
    }

    @Test
    void filterCourseEnrollments_paginationValuesCorrect() {

        FilterCourseEnrollmentRequest filterRequest = FilterCourseEnrollmentRequest.builder()
                .page(1)
                .size(1)
                .build();

        CourseEnrollment enrollment = CourseEnrollment.builder().build();

        Page<CourseEnrollment> pageResult = new org.springframework.data.domain.PageImpl<>(
                List.of(enrollment),
                PageRequest.of(1, 1),
                5); // total 5 items

        when(courseEnrollmentRepositoryForFilter.filterCourseEnrollment(any(), any()))
                .thenReturn(pageResult);

        when(courseEnrollmentMapper.toSummaryResponse(enrollment))
                .thenReturn(SummaryCourseEnrollmentResponse.builder().build());

        ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> response =
                courseEnrollmentService.filterCourseEnrollments(filterRequest);

        assertEquals(1, response.getData().getCurrentPage());
        assertEquals(5, response.getData().getTotalItems());
        assertEquals(5, response.getData().getTotalPages()); // totalPages = ceil(5/1)
    }

}
