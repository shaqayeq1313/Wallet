package com.example.wallet_project.ControllerTest;

import com.example.wallet_project.controllers.PersonController;
import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private ObjectMapper objectMapper;

    private Person person;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        person = createValidPerson();
    }

    @Test
    public void testGetMilitaryStatusOptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/persons/military-status-options"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(MilitaryServiceStatus.values().length));
    }

    @Test
    public void testCreatePerson() throws Exception {
        when(personService.createPerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(person.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(person.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(person.getGender().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.militaryServiceStatus").value(person.getMilitaryServiceStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(person.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value(person.getMobileNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationalId").value(person.getNationalId()));

        verify(personService, times(1)).createPerson(any(Person.class));
    }

    @Test
    public void testCreatePerson_InvalidPerson() throws Exception {
        Person invalidPerson = createValidPerson();
        invalidPerson.setNationalId(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().isBadRequest());

        verify(personService, never()).createPerson(any(Person.class));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        when(personService.updatePerson(eq(1L), any(Person.class))).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(person.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(person.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(person.getGender().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.militaryServiceStatus").value(person.getMilitaryServiceStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(person.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value(person.getMobileNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationalId").value(person.getNationalId()));

        verify(personService, times(1)).updatePerson(eq(1L), any(Person.class));
    }

    @Test
    public void testUpdatePerson_InvalidPerson() throws Exception {
        Person invalidPerson = createValidPerson();
        invalidPerson.setNationalId(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().isBadRequest());

        verify(personService, never()).updatePerson(anyLong(), any(Person.class));
    }

    @Test
    public void testDeletePerson() throws Exception {
        doNothing().when(personService).deletePerson(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/persons/1"))
                .andExpect(status().isNoContent());

        verify(personService, times(1)).deletePerson(1L);
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