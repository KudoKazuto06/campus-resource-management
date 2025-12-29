package com.campus_resource_management.courseservice.repository.course;

import com.campus_resource_management.courseservice.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // Check if courseCode exists among non-deleted courses
    boolean existsByCourseCodeAndIsDeletedFalse(String courseCode);

    // Find by UUID for non-deleted courses
    Optional<Course> findByIdAndIsDeletedFalse(UUID id);

    // Find by courseCode for non-deleted courses
    Optional<Course> findByCourseCodeAndIsDeletedFalse(String courseCode);

    // Find by courseCode including soft-deleted (for restore)
    @Query("SELECT c FROM Course c WHERE c.courseCode = :courseCode")
    Optional<Course> findByCourseCodeIncludeDeleted(@Param("courseCode") String courseCode);

    // Search by keyword in courseCode or courseName among non-deleted courses
    @Query("""
        SELECT c FROM Course c
        WHERE c.isDeleted = false
          AND (:keyword IS NULL
               OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
}
