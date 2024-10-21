package com.example.wallet_project.ServicesTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.repositories.AccountRepository;
import com.example.wallet_project.services.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

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
    public void testCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(account.getId(), createdAccount.getId());
        assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber());
        assertEquals(account.getIBAN(), createdAccount.getIBAN());
        assertEquals(account.getAccountBalance(), createdAccount.getAccountBalance());
        assertEquals(account.getAccountCreationDate(), createdAccount.getAccountCreationDate());
        assertEquals(account.getPerson(), createdAccount.getPerson());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount() {
        Account updatedAccount = createValidAccount();
        updatedAccount.setId(1L);
        updatedAccount.setAccountNumber("502229109448");
        updatedAccount.setIBAN("IR123456789012345678901235");
        updatedAccount.setAccountBalance(new BigDecimal("200000"));
        updatedAccount.setAccountCreationDate(LocalDate.now().minusDays(1));
        updatedAccount.setPerson(person);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals(updatedAccount.getId(), result.getId());
        assertEquals(updatedAccount.getAccountNumber(), result.getAccountNumber());
        assertEquals(updatedAccount.getIBAN(), result.getIBAN());
        assertEquals(updatedAccount.getAccountBalance(), result.getAccountBalance());
        assertEquals(updatedAccount.getAccountCreationDate(), result.getAccountCreationDate());
        assertEquals(updatedAccount.getPerson(), result.getPerson());

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_NotFound() {
        Account updatedAccount = createValidAccount();
        updatedAccount.setId(1L);
        updatedAccount.setAccountNumber("502229109448");
        updatedAccount.setIBAN("IR123456789012345678901235");
        updatedAccount.setAccountBalance(new BigDecimal("200000"));
        updatedAccount.setAccountCreationDate(LocalDate.now().minusDays(1));
        updatedAccount.setPerson(person);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.updateAccount(1L, updatedAccount);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAccountById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
        assertEquals(account.getIBAN(), result.getIBAN());
        assertEquals(account.getAccountBalance(), result.getAccountBalance());
        assertEquals(account.getAccountCreationDate(), result.getAccountCreationDate());
        assertEquals(account.getPerson(), result.getPerson());

        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getAccountById(1L);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllAccounts() {
        Account account2 = createValidAccount();
        account2.setId(2L);
        account2.setAccountNumber("502229109449");
        account2.setIBAN("IR123456789012345678901236");
        account2.setAccountBalance(new BigDecimal("300000"));
        account2.setAccountCreationDate(LocalDate.now().minusDays(2));
        account2.setPerson(person);

        List<Account> accounts = Arrays.asList(account, account2);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(account));
        assertTrue(result.contains(account2));

        verify(accountRepository, times(1)).findAll();
    }

    private Account createValidAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("100000"));
        account.setAccountCreationDate(LocalDate.now());
        account.setPerson(person); // Set the person for the account
        return account;
    }
}