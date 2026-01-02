package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.constant.StatusCode;
import com.campus_resource_management.courseservice.constant.StatusResponse;
import com.campus_resource_management.courseservice.constant.MessageResponse;
import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.exception.CourseNotFoundException;
import com.campus_resource_management.courseservice.exception.CourseOfferingNotFoundException;
import com.campus_resource_management.courseservice.exception.FieldExistedException;
import com.campus_resource_management.courseservice.exception.InstructorNotFoundException;
import com.campus_resource_management.courseservice.exception.InvalidAcademicTermException;
import com.campus_resource_management.courseservice.mapper.CourseOfferingMapper;
import com.campus_resource_management.courseservice.repository.course.CourseRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepository;
import com.campus_resource_management.courseservice.repository.course_offering.CourseOfferingRepositoryForFilter;
import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
import com.campus_resource_management.courseservice.grpc.dto.InstructorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService {

    private final CourseRepository courseRepository;
    private final CourseOfferingRepositoryForFilter  courseOfferingRepositoryForFilter;
    private final CourseOfferingRepository courseOfferingRepository;
    private final CourseOfferingMapper courseOfferingMapper;
    private final InstructorGrpcClient instructorGrpcClient;

    @Override
    public ServiceResponse<SummaryCourseOfferingResponse> addCourseOffering(
            AddCourseOfferingRequest request) {

        /* ================= Guard 1: Course exists ================= */
        Course course = courseRepository
                .findByCourseCodeAndIsDeletedFalse(request.getCourseCode())
                .orElseThrow(() ->
                        new CourseNotFoundException(request.getCourseCode())
                );

        /* ================= Guard 2: Instructor exists (gRPC) ================= */
        boolean instructorExists =
                instructorGrpcClient.existsByIdentityId(
                        request.getInstructorIdentityId()
                );

        if (!instructorExists) {
            throw new InstructorNotFoundException(
                    request.getInstructorIdentityId()
            );
        }


        /* ================= Guard 3: Term validation ================= */
        AcademicTerm term;
        try {
            term = AcademicTerm.valueOf(request.getTerm().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidAcademicTermException("Invalid academic term: " + request.getTerm());
        }

        /* ================= Generate offering code ================= */
        String offeringCode = generateOfferingCode(
                course.getCourseCode(),
                term,
                request.getYear(),
                request.getSection()
        );

        if (courseOfferingRepository.existsByOfferingCodeAndIsDeletedFalse(offeringCode)) {
            throw new FieldExistedException(
                    MessageResponse.COURSE_OFFERING_ALREADY_EXISTS
            );
        }

        /* ================= Create entity ================= */
        CourseOffering offering =
                courseOfferingMapper.addRequestToEntity(request, course);

        offering.setOfferingCode(offeringCode);
        offering.setCreatedBy("SYSTEM");

        CourseOffering saved = courseOfferingRepository.save(offering);

        SummaryCourseOfferingResponse response =
                courseOfferingMapper.toSummaryResponse(saved);

        return ServiceResponse.<SummaryCourseOfferingResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_COURSE_OFFERING_SUCCESS))
                .data(response)
                .build();

    }

    @Override
    public ServiceResponse<SummaryCourseOfferingResponse> updateCourseOffering(
            UpdateCourseOfferingRequest request) {

        CourseOffering existingOffering =
                courseOfferingRepository
                        .findByOfferingCodeAndIsDeletedFalse(request.getCourseOfferingCode())
                        .orElseThrow(() ->
                                new CourseOfferingNotFoundException(
                                        request.getCourseOfferingCode()
                                )
                        );

        /* ================= Guard: Instructor exists ================= */
        if (request.getInstructorIdentityId() != null) {
            boolean instructorExists =
                    instructorGrpcClient.existsByIdentityId(
                            request.getInstructorIdentityId()
                    );

            if (!instructorExists) {
                throw new InstructorNotFoundException(
                        request.getInstructorIdentityId()
                );
            }
        }

        /* ================= Update allowed fields ================= */
        courseOfferingMapper.updateCourseOfferingFromRequest(
                request,
                existingOffering
        );

        existingOffering.setModifiedBy("SYSTEM");

        CourseOffering saved =
                courseOfferingRepository.save(existingOffering);

        SummaryCourseOfferingResponse response =
                courseOfferingMapper.toSummaryResponse(saved);

        return ServiceResponse.<SummaryCourseOfferingResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.UPDATE_COURSE_OFFERING_SUCCESS)
                .data(response)
                .build();
    }

    @Override
    public ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> filterCourseOfferings(
            FilterCourseOfferingRequest filterRequest) {

        // 1. Default page & size
        int page = filterRequest.getPage() != null ? filterRequest.getPage() : 0;
        int size = filterRequest.getSize() != null ? filterRequest.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        // 2. Fetch filtered page
        Page<CourseOffering> coursePage = courseOfferingRepositoryForFilter.filterCourseOffering(filterRequest, pageable);

        // 3. Map entities to DTOs
        List<SummaryCourseOfferingResponse> dtoList = coursePage.getContent().stream()
                .map(courseOfferingMapper::toSummaryResponse)
                .toList();

        // 4. Wrap in PaginationResponse
        PaginationResponse<SummaryCourseOfferingResponse> paginationResponse = PaginationResponse.<SummaryCourseOfferingResponse>builder()
                .listData(dtoList)
                .totalPages(coursePage.getTotalPages())
                .currentPage(coursePage.getNumber())
                .totalItems(coursePage.getTotalElements())
                .pageSize(coursePage.getSize())
                .build();

        // 5. Wrap in ServiceResponse
        return ServiceResponse.<PaginationResponse<SummaryCourseOfferingResponse>>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.VIEW_ALL_COURSE_OFFERINGS_SUCCESS))
                .data(paginationResponse)
                .build();
    }

    @Override
    public ServiceResponse<DetailedCourseOfferingResponse> viewDetailedCourseOfferingByCode(String courseOfferingCode) {

        // 1. Find the offering (not soft-deleted)
        CourseOffering offering = courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(courseOfferingCode)
                .orElseThrow(() -> new CourseOfferingNotFoundException(courseOfferingCode));

        // 2. Fetch instructor info via gRPC (using identityId)
        InstructorInfo instructorInfo = instructorGrpcClient.getInstructorInfo(offering.getInstructorIdentityId());

        // 3. Map to detailed response
        DetailedCourseOfferingResponse response = courseOfferingMapper.toDetailedResponse(offering, instructorInfo);

        // 4. Wrap in ServiceResponse
        return ServiceResponse.<DetailedCourseOfferingResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.VIEW_DETAIL_COURSE_OFFERING_SUCCESS))
                .data(response)
                .build();
    }

    @Override
    public ServiceResponse<Void> deleteCourseOfferingByOfferingCode(String courseOfferingCode) {

        // 1. Find existing offering
        CourseOffering offering = courseOfferingRepository
                .findByOfferingCodeAndIsDeletedFalse(courseOfferingCode)
                .orElseThrow(() -> new CourseOfferingNotFoundException(courseOfferingCode));

        // 2. Soft delete
        offering.setIsDeleted(true);

        // 3. Save
        courseOfferingRepository.save(offering);

        // 4. Return response
        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.DELETE_COURSE_OFFERING_SUCCESS))
                .build();
    }

    @Override
    public ServiceResponse<Void> restoreCourseOfferingByOfferingCode(String courseOfferingCode) {

        // Find the soft-deleted offering
        CourseOffering offering = courseOfferingRepository
                .findByOfferingCodeAndIsDeletedTrue(courseOfferingCode)
                .orElseThrow(() -> new CourseOfferingNotFoundException(courseOfferingCode));

        // Check for conflicts with other active offerings (same offering code)
        boolean conflict = courseOfferingRepository
                .existsByOfferingCodeAndIsDeletedFalse(courseOfferingCode);

        if (conflict) {
            throw new FieldExistedException("Cannot restore: course offering code already exists for an active offering");
        }

        // Restore
        offering.setIsDeleted(false);
        courseOfferingRepository.save(offering);

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.RESTORE_COURSE_OFFERING_SUCCESS))
                .build();
    }


    private String generateOfferingCode(
            String courseCode,
            AcademicTerm term,
            Integer year,
            String section
    ) {
        String termShort = switch (term) {
            case FALL -> "F";
            case WINTER -> "W";
            case SPRING -> "SP";
            case SUMMER -> "SU";
        };

        String yearShort = String.valueOf(year).substring(2);

        return String.format(
                "%s_%s%s_%s",
                courseCode,
                termShort,
                yearShort,
                section.toUpperCase()
        );
    }

}
