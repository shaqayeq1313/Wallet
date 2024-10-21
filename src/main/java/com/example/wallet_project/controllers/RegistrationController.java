package com.example.wallet_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import com.example.wallet_project.validation.PersonValidator;

/**
 * RegistrationController is a REST controller that handles person(user) registration requests.
 * It provides an endpoint for registering a new user by validating input data, 
 * checking for existing users, encoding the password, and saving the user to the database.
 */

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody Person person) {

        try {
            // Validate the person data
            PersonValidator.validatePerson(person);

            // Check if user already exists
            if (personRepository.findByEmail(person.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            // Encode password
            person.setPassword(passwordEncoder.encode(person.getPassword()));

            // Save user to database
            personRepository.save(person);

            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
