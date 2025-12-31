package com.campus_resource_management.courseservice.grpc;

import com.campus_resource_management.courseservice.grpc.dto.StudentInfo;
import com.campus_resource_management.proto.student.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentGrpcClient {

    private final StudentServiceGrpc.StudentServiceBlockingStub stub;

    public boolean existsByIdentityId(String identityId) {
        StudentExistRequest request = StudentExistRequest.newBuilder()
                .setIdentityId(identityId)
                .build();

        StudentExistResponse response = stub.existsByIdentityId(request);
        return response.getExists();
    }

    public List<String> filterStudentIdentityIds(String fullName, String status) {
        FilterStudentRequest request = FilterStudentRequest.newBuilder()
                .setFullName(fullName != null ? fullName : "")
                .setStatus(status != null ? status.toUpperCase() : "ACTIVE")
                .build();

        FilterStudentIdentityIdsResponse response = stub.filterStudentIdentityIds(request);

        return response.getIdentityIdsList().stream()
                .collect(Collectors.toList());
    }

    public StudentInfo getStudentInfo(String identityId) {
        StudentInfoRequest request = StudentInfoRequest.newBuilder()
                .setIdentityId(identityId)
                .build();

        StudentInfoResponse response = stub.getStudentInfoByIdentityId(request);

        return StudentInfo.builder()
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .enrollmentStatus(response.getEnrollmentStatus())
                .build();
    }
}
