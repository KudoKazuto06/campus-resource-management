package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.constant.*;
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
import com.campus_resource_management.courseservice.repository.course.CourseRepository;
import com.campus_resource_management.courseservice.repository.course.CourseRepositoryForFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseRepositoryForFilter courseRepositoryForFilter;
    private final CourseMapper courseMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    public ServiceResponse<SummaryCourseResponse> addCourse(AddCourseRequest addCourseRequest) {

        if (courseRepository.existsByCourseCodeAndIsDeletedFalse(addCourseRequest.getCourseCode())) {
            throw new FieldExistedException(MessageResponse.COURSE_CODE_ALREADY_EXISTS);
        }

        Department deptEnum = Department.valueOf(addCourseRequest.getDepartment().toUpperCase());
        String deptPrefix = departmentMapper.toShortCode(deptEnum);
        String fullCourseCode = deptPrefix + addCourseRequest.getCourseCode();
        addCourseRequest.setCourseCode(fullCourseCode);        addCourseRequest.setCourseCode(fullCourseCode);

        Course course = courseMapper.addCourseRequestToCourse(addCourseRequest);
        course.setCreatedBy("SYSTEM");

        Course savedCourse = courseRepository.save(course);
        SummaryCourseResponse response = courseMapper.toSummaryResponse(savedCourse);

        return ServiceResponse.<SummaryCourseResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_COURSE_SUCCESS))
                .data(response)
                .build();
    }

    @Override
    public ServiceResponse<SummaryCourseResponse> updateCourse(UpdateCourseRequest updateCourseRequest) {

        Course existingCourse = courseRepository.findByCourseCodeAndIsDeletedFalse(updateCourseRequest.getCourseCode())
                .orElseThrow(() -> new CourseNotFoundException(updateCourseRequest.getCourseCode()));

        if (updateCourseRequest.getDepartment() != null) {
            Department deptEnum = Department.valueOf(updateCourseRequest.getDepartment().toUpperCase());
            String deptPrefix = departmentMapper.toShortCode(deptEnum);

            String numericPart = existingCourse.getCourseCode().replaceAll("[^0-9]", "");
            existingCourse.setCourseCode(deptPrefix + numericPart);
        }

        updateCourseRequest.setCourseCode(null);
        courseMapper.updateCourseRequestToCourse(updateCourseRequest, existingCourse);

        existingCourse.setModifiedBy("SYSTEM");

        Course savedCourse = courseRepository.save(existingCourse);
        SummaryCourseResponse response = courseMapper.toSummaryResponse(savedCourse);

        return ServiceResponse.<SummaryCourseResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.UPDATE_COURSE_SUCCESS))
                .data(response)
                .build();
    }


    @Override
    public ServiceResponse<PaginationResponse<SummaryCourseResponse>> viewFilteredCourses(FilterCourseRequest filterCourseRequest) {

        int page = filterCourseRequest.getPage() != null ? filterCourseRequest.getPage() : 0;
        int size = filterCourseRequest.getSize() != null ? filterCourseRequest.getSize() : 5;

        Pageable pageable = PageRequest.of(page, size);

        // Filter repository handles predicates + default sorting based on column
        Page<Course> coursePage = courseRepositoryForFilter.filterCourse(filterCourseRequest, pageable);

        if (coursePage.getContent().isEmpty()) {
            throw new ListEmptyException(MessageResponse.NO_DATA + filterCourseRequest.toString());
        }

        PaginationResponse<SummaryCourseResponse> paginationResponse =
                PaginationResponse.<SummaryCourseResponse>builder()
                        .listData(coursePage.getContent().stream()
                                .map(courseMapper::toSummaryResponse)
                                .toList())
                        .totalPages(coursePage.getTotalPages())
                        .currentPage(coursePage.getNumber())
                        .totalItems(coursePage.getTotalElements())
                        .pageSize(coursePage.getSize())
                        .build();

        return ServiceResponse.<PaginationResponse<SummaryCourseResponse>>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.VIEW_ALL_COURSES_SUCCESS))
                .data(paginationResponse)
                .build();
    }

    @Override
    public ServiceResponse<DetailedCourseResponse> viewDetailedCourseByCourseCode(String courseCode) {

        Course course = courseRepository.findByCourseCodeAndIsDeletedFalse(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));

        return ServiceResponse.<DetailedCourseResponse>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.VIEW_DETAIL_COURSE_SUCCESS))
                .data(courseMapper.toDetailedResponse(course))
                .build();
    }

    @Override
    public ServiceResponse<Void> deleteCourseByCourseCode(String courseCode) {

        Course course = courseRepository.findByCourseCodeAndIsDeletedFalse(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));

        course.setIsDeleted(true);
        courseRepository.save(course);

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.DELETE_COURSE_SUCCESS))
                .build();
    }

    @Override
    public ServiceResponse<Void> restoreCourseByCourseCode(String courseCode) {

        Course course = courseRepository.findByCourseCodeIncludeDeleted(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));

        course.setIsDeleted(false);
        courseRepository.save(course);

        return ServiceResponse.<Void>builder()
                .statusCode(StatusCode.SUCCESS)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.RESTORE_COURSE_SUCCESS))
                .build();
    }
}
