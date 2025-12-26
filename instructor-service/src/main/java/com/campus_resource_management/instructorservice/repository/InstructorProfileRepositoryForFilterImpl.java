package com.campus_resource_management.instructorservice.repository;

import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InstructorProfileRepositoryForFilterImpl
        implements InstructorProfileRepositoryForFilter {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<InstructorProfile> filterInstructorProfile(
            FilterInstructorProfileRequest filter,
            Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InstructorProfile> cq = cb.createQuery(InstructorProfile.class);
        Root<InstructorProfile> root = cq.from(InstructorProfile.class);

        List<Predicate> predicates = buildPredicates(cb, root, filter);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting
        List<Order> orders = pageable.getSort().stream().map(order -> {
            String property = order.getProperty();
            boolean isAsc = order.isAscending();

            if (property.equals("fullName")) {
                Expression<String> fullNameExpr = cb.concat(
                        cb.concat(cb.lower(root.get("firstName")), " "),
                        cb.lower(root.get("lastName"))
                );
                return isAsc ? cb.asc(fullNameExpr) : cb.desc(fullNameExpr);
            } else {
                return isAsc ? cb.asc(root.get(property)) : cb.desc(root.get(property));
            }
        }).toList();

        cq.orderBy(orders);

        TypedQuery<InstructorProfile> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<InstructorProfile> results = query.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<InstructorProfile> countRoot = countQuery.from(InstructorProfile.class);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, filter);
        countQuery.select(cb.count(countRoot))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<InstructorProfile> root,
            FilterInstructorProfileRequest filter) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getIdentityId() != null) {
            predicates.add(cb.equal(root.get("identityId"), filter.getIdentityId()));
        }

        if (filter.getFullName() != null) {
            Expression<String> fullNameExpr = cb.concat(
                    cb.concat(cb.lower(root.get("firstName")), " "),
                    cb.lower(root.get("lastName"))
            );
            predicates.add(cb.like(fullNameExpr, "%" + filter.getFullName().toLowerCase() + "%"));
        }

        if (filter.getDepartment() != null) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get("department")),
                            "%" + filter.getDepartment().toLowerCase() + "%"
                    )
            );
        }

        if (filter.getGender() != null) {
            predicates.add(cb.equal(root.get("gender"), filter.getGender()));
        }

        if (filter.getAcademicRank() != null) {
            predicates.add(cb.equal(root.get("academicRank"), filter.getAcademicRank()));
        }

        if (filter.getEmploymentStatus() != null) {
            predicates.add(cb.equal(root.get("employmentStatus"), filter.getEmploymentStatus()));
        }

        if (filter.getDateOfBirthFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), filter.getDateOfBirthFrom()));
        }

        if (filter.getDateOfBirthTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("dateOfBirth"), filter.getDateOfBirthTo()));
        }

        if (filter.getEmail() != null) {
            predicates.add(cb.equal(root.get("email"), filter.getEmail()));
        }

        if (filter.getSchoolEmail() != null) {
            predicates.add(cb.equal(root.get("schoolEmail"), filter.getSchoolEmail()));
        }

        if (filter.getHireDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), filter.getHireDateFrom()));
        }

        if (filter.getHireDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("dateOfBirth"), filter.getHireDateTo()));
        }

        return predicates;
    }
}
