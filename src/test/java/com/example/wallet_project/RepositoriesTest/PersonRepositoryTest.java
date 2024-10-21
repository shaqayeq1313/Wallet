package com.example.wallet_project.RepositoriesTest;

import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = createValidPerson();
    }

    @Test
    public void testSavePerson() {
        Person savedPerson = personRepository.save(person);

        assertNotNull(savedPerson.getId());
        assertEquals(person.getFirstName(), savedPerson.getFirstName());
        assertEquals(person.getLastName(), savedPerson.getLastName());
        assertEquals(person.getDateOfBirth(), savedPerson.getDateOfBirth());
        assertEquals(person.getGender(), savedPerson.getGender());
        assertEquals(person.getMilitaryServiceStatus(), savedPerson.getMilitaryServiceStatus());
        assertEquals(person.getEmail(), savedPerson.getEmail());
        assertEquals(person.getMobileNumber(), savedPerson.getMobileNumber());
        assertEquals(person.getNationalId(), savedPerson.getNationalId());
    }

    @Test
    public void testFindPersonById() {
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> foundPerson = personRepository.findById(person.getId());

        assertTrue(foundPerson.isPresent());
        assertEquals(person.getId(), foundPerson.get().getId());
        assertEquals(person.getFirstName(), foundPerson.get().getFirstName());
        assertEquals(person.getLastName(), foundPerson.get().getLastName());
        assertEquals(person.getDateOfBirth(), foundPerson.get().getDateOfBirth());
        assertEquals(person.getGender(), foundPerson.get().getGender());
        assertEquals(person.getMilitaryServiceStatus(), foundPerson.get().getMilitaryServiceStatus());
        assertEquals(person.getEmail(), foundPerson.get().getEmail());
        assertEquals(person.getMobileNumber(), foundPerson.get().getMobileNumber());
        assertEquals(person.getNationalId(), foundPerson.get().getNationalId());
    }

    @Test
    public void testFindPersonById_NotFound() {
        Optional<Person> foundPerson = personRepository.findById(1L);

        assertFalse(foundPerson.isPresent());
    }

    @Test
    public void testFindPersonByEmail() {
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> foundPerson = personRepository.findByEmail(person.getEmail());

        assertTrue(foundPerson.isPresent());
        assertEquals(person.getId(), foundPerson.get().getId());
        assertEquals(person.getFirstName(), foundPerson.get().getFirstName());
        assertEquals(person.getLastName(), foundPerson.get().getLastName());
        assertEquals(person.getDateOfBirth(), foundPerson.get().getDateOfBirth());
        assertEquals(person.getGender(), foundPerson.get().getGender());
        assertEquals(person.getMilitaryServiceStatus(), foundPerson.get().getMilitaryServiceStatus());
        assertEquals(person.getEmail(), foundPerson.get().getEmail());
        assertEquals(person.getMobileNumber(), foundPerson.get().getMobileNumber());
        assertEquals(person.getNationalId(), foundPerson.get().getNationalId());
    }

    @Test
    public void testFindPersonByEmail_NotFound() {
        Optional<Person> foundPerson = personRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundPerson.isPresent());
    }

    @Test
    public void testFindAllPersons() {
        Person person2 = createValidPerson();
        person2.setId(2L);
        person2.setNationalId("0023983051");
        person2.setFirstName("Shaqayeq");
        person2.setLastName("Seifi");
        person2.setGender(Gender.FEMALE);
        person2.setMilitaryServiceStatus(null);
        person2.setEmail("seifi.shaqayeq13@gmail.com");
        person2.setMobileNumber("09123456788");

        entityManager.persist(person);
        entityManager.persist(person2);
        entityManager.flush();

        List<Person> persons = personRepository.findAll();

        assertEquals(2, persons.size());
        assertTrue(persons.contains(person));
        assertTrue(persons.contains(person2));
    }

    @Test
    public void testDeletePerson() {
        entityManager.persist(person);
        entityManager.flush();

        personRepository.deleteById(person.getId());

        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent());
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