package com.campus_resource_management.studentservice.entity;

import com.campus_resource_management.studentservice.constant.DegreeType;
import com.campus_resource_management.studentservice.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private UUID id;

    @Column(name = "identity_id", unique = true, nullable = false)
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

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree_type", nullable = false)
    private DegreeType degreeType;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "credits_completed")
    private Integer creditsCompleted = 0;

    @Column(name = "year_of_study")
    private Integer yearOfStudy;

    @Column(name = "student_note")
    private String studentNote;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // @OneToMany(mappedBy = "student_profile", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Course> courses = new ArrayList<>();

}
