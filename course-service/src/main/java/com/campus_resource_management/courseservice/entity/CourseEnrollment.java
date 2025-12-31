package com.campus_resource_management.courseservice.entity;

import com.campus_resource_management.courseservice.constant.GradeStatus;
import com.campus_resource_management.courseservice.constant.LetterGrade;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "course_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"offering_code", "student_identity_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollment extends BaseEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "enrollment_id")
    private UUID id;

    @Column(name = "offering_code", nullable = false, length = 50)
    private String offeringCode;

    @Column(name = "student_identity_id", nullable = false)
    private String studentIdentityId;

    @Builder.Default
    @Column(name = "is_withdrawn")
    private Boolean isWithdrawn = false;

    @Column(name = "final_grade")
    private Double finalGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "letter_grade")
    private LetterGrade letterGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_status", nullable = false)
    @Builder.Default
    private GradeStatus gradeStatus = GradeStatus.NOT_GRADED;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDate enrolledAt;

    @Column(name = "withdrawn_at")
    private LocalDate withdrawnAt;

}
