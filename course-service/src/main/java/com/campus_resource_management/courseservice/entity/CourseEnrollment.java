package com.campus_resource_management.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "course_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"offering_id", "student_identity_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollment {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "enrollment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private CourseOffering courseOffering;

    /** Student identity from IAM */
    @Column(name = "student_identity_id", nullable = false)
    private String studentIdentityId;

    @Builder.Default
    @Column(name = "is_withdrawn")
    private Boolean isWithdrawn = false;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDate enrolledAt;
}
