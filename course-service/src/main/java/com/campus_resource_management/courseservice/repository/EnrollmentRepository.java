package com.campus_resource_management.courseservice.repository;

import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository
        extends JpaRepository<CourseEnrollment, UUID> {

    boolean existsByCourseOfferingIdAndStudentIdentityIdAndIsWithdrawnFalse(
            UUID offeringId,
            String studentIdentityId
    );

    Optional<CourseEnrollment>
    findByCourseOfferingIdAndStudentIdentityId(
            UUID offeringId,
            String studentIdentityId
    );

    Page<CourseEnrollment> findByStudentIdentityIdAndIsWithdrawnFalse(
            String studentIdentityId,
            Pageable pageable
    );

    Page<CourseEnrollment> findByCourseOfferingIdAndIsWithdrawnFalse(
            UUID offeringId,
            Pageable pageable
    );
}
