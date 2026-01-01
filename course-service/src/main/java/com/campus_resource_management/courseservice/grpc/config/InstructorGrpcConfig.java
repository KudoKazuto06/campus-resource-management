package com.campus_resource_management.courseservice.grpc.config;

import com.campus_resource_management.proto.instructor.InstructorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstructorGrpcConfig {

    @Bean
    public InstructorServiceGrpc.InstructorServiceBlockingStub
    instructorServiceStub(
            @Value("${grpc.instructor-service.host}") String host,
            @Value("${grpc.instructor-service.port}") int  port) {

        ManagedChannel channel =
                ManagedChannelBuilder
                        .forAddress(host, port)
                        .usePlaintext()
                        .build();

        return InstructorServiceGrpc.newBlockingStub(channel);
    }
}

