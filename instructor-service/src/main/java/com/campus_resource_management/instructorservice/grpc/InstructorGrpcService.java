package com.campus_resource_management.instructorservice.grpc;

import com.campus_resource_management.proto.instructor.*;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import com.campus_resource_management.instructorservice.repository.InstructorProfileRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class InstructorGrpcService
        extends InstructorServiceGrpc.InstructorServiceImplBase {

    private final InstructorProfileRepository instructorProfileRepository;

    @Override
    public void existsByIdentityId(
            InstructorExistRequest request,
            StreamObserver<InstructorExistResponse> responseObserver) {

        boolean exists =
                instructorProfileRepository
                        .findByIdentityId(request.getIdentityId())
                        .isPresent();

        InstructorExistResponse response =
                InstructorExistResponse.newBuilder()
                        .setExists(exists)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void filterInstructorIdentityIds(
            FilterInstructorRequest request,
            StreamObserver<FilterInstructorIdentityIdsResponse> responseObserver) {

        List<String> identityIds =
                instructorProfileRepository.filterIdentityIds(
                        request.getFullName(),
                        request.getAcademicRank()
                );

        FilterInstructorIdentityIdsResponse response =
                FilterInstructorIdentityIdsResponse.newBuilder()
                        .addAllIdentityIds(identityIds)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getInstructorInfoByIdentityId(InstructorInfoRequest request,
                                              StreamObserver<InstructorInfoResponse> responseObserver) {

        InstructorProfile profile = instructorProfileRepository
                .findByIdentityId(request.getIdentityId())
                .orElseThrow(() -> new RuntimeException("Instructor not found: " + request.getIdentityId()));

        InstructorInfoResponse response = InstructorInfoResponse.newBuilder()
                .setFirstName(profile.getFirstName())
                .setLastName(profile.getLastName())
                .setEmail(profile.getEmail())
                .setOfficeHour(profile.getOfficeHours())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
