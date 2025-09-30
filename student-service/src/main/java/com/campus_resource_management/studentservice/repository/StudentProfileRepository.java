package com.campus_resource_management.studentservice.repository;

import com.campus_resource_management.studentservice.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID>, StudentProfileRepositoryForFilter {

    // ===== Search only active Student Profiles =====
    @Query("SELECT s FROM StudentProfile s WHERE s.identityId = :identityId AND s.isDeleted = false")
    Optional<StudentProfile> findByIdentityId(String identityId);

    // ===== Include soft-deleted profiles =====
    @Query("SELECT s FROM StudentProfile s WHERE s.identityId = :identityId")
    Optional<StudentProfile> findByIdentityIdIncludeSoftDeleted(String identityId);
    @Query("SELECT s FROM StudentProfile s WHERE s.schoolEmail = :schoolEmail")
    Optional<StudentProfile> findBySchoolEmail(String schoolEmail);

}
