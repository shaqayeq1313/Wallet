package com.example.wallet_project.ServicesTest;

import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import com.example.wallet_project.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = createValidPerson();
    }

    @Test
    public void testCreatePerson() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person createdPerson = personService.createPerson(person);

        assertNotNull(createdPerson);
        assertEquals(person.getId(), createdPerson.getId());
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getLastName(), createdPerson.getLastName());
        assertEquals(person.getDateOfBirth(), createdPerson.getDateOfBirth());
        assertEquals(person.getGender(), createdPerson.getGender());
        assertEquals(person.getMilitaryServiceStatus(), createdPerson.getMilitaryServiceStatus());
        assertEquals(person.getEmail(), createdPerson.getEmail());
        assertEquals(person.getMobileNumber(), createdPerson.getMobileNumber());
        assertEquals(person.getNationalId(), createdPerson.getNationalId());

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void testUpdatePerson() {
        Person updatedPerson = createValidPerson();
        updatedPerson.setId(1L);
        updatedPerson.setNationalId("0061112380");
        updatedPerson.setFirstName("Ali");
        updatedPerson.setLastName("Seifi");
        updatedPerson.setDateOfBirth(LocalDate.now().minusYears(21));
        updatedPerson.setGender(Gender.MALE);
        updatedPerson.setMilitaryServiceStatus(MilitaryServiceStatus.EXEMPTED);
        updatedPerson.setEmail("seifi.Ali@gmail.com");
        updatedPerson.setMobileNumber("09123456787");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        Person result = personService.updatePerson(1L, updatedPerson);

        assertNotNull(result);
        assertEquals(updatedPerson.getId(), result.getId());
        assertEquals(updatedPerson.getFirstName(), result.getFirstName());
        assertEquals(updatedPerson.getLastName(), result.getLastName());
        assertEquals(updatedPerson.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatedPerson.getGender(), result.getGender());
        assertEquals(updatedPerson.getMilitaryServiceStatus(), result.getMilitaryServiceStatus());
        assertEquals(updatedPerson.getEmail(), result.getEmail());
        assertEquals(updatedPerson.getMobileNumber(), result.getMobileNumber());
        assertEquals(updatedPerson.getNationalId(), result.getNationalId());

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void testUpdatePerson_NotFound() {
        Person updatedPerson = createValidPerson();
        updatedPerson.setId(1L);
        updatedPerson.setNationalId("0061112380");
        updatedPerson.setFirstName("Ali");
        updatedPerson.setLastName("Seifi");
        updatedPerson.setDateOfBirth(LocalDate.now().minusYears(21));
        updatedPerson.setGender(Gender.MALE);
        updatedPerson.setMilitaryServiceStatus(MilitaryServiceStatus.EXEMPTED);
        updatedPerson.setEmail("seifi.Ali@gmail.com");
        updatedPerson.setMobileNumber("09123456787");
        

        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.updatePerson(1L, updatedPerson);
        });

        assertEquals("Person not found", exception.getMessage());
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeletePerson() {
        doNothing().when(personRepository).deleteById(1L);

        personService.deletePerson(1L);

        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePerson_NotFound() {
        doThrow(new IllegalArgumentException("Person not found")).when(personRepository).deleteById(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.deletePerson(1L);
        });

        assertEquals("Person not found", exception.getMessage());
        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetPersonById() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Person result = personService.getPersonById(1L);

        assertNotNull(result);
        assertEquals(person.getId(), result.getId());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getMilitaryServiceStatus(), result.getMilitaryServiceStatus());
        assertEquals(person.getEmail(), result.getEmail());
        assertEquals(person.getMobileNumber(), result.getMobileNumber());
        assertEquals(person.getNationalId(), result.getNationalId());

        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPersonById_NotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.getPersonById(1L);
        });

        assertEquals("Person not found", exception.getMessage());
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllPersons() {
        Person person2 = createValidPerson();
        person2.setId(2L);
        person2.setNationalId("0023983051");
        person2.setFirstName("Shaqayeq");
        person2.setLastName("Seifi");
        person2.setGender(Gender.FEMALE);
        person2.setMilitaryServiceStatus(null);
        person2.setEmail("seifi.shaqayeq13@gmail.com");
        person2.setMobileNumber("09123456788");

        List<Person> persons = Arrays.asList(person, person2);
        when(personRepository.findAll()).thenReturn(persons);

        List<Person> result = personService.getAllPersons();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(person));
        assertTrue(result.contains(person2));

        verify(personRepository, times(1)).findAll();
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