package com.example.wallet_project.RepositoriesTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Account account;
    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setNationalId("0061112380");
        person.setFirstName("Ali");
        person.setLastName("Seifi");
        person.setDateOfBirth(LocalDate.now().minusYears(20));
        person.setGender(Gender.MALE);
        person.setMilitaryServiceStatus(MilitaryServiceStatus.COMPLETED);
        person.setEmail("seifi.Ali@gmail.com");
        person.setMobileNumber("09123456789");

        entityManager.persist(person);
        entityManager.flush();

        account = createValidAccount();
        account.setPerson(person);

        entityManager.persist(account);
        entityManager.flush();
    }

    @Test
    public void testFindById() {
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        assertTrue(foundAccount.isPresent());
        assertEquals(account.getId(), foundAccount.get().getId());
        assertEquals(account.getAccountNumber(), foundAccount.get().getAccountNumber());
        assertEquals(account.getIBAN(), foundAccount.get().getIBAN());
        assertEquals(account.getAccountBalance(), foundAccount.get().getAccountBalance());
        assertEquals(account.getAccountCreationDate(), foundAccount.get().getAccountCreationDate());
        assertEquals(account.getPerson(), foundAccount.get().getPerson());
    }

    @Test
    public void testFindAll() {
        Account account2 = createValidAccount();
        account2.setAccountNumber("502229109448");
        account2.setIBAN("IR123456789012345678901235");
        account2.setAccountBalance(new BigDecimal("200000"));
        account2.setAccountCreationDate(LocalDate.now().minusDays(1));
        account2.setPerson(person); // Set the person for account2

        entityManager.persist(account2);
        entityManager.flush();

        List<Account> accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(account));
        assertTrue(accounts.contains(account2));
    }

    @Test
    public void testSave() {
        Account newAccount = createValidAccount();
        newAccount.setAccountNumber("502229109449");
        newAccount.setIBAN("IR123456789012345678901236");
        newAccount.setAccountBalance(new BigDecimal("300000"));
        newAccount.setAccountCreationDate(LocalDate.now().minusDays(2));
        newAccount.setPerson(person); // Set the person for newAccount

        Account savedAccount = accountRepository.save(newAccount);

        assertNotNull(savedAccount.getId());
        assertEquals(newAccount.getAccountNumber(), savedAccount.getAccountNumber());
        assertEquals(newAccount.getIBAN(), savedAccount.getIBAN());
        assertEquals(newAccount.getAccountBalance(), savedAccount.getAccountBalance());
        assertEquals(newAccount.getAccountCreationDate(), savedAccount.getAccountCreationDate());
        assertEquals(newAccount.getPerson(), savedAccount.getPerson());
    }

    @Test
    public void testDeleteById() {
        accountRepository.deleteById(account.getId());

        Optional<Account> deletedAccount = accountRepository.findById(account.getId());

        assertFalse(deletedAccount.isPresent());
    }

    private Account createValidAccount() {
        Account account = new Account();
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("100000"));
        account.setAccountCreationDate(LocalDate.now());
        account.setPerson(person); // Set the person for Account
        return account;
    }
}