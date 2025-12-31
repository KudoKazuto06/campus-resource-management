package com.campus_resource_management.courseservice.repository.course_offering;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import com.campus_resource_management.courseservice.grpc.InstructorGrpcClient;
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
                case "courseOfferingCode" -> orders.add(cb.asc(root.get("courseOfferingCode")));
                case "courseCode" -> orders.add(cb.asc(root.get("courseCode")));
                case "term" -> orders.add(cb.asc(root.get("term")));
                case "year" -> orders.add(cb.asc(root.get("year")));
                case "maxStudents" -> orders.add(cb.asc(root.get("maxStudents")));
                default -> orders.add(cb.asc(root.get("courseOfferingCode")));
            }
        } else {
            orders.add(cb.asc(root.get("courseOfferingCode"))); // default
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

        if (filter.getCourseOfferingCode() != null) {
            predicates.add(cb.equal(root.get("courseOfferingCode"), filter.getCourseOfferingCode()));
        }

        if (filter.getCourseCode() != null) {
            predicates.add(cb.equal(root.get("courseCode"), filter.getCourseCode()));
        }

        if (filter.getCourseDepartment() != null) {
            predicates.add(cb.like(
                    cb.upper(root.get("courseCode")),
                    filter.getCourseDepartment().toUpperCase() + "%"
            ));
        }

        if (filter.getTerm() != null) {
            predicates.add(cb.equal(root.get("term"), AcademicTerm.valueOf(filter.getTerm())));
        }

        if (filter.getYearFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("year"), filter.getYearFrom()));
        }

        if (filter.getYearTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("year"), filter.getYearTo()));
        }

        if (filter.getInstructorName() != null || filter.getAcademicRank() != null) {
            List<String> instructorIds = instructorGrpcClient
                    .filterInstructorIdentityIds(filter.getInstructorName(), filter.getAcademicRank());

            if (!instructorIds.isEmpty()) {
                predicates.add(root.get("instructorId").in(instructorIds));
            } else {
                // No matching instructors → return empty
                predicates.add(cb.disjunction());
            }
        }
        return predicates;
    }
}
