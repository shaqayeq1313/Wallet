package com.example.wallet_project.ValidationTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.validation.AccountValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountValidatorTest {

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("100000"));
    }

    @Test
    public void testValidateAccount_ValidAccount() {
        assertDoesNotThrow(() -> AccountValidator.validateAccount(account));
    }

    @Test
    public void testValidateAccount_MissingAccountNumber() {
        account.setAccountNumber(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AccountValidator.validateAccount(account);
        });
        assertEquals("Account number is required.", exception.getMessage());
    }

    @Test
    public void testValidateAccount_EmptyAccountNumber() {
        account.setAccountNumber("");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AccountValidator.validateAccount(account);
        });
        assertEquals("Account number is required.", exception.getMessage());
    }

    @Test
    public void testValidateAccount_MissingIBAN() {
        account.setIBAN(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AccountValidator.validateAccount(account);
        });
        assertEquals("IBAN is required.", exception.getMessage());
    }

    @Test
    public void testValidateAccount_EmptyIBAN() {
        account.setIBAN("");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AccountValidator.validateAccount(account);
        });
        assertEquals("IBAN is required.", exception.getMessage());
    }

    @Test
    public void testValidateAccount_InsufficientBalance() {
        account.setAccountBalance(new BigDecimal("9999"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AccountValidator.validateAccount(account);
        });
        assertEquals("Account balance must be at least 10,000 rials.", exception.getMessage());
    }
}