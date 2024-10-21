package com.example.wallet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet_project.model.Person;

import java.util.Optional;

/**
 * PersonRepository is a Spring Data JPA repository interface for managing Person entities.
 * It extends JpaRepository, providing CRUD operations for Person objects identified by their unique ID.
 * This interface includes a method to find a Person by their email.
 */

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}
