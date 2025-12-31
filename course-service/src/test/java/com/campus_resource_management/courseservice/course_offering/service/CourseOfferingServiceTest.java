package com.campus_resource_management.courseservice.course_offering.service;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.constant.MessageResponse;
import com.campus_resource_management.courseservice.constant.StatusCode;
import com.campus_resource_management.courseservice.constant.StatusResponse;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.exception.*;
import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
import com.campus_resource_management.courseservice.grpc.dto.InstructorInfo;
import com.campus_resource_management.courseservice.mapper.CourseOfferingMapper;
import com.campus_resource_management.courseservice.mapper.CourseOfferingMapperImpl;
import com.campus_resource_management.courseservice.repository.course.CourseRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepositoryForFilter;
import com.campus_resource_management.courseservice.service.CourseOfferingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseOfferingServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseOfferingRepository courseOfferingRepository;

    @Mock
    private CourseOfferingRepositoryForFilter courseOfferingRepositoryForFilter;

    @Mock
    private CourseOfferingMapper courseOfferingMapper;

    @Mock
    private InstructorGrpcClient instructorGrpcClient;

    @InjectMocks
    private CourseOfferingServiceImpl courseOfferingService;

    private Course course;
    private CourseOffering offering;
    private AddCourseOfferingRequest request;
    private final CourseOfferingMapper mapper = new CourseOfferingMapperImpl();

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(UUID.randomUUID());
        course.setCourseCode("CSC101");

        offering = new CourseOffering();
        offering.setOfferingCode("CSC101_F25_A");

        request = AddCourseOfferingRequest.builder()
                .courseCode("CSC101")
                .term("FALL")
                .year(2025)
                .section("A")
                .instructorIdentityId("INST12345678")
                .maxStudents(50)
                .build();
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
                courseOfferingService.addCourseOffering(request);

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
                () -> courseOfferingService.addCourseOffering(request)
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
                () -> courseOfferingService.addCourseOffering(request)
        );
    }

    // ===================== INVALID TERM =====================
    @Test
    void addCourseOffering_invalidTerm_shouldThrowException() {

        request.setTerm("INVALID_TERM");

        when(courseRepository.findByCourseCodeAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(course));

        when(instructorGrpcClient.existsByIdentityId(anyString()))
                .thenReturn(true);

        assertThrows(
                InvalidAcademicTermException.class,
                () -> courseOfferingService.addCourseOffering(request)
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
                () -> courseOfferingService.addCourseOffering(request)
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
                () -> courseOfferingService.addCourseOffering(request)
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

        CourseOffering offering = mapper.addRequestToEntity(request, course);

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

}
