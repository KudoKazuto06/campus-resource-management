package com.campus_resource_management.courseservice.grpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String officeHours;
}
