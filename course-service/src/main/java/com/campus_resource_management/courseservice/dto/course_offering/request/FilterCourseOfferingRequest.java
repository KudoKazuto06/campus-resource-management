package com.campus_resource_management.courseservice.dto.course_offering.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCourseOfferingRequest {

    /* ================= Pagination ================= */
    @Min(value = 0, message = MessageResponse.PAGE_FORMAT_INVALID)
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = MessageResponse.SIZE_FORMAT_INVALID)
    @Builder.Default
    private Integer size = 10;

    /* ================= Course ================= */
    @Size(max = 20, message = MessageResponse.COURSE_CODE_MAX_LENGTH)
    private String courseCode;

    @Pattern(
            regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID
    )
    private String courseDepartment;

    /* ================= Academic ================= */
    @Pattern(
            regexp = "FALL|WINTER|SPRING|SUMMER",
            message = MessageResponse.ACADEMIC_TERM_INVALID
    )
    private String term;

    @Min(value = 2000, message = MessageResponse.ACADEMIC_YEAR_MIN_VALUE)
    @Max(value = 2100, message = MessageResponse.ACADEMIC_YEAR_MAX_VALUE)
    private Integer yearFrom;

    @Min(value = 2000, message = MessageResponse.ACADEMIC_YEAR_MIN_VALUE)
    @Max(value = 2100, message = MessageResponse.ACADEMIC_YEAR_MAX_VALUE)
    private Integer yearTo;

    /* ================= Instructor ================= */
    @Size(max = 100, message = MessageResponse.INSTRUCTOR_NAME_MAX_LENGTH)
    private String instructorName;

    @Pattern(
            regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID
    )
    private String instructorDepartment;

    @Pattern(
            regexp = "LECTURER|ASSISTANT_PROFESSOR|ASSOCIATE_PROFESSOR|PROFESSOR|GENERAL_STAFF",
            message = MessageResponse.ACADEMIC_RANK_TYPE_INVALID
    )
    private String academicRank;

    /* ================= Sorting ================= */
    private String sortBy;
}
