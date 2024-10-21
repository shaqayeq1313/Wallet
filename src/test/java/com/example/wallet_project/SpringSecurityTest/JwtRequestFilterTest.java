package com.example.wallet_project.SpringSecurityTest;

import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import com.example.wallet_project.springSecurity.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtRequestFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Person person;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setId(1L);
        person.setEmail("seifi.Ali@gmail.com");
        person.setPassword("password123");

        userDetails = new User(person.getEmail(), person.getPassword(), new ArrayList<>());
    }

    @Test
    public void testJwtRequestFilter_ValidToken() throws Exception {
        String validToken = jwtUtil.generateToken(userDetails);
        String username = "seifi.Ali@gmail.com";

        when(jwtUtil.extractUsername(validToken)).thenReturn(username);
        when(personRepository.findByEmail(username)).thenReturn(Optional.of(person));
        when(jwtUtil.validateToken(validToken, userDetails)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/secure")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(jwtUtil, times(1)).extractUsername(validToken);
        verify(personRepository, times(1)).findByEmail(username);
        verify(jwtUtil, times(1)).validateToken(validToken, userDetails);
    }

    @Test
    public void testJwtRequestFilter_InvalidToken() throws Exception {
        String invalidToken = "invalidToken";
        String username = "seifi.Ali@gmail.com";

        when(jwtUtil.extractUsername(invalidToken)).thenReturn(username);
        when(personRepository.findByEmail(username)).thenReturn(Optional.of(person));
        when(jwtUtil.validateToken(invalidToken, userDetails)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/secure")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(jwtUtil, times(1)).extractUsername(invalidToken);
        verify(personRepository, times(1)).findByEmail(username);
        verify(jwtUtil, times(1)).validateToken(invalidToken, userDetails);
    }
}