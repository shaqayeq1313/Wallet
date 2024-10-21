package com.example.wallet_project.ControllerTest;

import com.example.wallet_project.controllers.TransactionController;
import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;
    private Transaction transaction;
    private Account account;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("1000000"));
        account.setAccountCreationDate(LocalDateTime.now().toLocalDate());

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(new BigDecimal("500000"));
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionStatus("SUCCESS");
        transaction.setAccount(account);
        transaction.setDescription("Test transaction");
        transaction.setReferenceId("ref1");
        transaction.setFee(new BigDecimal("1000"));
    }

    @Test
    @WithMockUser(username = "user", password = "password123", roles = "USER")
    public void testDeposit() throws Exception {
        when(transactionService.deposit(1L, new BigDecimal("500000"))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/deposit/1")
                        .param("amount", "500000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(transaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(transaction.getAmount().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value(transaction.getTransactionType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus").value(transaction.getTransactionStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Description").value(transaction.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ReferenceId").value(transaction.getReferenceId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Fee").value(transaction.getFee()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account.id").value(account.getId()));

        verify(transactionService, times(1)).deposit(1L, new BigDecimal("500000"));
    }

    @Test
    @WithMockUser(username = "user", password = "password123", roles = "USER")
    public void testWithdraw() throws Exception {
        when(transactionService.withdraw(1L, new BigDecimal("500000"))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/withdraw/1")
                        .param("amount", "500000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(transaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(transaction.getAmount().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType").value(transaction.getTransactionType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus").value(transaction.getTransactionStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Description").value(transaction.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ReferenceId").value(transaction.getReferenceId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Fee").value(transaction.getFee()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account.id").value(account.getId()));

        verify(transactionService, times(1)).withdraw(1L, new BigDecimal("500000"));
    }
    
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testWithdraw_InvalidAmount() throws Exception {
    	 when(transactionService.withdraw(1L, new BigDecimal("50000"))).thenReturn(transaction); // Invalid amount

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/withdraw/1")
                      .param("amount", "50000")
                      .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Withdrawal amount must be between 100,000 and 10,000,000 rials."));

        // Ensure that the withdraw method was not called since the amount is invalid
        verify(transactionService, times(0)).withdraw(any(Long.class), any(BigDecimal.class));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetTransactionsByAccountId() throws Exception {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getTransactionsByAccountId(1L)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(transaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(transaction.getAmount().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType").value(transaction.getTransactionType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionStatus").value(transaction.getTransactionStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].Description").value(transaction.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ReferenceId").value(transaction.getReferenceId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].Fee").value(transaction.getFee()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].account.id").value(account.getId()));

        verify(transactionService, times(1)).getTransactionsByAccountId(1L);
    }
}