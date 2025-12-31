package com.campus_resource_management.courseservice.repository.course_enrollment;

import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import com.campus_resource_management.courseservice.grpc.StudentGrpcClient;
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
public class CourseEnrollmentRepositoryForFilterImpl implements CourseEnrollmentRepositoryForFilter {

    @PersistenceContext
    private EntityManager em;

    private final StudentGrpcClient studentGrpcClient;

    @Override
    public Page<CourseEnrollment> filterCourseEnrollment(FilterCourseEnrollmentRequest filter, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CourseEnrollment> cq = cb.createQuery(CourseEnrollment.class);
        Root<CourseEnrollment> root = cq.from(CourseEnrollment.class);

        // Build predicates
        List<Predicate> predicates = buildPredicates(cb, root, filter);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting
        List<Order> orders = new ArrayList<>();
        String sortBy = filter.getSortBy();
        if (sortBy != null && !sortBy.isBlank()) {
            switch (sortBy) {
                case "enrollmentId" -> orders.add(cb.asc(root.get("id")));
                case "offeringCode" -> orders.add(cb.asc(root.get("offeringCode")));
                case "studentIdentityId" -> orders.add(cb.asc(root.get("studentIdentityId")));
                case "enrolledAt" -> orders.add(cb.asc(root.get("enrolledAt")));
                default -> orders.add(cb.asc(root.get("id"))); // default
            }
        } else {
            orders.add(cb.asc(root.get("id"))); // default
        }
        cq.orderBy(orders);

        // Pagination
        TypedQuery<CourseEnrollment> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<CourseEnrollment> results = query.getResultList();

        // Total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CourseEnrollment> countRoot = countQuery.from(CourseEnrollment.class);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, filter);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<CourseEnrollment> root,
            FilterCourseEnrollmentRequest filter) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getOfferingCode() != null) {
            predicates.add(cb.equal(root.get("offeringCode"), filter.getOfferingCode()));
        }

        if (filter.getIsWithdrawn() != null) {
            predicates.add(cb.equal(root.get("isWithdrawn"), filter.getIsWithdrawn()));
        }

        if (filter.getEnrolledFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("enrolledAt"), filter.getEnrolledFrom()));
        }

        if (filter.getEnrolledTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("enrolledAt"), filter.getEnrolledTo()));
        }

        if (filter.getLetterGrade() != null) {
            predicates.add(cb.equal(root.get("letterGrade"), filter.getLetterGrade()));
        }

        if (filter.getStudentName() != null) {
            List<String> studentIds = studentGrpcClient.filterStudentIdentityIds(
                    filter.getStudentName(), "ACTIVE" // or get from filter if you add a status field
            );
            if (!studentIds.isEmpty()) {
                predicates.add(root.get("studentIdentityId").in(studentIds));
            } else {
                predicates.add(cb.disjunction()); // no matches
            }
        }


        return predicates;
    }
}
