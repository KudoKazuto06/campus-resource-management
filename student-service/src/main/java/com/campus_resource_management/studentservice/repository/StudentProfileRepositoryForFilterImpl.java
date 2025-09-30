package com.campus_resource_management.studentservice.repository;

import com.campus_resource_management.studentservice.constant.StudentStatus;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.entity.StudentProfile;
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
public class StudentProfileRepositoryForFilterImpl implements StudentProfileRepositoryForFilter {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<StudentProfile> filterStudentProfile(FilterStudentProfileRequest filterStudentProfileRequest, Pageable pageable){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StudentProfile> cq = cb.createQuery(StudentProfile.class);
        Root<StudentProfile> root = cq.from(StudentProfile.class);

        List<Predicate> predicates = buildPredicates(cb, root, filterStudentProfileRequest);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

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

        TypedQuery<StudentProfile> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<StudentProfile> results = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StudentProfile> countRoot = countQuery.from(StudentProfile.class);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, filterStudentProfileRequest);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<StudentProfile> root, FilterStudentProfileRequest filter) {
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

        if (filter.getGender() != null) {
            predicates.add(cb.equal(root.get("gender"), filter.getGender()));
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

        if (filter.getDegreeType() != null) {
            predicates.add(cb.equal(root.get("degreeType"), filter.getDegreeType()));
        }

        if (filter.getMajor() != null) {
            predicates.add(cb.like(cb.lower(root.get("major")), "%" + filter.getMajor().toLowerCase() + "%"));
        }

        if (filter.getGpaFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("gpa"), filter.getGpaFrom()));
        }

        if (filter.getGpaTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("gpa"), filter.getGpaTo()));
        }

        if (filter.getStudentStatus() != null) {
            predicates.add(cb.equal(root.get("studentStatus"), StudentStatus.valueOf(filter.getStudentStatus())));
        }

        if (filter.getYearOfStudyFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("yearOfStudy"), filter.getYearOfStudyFrom()));
        }

        if (filter.getYearOfStudyTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("yearOfStudy"), filter.getYearOfStudyTo()));
        }

        return predicates;
    }


}
