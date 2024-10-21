package com.example.wallet_project.ControllerTest;

import com.example.wallet_project.controllers.AccountController;
import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Gender;
import com.example.wallet_project.model.MilitaryServiceStatus;
import com.example.wallet_project.model.Person;
import com.example.wallet_project.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;
    private Account account;
    private Person person;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
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
    public void testCreateAccount() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(account.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IBAN").value(account.getIBAN()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountBalance").value(account.getAccountBalance().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountCreationDate").value(account.getAccountCreationDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.person.id").value(person.getId()));

        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        Account updatedAccount = createValidAccount();
        updatedAccount.setId(1L);
        updatedAccount.setAccountNumber("502229109448");
        updatedAccount.setIBAN("IR123456789012345678901235");
        updatedAccount.setAccountBalance(new BigDecimal("200000"));
        updatedAccount.setAccountCreationDate(LocalDate.now().minusDays(1));
        updatedAccount.setPerson(person);

        when(accountService.updateAccount(eq(1L), any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedAccount.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(updatedAccount.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IBAN").value(updatedAccount.getIBAN()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountBalance").value(updatedAccount.getAccountBalance().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountCreationDate").value(updatedAccount.getAccountCreationDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.person.id").value(person.getId()));

        verify(accountService, times(1)).updateAccount(eq(1L), any(Account.class));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(1L);
    }

    @Test
    public void testGetAccountById() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(account.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IBAN").value(account.getIBAN()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountBalance").value(account.getAccountBalance().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountCreationDate").value(account.getAccountCreationDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.person.id").value(person.getId()));

        verify(accountService, times(1)).getAccountById(1L);
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