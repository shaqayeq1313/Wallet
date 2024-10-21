package com.example.wallet_project.ValidationTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.repositories.TransactionRepository;
import com.example.wallet_project.validation.TransactionValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionValidatorTest {

    @Autowired
    private TransactionValidator transactionValidator;

    @MockBean
    private TransactionRepository transactionRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("1000000"));
        account.setAccountCreationDate(LocalDateTime.now().toLocalDate());
    }

    @Test
    public void testValidateWithdrawal_ValidAmount() {
        when(transactionRepository.findByAccountIdAndTransactionDateBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(
                        new Transaction(1L, LocalDateTime.now(), new BigDecimal("100000"), "WITHDRAWAL", "SUCCESS", account, "Test transaction", "ref1", new BigDecimal("1000"))
                ));

        assertDoesNotThrow(() -> transactionValidator.validateWithdrawal(account, new BigDecimal("500000")));
    }

    @Test
    public void testValidateWithdrawal_InvalidAmount() {
        assertThrows(RuntimeException.class, () -> transactionValidator.validateWithdrawal(account, new BigDecimal("5000")));
    }

    @Test
    public void testValidateWithdrawal_DailyLimitExceeded() {
        when(transactionRepository.findByAccountIdAndTransactionDateBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(
                        new Transaction(1L, LocalDateTime.now(), new BigDecimal("9000000"), "WITHDRAWAL", "SUCCESS", account, "Test transaction", "ref1", new BigDecimal("1000"))
                ));

        assertThrows(RuntimeException.class, () -> transactionValidator.validateWithdrawal(account, new BigDecimal("2000000")));
    }
}