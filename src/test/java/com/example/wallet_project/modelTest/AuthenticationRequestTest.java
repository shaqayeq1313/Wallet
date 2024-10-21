package com.example.wallet_project.modelTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wallet_project.model.AuthenticationRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationRequestTest {

    @Test
    public void testAuthenticationRequest_ConstructorAndGetters() {
        String username = "seifi.Ali@gmail.com";
        String password = "password123";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        assertEquals(username, authenticationRequest.getUsername());
        assertEquals(password, authenticationRequest.getPassword());
    }

    @Test
    public void testAuthenticationRequest_Setters() {
        String username = "seifi.Ali@gmail.com";
        String password = "password123";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        assertEquals(username, authenticationRequest.getUsername());
        assertEquals(password, authenticationRequest.getPassword());
    }
}
