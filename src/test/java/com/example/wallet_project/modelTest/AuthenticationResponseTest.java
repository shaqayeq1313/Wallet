package com.example.wallet_project.modelTest;

import com.example.wallet_project.model.AuthenticationResponse;
import com.example.wallet_project.springSecurity.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationResponseTest {

    @Autowired
    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new User("seifi.Ali@gmail.com", "password123", new ArrayList<>());
    }

    @Test
    public void testAuthenticationResponse_ConstructorAndGetter() {
        String jwt = jwtUtil.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);

        assertEquals(jwt, authenticationResponse.getJwt());
    }
}
