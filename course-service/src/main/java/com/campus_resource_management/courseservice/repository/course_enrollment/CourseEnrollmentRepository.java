package com.campus_resource_management.courseservice.repository.course_enrollment;

import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, UUID> {

    Optional<CourseEnrollment> findById(UUID id);

    Optional<CourseEnrollment> findByIdAndIsWithdrawnFalse(UUID id);

    boolean existsByOfferingCodeAndStudentIdentityIdAndIsWithdrawnFalse(
            String offeringCode,
            String studentIdentityId
    );

    List<CourseEnrollment> findAllByOfferingCodeAndIsWithdrawnFalse(String offeringCode);

    List<CourseEnrollment> findAllByStudentIdentityIdAndIsWithdrawnFalse(String studentIdentityId);
}
