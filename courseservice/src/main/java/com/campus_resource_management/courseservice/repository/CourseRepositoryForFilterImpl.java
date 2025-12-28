package com.campus_resource_management.courseservice.repository;

import com.campus_resource_management.courseservice.constant.Department;
import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.entity.Course;
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
public class CourseRepositoryForFilterImpl implements CourseRepositoryForFilter {

    @PersistenceContext
    private EntityManager em;

    private final DepartmentMapper departmentMapper;

    @Override
    public Page<Course> filterCourse(FilterCourseRequest filter, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);

        List<Predicate> predicates = buildPredicates(cb, root, filter);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Order> orders = new ArrayList<>();
        String sortBy = filter.getSortBy();

        if (sortBy != null && !sortBy.isBlank()) {
            switch (sortBy) {
                case "courseCode" -> orders.add(cb.asc(root.get("courseCode")));    // alphabetical
                case "courseName" -> orders.add(cb.asc(root.get("courseName")));    // alphabetical
                case "credit" -> orders.add(cb.asc(root.get("credit")));            // numeric low → high
                case "createdAt" -> orders.add(cb.desc(root.get("createdAt")));     // latest first
                default -> orders.add(cb.asc(root.get("courseCode")));              // fallback
            }
        } else {
            orders.add(cb.asc(root.get("courseCode")));                            // default
        }

        cq.orderBy(orders);

        TypedQuery<Course> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Course> results = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Course> countRoot = countQuery.from(Course.class);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, filter);
        countQuery.select(cb.count(countRoot))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Course> root,
            FilterCourseRequest filter) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCourseCode() != null) {
            predicates.add(cb.equal(root.get("courseCode"), filter.getCourseCode()));
        }

        if (filter.getCourseName() != null) {
            predicates.add(cb.like(
                    cb.lower(root.get("courseName")),
                    "%" + filter.getCourseName().toLowerCase() + "%"
            ));
        }

        if (filter.getDepartment() != null) {
            Department dept;

            try {
                // Case 1: ENUM name (COMPUTER_SCIENCE)
                dept = Department.valueOf(filter.getDepartment().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Case 2: short code (csc, mat, che)
                dept = departmentMapper.toDepartment(filter.getDepartment());
            }

            if (dept != null) {
                String prefix = departmentMapper.toShortCode(dept);
                predicates.add(cb.like(
                        cb.upper(root.get("courseCode")),
                        prefix + "%"
                ));
            }
        }


        if (filter.getCreditFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("credit"), filter.getCreditFrom()));
        }

        if (filter.getCreditTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("credit"), filter.getCreditTo()));
        }

        if (filter.getDescription() != null) {
            predicates.add(cb.like(
                    cb.lower(root.get("description")),
                    "%" + filter.getDescription().toLowerCase() + "%"
            ));
        }

        return predicates;
    }
}
