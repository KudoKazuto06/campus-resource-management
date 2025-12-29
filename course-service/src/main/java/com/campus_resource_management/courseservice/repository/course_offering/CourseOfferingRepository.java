package com.campus_resource_management.courseservice.repository.course_offering;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, UUID> {

    boolean existsByCourse_IdAndTermAndYearAndIsDeletedFalse(
            UUID courseId,
            AcademicTerm term,
            Integer year
    );

    Optional<CourseOffering> findByIdAndIsDeletedFalse(UUID offeringId);

    List<CourseOffering> findAllByCourse_IdAndIsDeletedFalse(UUID courseId);
}
