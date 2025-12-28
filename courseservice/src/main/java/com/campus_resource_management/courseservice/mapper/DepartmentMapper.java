package com.campus_resource_management.courseservice.mapper;

import com.campus_resource_management.courseservice.constant.Department;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DepartmentMapper {

    private static final Map<String, Department> SHORT_TO_ENUM = new HashMap<>();
    private static final Map<Department, String> ENUM_TO_SHORT = new HashMap<>();

    static {
        SHORT_TO_ENUM.put("csc", Department.COMPUTER_SCIENCE);
        SHORT_TO_ENUM.put("swe", Department.SOFTWARE_ENGINEERING);
        SHORT_TO_ENUM.put("mat", Department.MATHEMATICS);
        SHORT_TO_ENUM.put("phy", Department.PHYSICS);
        SHORT_TO_ENUM.put("che", Department.CHEMISTRY);
        SHORT_TO_ENUM.put("bio", Department.BIOLOGY);
        SHORT_TO_ENUM.put("eco", Department.ECONOMICS);
        SHORT_TO_ENUM.put("bus", Department.BUSINESS);
        SHORT_TO_ENUM.put("psy", Department.PSYCHOLOGY);
        SHORT_TO_ENUM.put("edu", Department.EDUCATION);
        SHORT_TO_ENUM.put("his", Department.HISTORY);
        SHORT_TO_ENUM.put("lit", Department.LITERATURE);
        SHORT_TO_ENUM.put("phi", Department.PHILOSOPHY);
        SHORT_TO_ENUM.put("eng", Department.ENGINEERING);
        SHORT_TO_ENUM.put("med", Department.MEDICINE);
        SHORT_TO_ENUM.put("nur", Department.NURSING);
        SHORT_TO_ENUM.put("art", Department.ART);
        SHORT_TO_ENUM.put("mus", Department.MUSIC);
        SHORT_TO_ENUM.put("law", Department.LAW);
        SHORT_TO_ENUM.put("soc", Department.SOCIOLOGY);

        // reverse map
        SHORT_TO_ENUM.forEach((k, v) -> ENUM_TO_SHORT.put(v, k));
    }

    public Department toDepartment(String shortCode) {
        if (shortCode == null || shortCode.isBlank()) return null;
        return SHORT_TO_ENUM.get(shortCode.toLowerCase());
    }

    public String toShortCode(Department department) {
        if (department == null) return null;
        return ENUM_TO_SHORT.get(department).toUpperCase();
    }
}
