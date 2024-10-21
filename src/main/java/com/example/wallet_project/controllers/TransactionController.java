package com.example.wallet_project.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.services.TransactionService;

/**
 * TransactionController is a REST controller that manages transactions related to person accounts.
 * It provides endpoints for depositing and withdrawing funds, and retrieving transaction history.
 * Each method ensures that the account ID matches the authenticated person's ID, enhancing security.
 */

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit/{accountId}")
    @PreAuthorize("#accountId == authentication.principal.id")
    public ResponseEntity<Transaction> deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.deposit(accountId, amount));
    }

    @PostMapping("/withdraw/{accountId}")
    @PreAuthorize("#accountId == authentication.principal.id")
    public ResponseEntity<?> withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("100000")) < 0 || amount.compareTo(new BigDecimal("10000000")) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal amount must be between 100,000 and 10,000,000 rials.");
        }
        return ResponseEntity.ok(transactionService.withdraw(accountId, amount));
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("#accountId == authentication.principal.id")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
}