package com.example.wallet_project.SpringSecurityTest;

import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import com.example.wallet_project.springSecurity.CustomUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setId(1L);
        person.setEmail("seifi.Ali@gmail.com");
        person.setPassword("password123");
    }

    @Test
    public void testLoadUserByUsername_ValidUser() {
        when(personRepository.findByEmail("seifi.Ali@gmail.com")).thenReturn(Optional.of(person));

        UserDetails userDetails = userDetailsService.loadUserByUsername("seifi.Ali@gmail.com");

        assertNotNull(userDetails);
        assertEquals("seifi.Ali@gmail.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_InvalidUser() {
        when(personRepository.findByEmail("seifi.Ali@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("seifi.Ali@gmail.com"));
    }
}
