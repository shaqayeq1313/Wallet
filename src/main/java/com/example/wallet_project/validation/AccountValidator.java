package com.example.wallet_project.validation;

import java.math.BigDecimal;

import com.example.wallet_project.model.Account;

public class AccountValidator {
    public static void validateAccount(Account account) {
    	
        // Validate account number and IBAN(Sheba)
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number is required.");
        }

        if (account.getIBAN() == null || account.getIBAN().isEmpty()) {
            throw new IllegalArgumentException("IBAN is required.");
        }

        // Validate minimum account balance
        if (account.getAccountBalance().compareTo(new BigDecimal("10000")) < 0) {
            throw new IllegalArgumentException("Account balance must be at least 10,000 rials.");
        }
    }
}