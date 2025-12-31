package com.campus_resource_management.studentservice.grpc;

import com.campus_resource_management.proto.student.*;
import com.campus_resource_management.studentservice.constant.StudentStatus;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class StudentGrpcService extends StudentServiceGrpc.StudentServiceImplBase {

    private final StudentProfileRepository studentProfileRepository;

    @Override
    public void existsByIdentityId(StudentExistRequest request,
                                   io.grpc.stub.StreamObserver<StudentExistResponse> responseObserver) {

        boolean exists = studentProfileRepository.existsByIdentityIdAndIsDeletedFalse(request.getIdentityId());

        StudentExistResponse response = StudentExistResponse.newBuilder()
                .setExists(exists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void filterStudentIdentityIds(FilterStudentRequest request,
                                         io.grpc.stub.StreamObserver<FilterStudentIdentityIdsResponse> responseObserver) {

        String statusString = "ACTIVE"; // from request
        StudentStatus status;

        try {
            status = StudentStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid student status: " + statusString);
        }

        var identityIds = studentProfileRepository.findAllByFullNameAndStatus(
                        request.getFullName() != null ? request.getFullName().toLowerCase() : "",
                        status)
                .stream()
                .map(StudentProfile::getIdentityId)
                .collect(Collectors.toList());

        FilterStudentIdentityIdsResponse response = FilterStudentIdentityIdsResponse.newBuilder()
                .addAllIdentityIds(identityIds)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentInfoByIdentityId(StudentInfoRequest request,
                                           io.grpc.stub.StreamObserver<StudentInfoResponse> responseObserver) {

        var profile = studentProfileRepository.findByIdentityIdAndIsDeletedFalse(request.getIdentityId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentInfoResponse response = StudentInfoResponse.newBuilder()
                .setFirstName(profile.getFirstName())
                .setLastName(profile.getLastName())
                .setEmail(profile.getEmail())
                .setEnrollmentStatus(profile.getStudentStatus().name())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
