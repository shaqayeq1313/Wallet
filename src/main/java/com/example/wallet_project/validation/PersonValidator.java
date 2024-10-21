package com.example.wallet_project.validation;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;


public class PersonValidator {
	
    public static void validatePerson(Person person) {
    	
        // Validate national ID, email, and mobile number
        if (person.getNationalId() == null || person.getNationalId().isEmpty()) {
            throw new IllegalArgumentException("National ID is required.");
        }

        if (person.getEmail() == null || person.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (person.getMobileNumber() == null || person.getMobileNumber().isEmpty()) {
            throw new IllegalArgumentException("Mobile number is required.");
        }


     // Validate military service status for males over 18 during creation
        if (person.getGender() == Gender.MALE && calculateAge(person.getDateOfBirth()) >= 18) {
            if (person.getMilitaryServiceStatus() == null) {
                throw new IllegalArgumentException("Military status is required for individuals above 18 years old. Possible values: " + Arrays.toString(MilitaryServiceStatus.values()));
            }
        } else {
            // If the person is under 18 or not male, military service status is not required
            person.setMilitaryServiceStatus(null);
        }
    }

    private static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}