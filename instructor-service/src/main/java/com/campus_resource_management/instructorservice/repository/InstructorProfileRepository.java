package com.campus_resource_management.instructorservice.repository;

import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InstructorProfileRepository extends
        JpaRepository<InstructorProfile, UUID>,
        InstructorProfileRepositoryForFilter {

    // ===== Search only active Instructor Profiles =====
    @Query("SELECT i FROM InstructorProfile i WHERE i.identityId = :identityId AND i.isDeleted = false")
    Optional<InstructorProfile> findByIdentityId(String identityId);

    // ===== Include soft-deleted profiles =====
    @Query("SELECT i FROM InstructorProfile i WHERE i.identityId = :identityId")
    Optional<InstructorProfile> findByIdentityIdIncludeSoftDeleted(String identityId);

    // ===== Search by school email =====
    @Query("SELECT i FROM InstructorProfile i WHERE i.schoolEmail = :schoolEmail")
    Optional<InstructorProfile> findBySchoolEmail(String schoolEmail);

    // ==== Search by email =====
    Optional<InstructorProfile> findByEmail(String email);
}
