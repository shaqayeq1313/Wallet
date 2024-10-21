package com.example.wallet_project.modelTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account account;
    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setId(1L);
        person.setNationalId("0061112380");
        person.setFirstName("Ali");
        person.setLastName("Seifi");
        person.setDateOfBirth(LocalDate.now().minusYears(20));
        person.setGender(Gender.MALE);
        person.setMilitaryServiceStatus(MilitaryServiceStatus.COMPLETED);
        person.setEmail("seifi.Ali@gmail.com");
        person.setMobileNumber("09123456789");

        account = createValidAccount();
        account.setPerson(person);
    }

    @Test
    public void testAccountGettersAndSetters() {
        assertEquals(1L, account.getId());
        assertEquals("502229109447", account.getAccountNumber());
        assertEquals(new BigDecimal("100000"), account.getAccountBalance());
        assertEquals(LocalDate.now(), account.getAccountCreationDate());
        assertEquals("IR123456789012345678901234", account.getIBAN());
        assertEquals(person, account.getPerson());
    }

    @Test
    public void testAccountToString() {
        String expectedToString = "Account [id=1, accountNumber=502229109447, accountBalance=100000, accountCreationDate=" + LocalDate.now() + ", IBAN=IR123456789012345678901234, person=" + person + "]";
        assertEquals(expectedToString, account.toString());
    }

    private Account createValidAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("100000"));
        account.setAccountCreationDate(LocalDate.now());
        return account;
    }
}