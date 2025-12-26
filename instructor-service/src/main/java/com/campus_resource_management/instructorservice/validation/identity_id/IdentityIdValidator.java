package com.campus_resource_management.instructorservice.validation.identity_id;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentityIdValidator
        implements ConstraintValidator<
        com.campus_resource_management.instructorservice.validation.identity_id.ValidIdentityId, String> {
    private static final Pattern CCCD_PATTERN = Pattern.compile("^(\\d{3})(\\d)(\\d{2})(\\d{6})$");

    // List of valid Vietnamese province codes (from 001 to 096)
    private static final Set<String> VALID_PROVINCE_CODES =
            Set.of(
                    "001", "002", "004", "006", "008", "010", "011", "012", "014", "015", "017", "019", "020",
                    "022", "024", "025", "026", "027", "030", "031", "033", "034", "035", "036", "037", "038",
                    "040", "042", "044", "045", "046", "048", "049", "051", "052", "054", "056", "058", "060",
                    "062", "064", "066", "067", "068", "070", "072", "074", "075", "077", "079", "080", "082",
                    "083", "084", "086", "087", "089", "091", "092", "093", "094", "095", "096");

    @Override
    public boolean isValid(String identityId, ConstraintValidatorContext context) {
        if (identityId == null || identityId.isBlank()) {
            return true;
        }
        if (!CCCD_PATTERN.matcher(identityId).matches()) {
            return false;
        }

        Matcher matcher = CCCD_PATTERN.matcher(identityId);
        if (!matcher.matches()) return false;

        String provinceCode = matcher.group(1);
        int genderCenturyCode = Integer.parseInt(matcher.group(2));
        int yearSuffix = Integer.parseInt(matcher.group(3));

        // Validate province code
        if (!VALID_PROVINCE_CODES.contains(provinceCode)) return false;

        // Determine century based on gender-century code
        int century =
                switch (genderCenturyCode) {
                    case 0, 1 -> 1900; // 20th century: 0 = male, 1 = female
                    case 2, 3 -> 2000; // 21st century: 2 = male, 3 = female
                    default -> -1;
                };
        if (century == -1) return false;

        // Validate birth year
        int fullBirthYear = century + yearSuffix;
        int currentYear = LocalDate.now().getYear();

        return fullBirthYear >= 1900 && fullBirthYear <= currentYear;
    }
}