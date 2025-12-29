package com.campus_resource_management.courseservice.entity;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "course_offerings",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"course_id", "term", "year"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOffering extends BaseEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "offering_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "term", nullable = false)
    private AcademicTerm term;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "instructor_identity_id", nullable = false)
    private String instructorIdentityId;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
