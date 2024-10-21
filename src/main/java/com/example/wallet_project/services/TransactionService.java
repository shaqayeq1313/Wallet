package com.example.wallet_project.services;

import org.springframework.stereotype.Service;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.repositories.AccountRepository;
import com.example.wallet_project.repositories.TransactionRepository;
import com.example.wallet_project.validation.TransactionValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionValidator transactionValidator;


    // dependency injection of repositories and validator
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionValidator = transactionValidator;
    }

    // Method to handle deposit transactions
    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountBalance(account.getAccountBalance().add(amount));
        accountRepository.save(account);

        // Create a new transaction object
        Transaction transaction = new Transaction();
        transaction.setAmount(amount); // Set the transaction amount
        transaction.setTransactionDate(LocalDateTime.now()); // Set the current date and time
        transaction.setTransactionType("DEPOSIT"); // Set the transaction type to DEPOSIT
        transaction.setTransactionStatus("SUCCESS"); // Set the transaction status to SUCCESS
        transaction.setAccount(account); // Associate the transaction with the account
        transaction.setReferenceId(UUID.randomUUID().toString()); // Generate a unique reference ID
        return transactionRepository.save(transaction); // Save and return the transaction
    }

    // Method to handle withdrawal transactions
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        // Validate the withdrawal
        transactionValidator.validateWithdrawal(account, amount);

        // Check if the withdrawal will leave a minimum balance of 10,000
        if (account.getAccountBalance().subtract(amount).compareTo(new BigDecimal("10000")) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setAccountBalance(account.getAccountBalance().subtract(amount));
        accountRepository.save(account);

        // Create a new transaction object
        Transaction transaction = new Transaction();
        transaction.setAmount(amount); // Set the transaction amount
        transaction.setTransactionDate(LocalDateTime.now()); // Set the current date and time
        transaction.setTransactionType("WITHDRAWAL"); // Set the transaction type to WITHDRAWAL
        transaction.setTransactionStatus("SUCCESS"); // Set the transaction status to SUCCESS
        transaction.setAccount(account); // Associate the transaction with the account
        transaction.setReferenceId(UUID.randomUUID().toString()); // Generate a unique reference ID
        return transactionRepository.save(transaction); // Save and return the transaction
    }

    // retrieve transactions for a specific account
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account);
    }
}