package com.example.wallet_project.ValidationTest;

import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.validation.PersonValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonValidatorTest {

    @Autowired
    private PersonValidator personValidator;

    @Test
    public void testValidatePerson_ValidPerson() {
        Person person = createValidPerson();
        assertDoesNotThrow(() -> personValidator.validatePerson(person));
    }

    @Test
    public void testValidatePerson_MissingNationalId() {
        Person person = createValidPerson();
        person.setNationalId(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("National ID is required.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_InvalidNationalId() {
        Person person = createValidPerson();
        person.setNationalId("12345"); // کمتر از 10 رقم
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("National ID must be 10 digits.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_MissingEmail() {
        Person person = createValidPerson();
        person.setEmail(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("Email is required.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_InvalidEmail() {
        Person person = createValidPerson();
        person.setEmail("invalid-email"); // بدون @
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_MissingMobileNumber() {
        Person person = createValidPerson();
        person.setMobileNumber(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("Mobile number is required.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_InvalidMobileNumber() {
        Person person = createValidPerson();
        person.setMobileNumber("12345"); // کمتر از 11 رقم
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("Mobile number must be 11 digits.", exception.getMessage());
    }

    @Test
    public void testValidatePerson_MissingMilitaryServiceStatusForMaleOver18() {
        Person person = createValidPerson();
        person.setMilitaryServiceStatus(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personValidator.validatePerson(person);
        });
        assertEquals("Military status is required for individuals above 18 years old. Possible values: " + Arrays.toString(MilitaryServiceStatus.values()), exception.getMessage());
    }

    @Test
    public void testValidatePerson_NotRequiredMilitaryServiceStatusForMaleUnder18() {
        Person person = createValidPerson();
        person.setDateOfBirth(LocalDate.now().minusYears(17));
        assertDoesNotThrow(() -> personValidator.validatePerson(person));
        assertNull(person.getMilitaryServiceStatus());
    }

    @Test
    public void testValidatePerson_NotRequiredMilitaryServiceStatusForFemale() {
        Person person = createValidPerson();
        person.setGender(Gender.FEMALE);
        assertDoesNotThrow(() -> personValidator.validatePerson(person));
        assertNull(person.getMilitaryServiceStatus());
    }

    private Person createValidPerson() {
    	 Person person = new Person();
         person.setId(1L);
         person.setNationalId("0061112380");
         person.setFirstName("Ali");
         person.setLastName("Seifi");
         person.setDateOfBirth(LocalDate.now().minusYears(20));
         person.setGender(Gender.MALE);
         person.setMilitaryServiceStatus(MilitaryServiceStatus.COMPLETED);
         person.setEmail("seifi.Ali@gmail.com");
         person.setMobileNumber("09123456789");
         return person;
         }
}