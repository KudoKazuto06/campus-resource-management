package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import com.campus_resource_management.courseservice.constant.StatusCode;
import com.campus_resource_management.courseservice.constant.StatusResponse;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.exception.CourseEnrollmentNotFoundException;
import com.campus_resource_management.courseservice.exception.CourseOfferingNotFoundException;
import com.campus_resource_management.courseservice.exception.FieldExistedException;
import com.campus_resource_management.courseservice.exception.StudentNotFoundException;
import com.campus_resource_management.courseservice.grpc.StudentGrpcClient;
import com.campus_resource_management.courseservice.grpc.dto.StudentInfo;
import com.campus_resource_management.courseservice.mapper.CourseEnrollmentMapper;
import com.campus_resource_management.courseservice.repository.course_enrollment.CourseEnrollmentRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepository;
import com.campus_resource_management.courseservice.repository.course_enrollment.CourseEnrollmentRepositoryForFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    private final CourseOfferingRepository courseOfferingRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseEnrollmentRepositoryForFilter courseEnrollmentRepositoryForFilter;
    private final CourseEnrollmentMapper courseEnrollmentMapper;
    private final StudentGrpcClient studentGrpcClient;

    @Override
    public ServiceResponse<SummaryCourseEnrollmentResponse> addCourseEnrollment(AddCourseEnrollmentRequest request) {

        // 1. Check course offering exists
        CourseOffering offering = courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(request.getOfferingCode())
                .orElseThrow(() -> new CourseOfferingNotFoundException(request.getOfferingCode()));

        // 2. Check student exists via gRPC
        if (!studentGrpcClient.existsByIdentityId(request.getStudentIdentityId())) {
            throw new StudentNotFoundException(request.getStudentIdentityId());
        }

        // 3. Check duplicate enrollment
        boolean exists = courseEnrollmentRepository
                .existsByOfferingCodeAndStudentIdentityIdAndIsWithdrawnFalse(
                        offering.getOfferingCode(),
                        request.getStudentIdentityId()
                );

        if (exists) {
            throw new FieldExistedException(MessageResponse.COURSE_ENROLLMENT_ALREADY_EXISTS);
        }

        // 4. Create enrollment using mapper with CourseOffering
        CourseEnrollment enrollment = courseEnrollmentMapper.addRequestToEntity(request, offering);
        enrollment.setCreatedBy("SYSTEM");

        CourseEnrollment saved = courseEnrollmentRepository.save(enrollment);

        SummaryCourseEnrollmentResponse response = courseEnrollmentMapper.toSummaryResponse(saved);

        return ServiceResponse.<SummaryCourseEnrollmentResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_COURSE_ENROLLMENT_SUCCESS))
                .data(response)
                .build();
    }


    @Override
    public ServiceResponse<SummaryCourseEnrollmentResponse> updateCourseEnrollment(UpdateCourseEnrollmentRequest request) {

        // 1. Find existing enrollment by offeringCode + studentIdentityId
        CourseEnrollment existingEnrollment = courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(request.getOfferingCode()).stream()
                .filter(e -> e.getStudentIdentityId().equals(request.getStudentIdentityId()))
                .findFirst()
                .orElseThrow(() -> new CourseEnrollmentNotFoundException(
                        "Course Enrollment Not Found"
                ));

        // 2. Update allowed fields
        courseEnrollmentMapper.updateCourseEnrollmentFromRequest(request, existingEnrollment);
        existingEnrollment.setModifiedBy("SYSTEM");

        CourseEnrollment saved = courseEnrollmentRepository.save(existingEnrollment);

        SummaryCourseEnrollmentResponse response = courseEnrollmentMapper.toSummaryResponse(saved);

        return ServiceResponse.<SummaryCourseEnrollmentResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.UPDATE_COURSE_ENROLLMENT_SUCCESS)
                .data(response)
                .build();
    }

    @Override
    public ServiceResponse<Void> withdrawCourseEnrollment(String offeringCode, String studentIdentityId) {

        CourseEnrollment enrollment = courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(offeringCode).stream()
                .filter(e -> e.getStudentIdentityId().equals(studentIdentityId))
                .findFirst()
                .orElseThrow(() -> new CourseEnrollmentNotFoundException(
                        "Enrollment not found for offering " + offeringCode +
                                " and student " + studentIdentityId
                ));

        enrollment.setIsWithdrawn(true);
        enrollment.setWithdrawnAt(java.time.LocalDate.now());
        courseEnrollmentRepository.save(enrollment);

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.WITHDRAW_COURSE_ENROLLMENT_SUCCESS))
                .build();
    }

    @Override
    public ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> filterCourseEnrollments(FilterCourseEnrollmentRequest filterRequest) {

        int page = filterRequest.getPage() != null ? filterRequest.getPage() : 0;
        int size = filterRequest.getSize() != null ? filterRequest.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<CourseEnrollment> enrollmentPage = courseEnrollmentRepositoryForFilter.filterCourseEnrollment(filterRequest, pageable);

        List<SummaryCourseEnrollmentResponse> dtoList = enrollmentPage.getContent().stream()
                .map(courseEnrollmentMapper::toSummaryResponse)
                .toList();

        PaginationResponse<SummaryCourseEnrollmentResponse> pagination = PaginationResponse.<SummaryCourseEnrollmentResponse>builder()
                .listData(dtoList)
                .totalPages(enrollmentPage.getTotalPages())
                .currentPage(enrollmentPage.getNumber())
                .totalItems(enrollmentPage.getTotalElements())
                .pageSize(enrollmentPage.getSize())
                .build();

        return ServiceResponse.<PaginationResponse<SummaryCourseEnrollmentResponse>>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.VIEW_ALL_COURSE_ENROLLMENTS_SUCCESS))
                .data(pagination)
                .build();
    }

    @Override
    public ServiceResponse<DetailedCourseEnrollmentResponse> viewDetailedCourseEnrollment(String offeringCode, String studentIdentityId) {

        CourseEnrollment enrollment = courseEnrollmentRepository
                .findAllByOfferingCodeAndIsWithdrawnFalse(offeringCode).stream()
                .filter(e -> e.getStudentIdentityId().equals(studentIdentityId))
                .findFirst()
                .orElseThrow(() -> new CourseEnrollmentNotFoundException("Course Enrollment Not Found"));

        // Fetch student info via gRPC
        var studentInfo = studentGrpcClient.getStudentInfo(studentIdentityId);

        DetailedCourseEnrollmentResponse response = courseEnrollmentMapper.toDetailedResponse(enrollment);
        response.setStudentName(studentInfo.getFirstName() + " " + studentInfo.getLastName());
        response.setStudentEmail(studentInfo.getEmail());

        return ServiceResponse.<DetailedCourseEnrollmentResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.VIEW_DETAIL_COURSE_ENROLLMENT_SUCCESS)
                .data(response)
                .build();
    }

}
