package com.campus_resource_management.courseservice.repository;

import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.constant.AcademicTerm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository
        extends JpaRepository<CourseOffering, UUID> {

    boolean existsByCourseIdAndTermAndYearAndIsDeletedFalse(
            UUID courseId,
            AcademicTerm term,
            Integer year
    );

    Optional<CourseOffering> findByIdAndIsDeletedFalse(UUID id);

    List<CourseOffering> findByCourseIdAndIsDeletedFalse(UUID courseId);

    Page<CourseOffering> findByCourseIdAndTermAndYearAndIsDeletedFalse(
            UUID courseId,
            AcademicTerm term,
            Integer year,
            Pageable pageable
    );

    Page<CourseOffering> findByInstructorIdentityIdAndIsDeletedFalse(
            String instructorIdentityId,
            Pageable pageable
    );
}
