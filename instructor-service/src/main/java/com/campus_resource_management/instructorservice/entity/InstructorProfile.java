package com.campus_resource_management.instructorservice.entity;

import com.campus_resource_management.instructorservice.constant.AcademicRank;
import com.campus_resource_management.instructorservice.constant.Department;
import com.campus_resource_management.instructorservice.constant.EmploymentStatus;
import com.campus_resource_management.instructorservice.constant.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "instructor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorProfile extends BaseEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "instructor_id")
    private UUID id;

    /**
     * Linked to IAM / Keycloak user
     */
    @Column(name = "identity_id", nullable = false, unique = true)
    private String identityId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "school_email", nullable = false, unique = true)
    private String schoolEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "office_location")
    private String officeLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_rank", nullable = false)
    private AcademicRank academicRank;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    /**
     * Example: A1, A2, B1 — avoid raw salary in microservices
     */
    @Column(name = "salary_band")
    private String salaryBand;

    @Column(name = "office_hours")
    private String officeHours;

    @Column(name = "instructor_note")
    private String instructorNote;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
