package com.campus_resource_management.courseservice.grpc;

import com.campus_resource_management.courseservice.grpc.dto.InstructorInfo;
import com.campus_resource_management.proto.instructor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstructorGrpcClient {

    private final InstructorServiceGrpc.InstructorServiceBlockingStub stub;

    public boolean existsByIdentityId(String identityId) {

        InstructorExistRequest request =
                InstructorExistRequest.newBuilder()
                        .setIdentityId(identityId)
                        .build();

        InstructorExistResponse response =
                stub.existsByIdentityId(request);

        return response.getExists();
    }

    public List<String> filterInstructorIdentityIds(String fullName, String academicRank) {

        FilterInstructorRequest request = FilterInstructorRequest.newBuilder()
                .setFullName(fullName != null ? fullName : "")
                .setAcademicRank(academicRank != null ? academicRank : "")
                .build();

        FilterInstructorIdentityIdsResponse response =
                stub.filterInstructorIdentityIds(request);

        return response.getIdentityIdsList();
    }

    public InstructorInfo getInstructorInfo(String identityId) {
        InstructorInfoResponse response = stub.getInstructorInfoByIdentityId(
                InstructorInfoRequest.newBuilder()
                        .setIdentityId(identityId)
                        .build()
        );

        return InstructorInfo.builder()
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .officeHours(response.getOfficeHour())
                .build();
    }

}
