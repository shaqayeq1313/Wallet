package com.example.wallet_project.modelTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonTest {

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        person.setId(id);
        assertEquals(id, person.getId());
    }

    @Test
    public void testSetAndGetNationalId() {
        String nationalId = "0061112380";
        person.setNationalId(nationalId);
        assertEquals(nationalId, person.getNationalId());
    }

    @Test
    public void testSetAndGetFirstName() {
        String firstName = "Ali";
        person.setFirstName(firstName);
        assertEquals(firstName, person.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        String lastName = "Seifi";
        person.setLastName(lastName);
        assertEquals(lastName, person.getLastName());
    }

    @Test
    public void testSetAndGetDateOfBirth() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(20);
        person.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, person.getDateOfBirth());
    }

    @Test
    public void testSetAndGetGender() {
        Gender gender = Gender.MALE;
        person.setGender(gender);
        assertEquals(gender, person.getGender());
    }

    @Test
    public void testSetAndGetMilitaryServiceStatus() {
        MilitaryServiceStatus militaryServiceStatus = MilitaryServiceStatus.COMPLETED;
        person.setMilitaryServiceStatus(militaryServiceStatus);
        assertEquals(militaryServiceStatus, person.getMilitaryServiceStatus());
    }

    @Test
    public void testSetAndGetEmail() {
        String email = "seifi.Ali@gmail.com";
        person.setEmail(email);
        assertEquals(email, person.getEmail());
    }

    @Test
    public void testSetAndGetMobileNumber() {
        String mobileNumber = "09123456789";
        person.setMobileNumber(mobileNumber);
        assertEquals(mobileNumber, person.getMobileNumber());
    }

    @Test
    public void testSetAndGetPassword() {
        String password = "password123";
        person.setPassword(password);
        assertEquals(password, person.getPassword());
    }

    @Test
    public void testToString() {
        person = new Person(1L, "0061112380", "Ali", "Seifi", LocalDate.now().minusYears(20), Gender.MALE, MilitaryServiceStatus.COMPLETED, "seifi.Ali@gmail.com", "09123456789", "password123");
        String expectedToString = "Person [id=1, nationalId=0061112380, firstName=Ali, lastName=Seifi, dateOfBirth=" + LocalDate.now().minusYears(20) + ", gender=MALE, militaryServiceStatus=COMPLETED, email=seifi.Ali@gmail.com, mobileNumber=09123456789, password=password123]";
        assertEquals(expectedToString, person.toString());
    }
}