package com.campus_resource_management.courseservice.grpc.config;

import com.campus_resource_management.proto.student.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentGrpcConfig {

    @Bean
    public StudentServiceGrpc.StudentServiceBlockingStub studentServiceStub(
            @Value("${grpc.student-service.host}") String host,
            @Value("${grpc.student-service.port}") int port) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        return StudentServiceGrpc.newBlockingStub(channel);
    }
}
