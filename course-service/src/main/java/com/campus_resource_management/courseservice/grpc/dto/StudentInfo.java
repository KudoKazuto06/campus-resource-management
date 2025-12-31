package com.campus_resource_management.courseservice.grpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo {

    private String firstName;
    private String lastName;
    private String email;
    private String enrollmentStatus; // ACTIVE, INACTIVE, etc.
}
