package com.example.wallet_project.ControllerTest;

import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setId(1L);
        person.setEmail("seifi.Ali@gmail.com");
        person.setPassword("password123");
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        when(personRepository.findByEmail("seifi.Ali@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));

        verify(personRepository, times(1)).findByEmail("seifi.Ali@gmail.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        when(personRepository.findByEmail("seifi.Ali@gmail.com")).thenReturn(Optional.of(person));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"seifi.Ali@gmail.com\",\"password\":\"password123\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("User already exists"));

        verify(personRepository, times(1)).findByEmail("seifi.Ali@gmail.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void testRegisterUser_InvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalidEmail\",\"password\":\"short\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(personRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(personRepository, never()).save(any(Person.class));
    }
}