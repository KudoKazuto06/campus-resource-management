package com.campus_resource_management.courseservice;

import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
import com.campus_resource_management.courseservice.grpc.StudentGrpcClient;
import com.campus_resource_management.courseservice.service.CourseEnrollmentServiceImpl;
import com.campus_resource_management.courseservice.service.CourseOfferingServiceImpl;
import com.campus_resource_management.courseservice.service.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CourseServiceImpl.class, CourseOfferingServiceImpl.class, CourseEnrollmentServiceImpl.class})
@ActiveProfiles("test")
class CourseserviceApplicationTests {

	@MockBean
	private InstructorGrpcClient instructorGrpcClient;

	@MockBean
	private StudentGrpcClient studentGrpcClient;

	@Test
	void contextLoads() {
	}

}
