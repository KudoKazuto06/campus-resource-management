package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import org.springframework.transaction.annotation.Transactional;

public class StudentProfileServiceImpl {

    private final StudentProfileRepository profileRepository;

    public StudentProfileServiceImpl(StudentProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public String generateUniqueSchoolEmail(String firstName, String lastName){
        String baseEmail =
                (firstName.replaceAll("\\s+", "") + "."
                        + lastName.replaceAll("\\s+", "")).toLowerCase();
        String email = baseEmail + "@school.com";
        int counter = 1;

        while (profileRepository.findBySchoolEmail(email).isPresent()) {
            email = baseEmail + counter + "@school.com";
            counter++;
        }
        return email;
    }

    @Transactional
    public StudentProfile createProfile(StudentProfile profile){
        if (profile.getSchoolEmail() == null || profile.getSchoolEmail().isEmpty()) {
            String uniqueSchoolEmail = generateUniqueSchoolEmail(profile.getFirstName(), profile.getLastName());
            profile.setSchoolEmail(uniqueSchoolEmail);
        }
        return profileRepository.save(profile);
    }

}
