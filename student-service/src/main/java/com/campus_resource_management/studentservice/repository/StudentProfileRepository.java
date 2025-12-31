package com.campus_resource_management.studentservice.repository;

import com.campus_resource_management.studentservice.constant.StudentStatus;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    boolean existsBySchoolEmail(String email);

    // Active profile by normal email
    @Query("SELECT s FROM StudentProfile s WHERE s.email = :email AND s.isDeleted = false")
    Optional<StudentProfile> findByEmail(String email);

    boolean existsByIdentityIdAndIsDeletedFalse(String identityId);

    Optional<StudentProfile> findByIdentityIdAndIsDeletedFalse(String identityId);

    @Query("SELECT s FROM StudentProfile s WHERE " +
            "(LOWER(s.firstName) || ' ' || LOWER(s.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%')) " +
            "AND s.studentStatus = :status " +
            "AND s.isDeleted = false")
    List<StudentProfile> findAllByFullNameAndStatus(@Param("fullName") String fullName,
                                                    @Param("status") StudentStatus status);


}
