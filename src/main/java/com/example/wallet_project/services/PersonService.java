package com.example.wallet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.PersonRepository;
import com.example.wallet_project.validation.PersonValidator;

import java.util.List;


@Service
public class PersonService {
	
    @Autowired
    private PersonRepository personRepository;

    public Person createPerson(Person person) {
        PersonValidator.validatePerson(person);
        return personRepository.save(person);
    }

    public Person updatePerson(Long id, Person person) {
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setDateOfBirth(person.getDateOfBirth());
        existingPerson.setGender(person.getGender());
        existingPerson.setMilitaryServiceStatus(person.getMilitaryServiceStatus());
        existingPerson.setEmail(person.getEmail());
        existingPerson.setMobileNumber(person.getMobileNumber());
        PersonValidator.validatePerson(existingPerson);
        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
}