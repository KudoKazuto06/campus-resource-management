package com.campus_resource_management.courseservice.exception;

import com.campus_resource_management.courseservice.constant.MessageResponse;

public class CourseOfferingNotFoundException extends RuntimeException {
    public CourseOfferingNotFoundException(String offeringCode) {
        super(MessageResponse.format(MessageResponse.COURSE_OFFERING_NOT_FOUND));
    }
}
