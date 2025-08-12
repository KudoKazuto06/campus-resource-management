package com.campus_resource_management.studentservice.repository;

import com.campus_resource_management.studentservice.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    Optional<StudentProfile> findBySchoolEmail(String schoolEmail);
}
