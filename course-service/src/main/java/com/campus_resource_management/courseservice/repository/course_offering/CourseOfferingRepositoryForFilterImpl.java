package com.campus_resource_management.courseservice.repository.course_offering;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.entity.Course;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
import com.campus_resource_management.courseservice.mapper.DepartmentMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseOfferingRepositoryForFilterImpl implements CourseOfferingRepositoryForFilter {

    @PersistenceContext
    private EntityManager em;

    private final InstructorGrpcClient instructorGrpcClient;
    private final DepartmentMapper departmentMapper;

    @Override
    public Page<CourseOffering> filterCourseOffering(FilterCourseOfferingRequest filter, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CourseOffering> cq = cb.createQuery(CourseOffering.class);
        Root<CourseOffering> root = cq.from(CourseOffering.class);

        // Build predicates
        List<Predicate> predicates = buildPredicates(cb, root, filter);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting
        List<Order> orders = new ArrayList<>();
        String sortBy = filter.getSortBy();
        if (sortBy != null && !sortBy.isBlank()) {
            switch (sortBy) {
                case "courseOfferingCode" -> orders.add(cb.asc(root.get("offeringCode")));
                case "courseCode" -> orders.add(cb.asc(root.get("courseCode")));
                case "term" -> orders.add(cb.asc(root.get("term")));
                case "year" -> orders.add(cb.asc(root.get("year")));
                case "maxStudents" -> orders.add(cb.asc(root.get("maxStudents")));
                default -> orders.add(cb.asc(root.get("courseOfferingCode")));
            }
        } else {
            orders.add(cb.asc(root.get("offeringCode"))); // default
        }
        cq.orderBy(orders);

        // Pagination
        TypedQuery<CourseOffering> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<CourseOffering> results = query.getResultList();

        // Total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CourseOffering> countRoot = countQuery.from(CourseOffering.class);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, filter);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<CourseOffering> root,
            FilterCourseOfferingRequest filter) {

        List<Predicate> predicates = new ArrayList<>();

        // Join Course
        Join<CourseOffering, Course> courseJoin = root.join("course", JoinType.LEFT);

        // CourseOffering code
        if (filter.getCourseOfferingCode() != null) {
            predicates.add(cb.equal(root.get("offeringCode"), filter.getCourseOfferingCode()));
        }

        // Course code
        if (filter.getCourseCode() != null) {
            predicates.add(cb.equal(courseJoin.get("courseCode"), filter.getCourseCode()));
        }

        // Course department
        if (filter.getCourseDepartment() != null) {
            String deptShort = departmentMapper
                    .toShortCode(departmentMapper.toDepartment(filter.getCourseDepartment())); // e.g. COMPUTER_SCIENCE -> CSC
            if (deptShort != null) {
                predicates.add(cb.like(cb.upper(root.get("offeringCode")), deptShort + "%"));
            }
        }

        // Term
        if (filter.getTerm() != null) {
            predicates.add(cb.equal(root.get("term"), AcademicTerm.valueOf(filter.getTerm())));
        }

        // Year range
        if (filter.getYearFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("year"), filter.getYearFrom()));
        }
        if (filter.getYearTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("year"), filter.getYearTo()));
        }

        // Instructor filter via gRPC
        if (filter.getInstructorName() != null || filter.getAcademicRank() != null) {
            List<String> instructorIds = instructorGrpcClient
                    .filterInstructorIdentityIds(filter.getInstructorName(), filter.getAcademicRank());

            if (!instructorIds.isEmpty()) {
                predicates.add(root.get("instructorIdentityId").in(instructorIds));
            } else {
                // No matching instructors -> always false
                predicates.add(cb.disjunction());
            }
        }

        return predicates;
    }
}