package com.example.wallet_project.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class TransactionValidator {
	
    @Autowired
    private TransactionRepository transactionRepository;

    public void validateWithdrawal(Account account, BigDecimal amount) {
    	 // Check if the withdrawal amount is within the specified range (100,000 to 10,000,000)
        if (amount.compareTo(new BigDecimal("100000")) < 0 || amount.compareTo(new BigDecimal("10000000")) > 0) {
            throw new RuntimeException("Withdrawal amount must be between 100,000 and 10,000,000 rials");
        }
        // Get the start of the current day
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        // Get the end of the current day
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Retrieve all transactions for the account today
        List<Transaction> transactionsToday = transactionRepository.findByAccountIdAndTransactionDateBetween(account.getId(), startOfDay, endOfDay);

        // the total amount withdrawn today
        BigDecimal totalWithdrawnToday = transactionsToday.stream()
                .filter(t -> "WITHDRAWAL".equals(t.getTransactionType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum the amounts

     // Set the daily withdrawal limit
        BigDecimal dailyLimit = new BigDecimal("10000000");
        // Check if adding the current withdrawal amount exceeds the daily limit
        if (totalWithdrawnToday.add(amount).compareTo(dailyLimit) > 0) {
            throw new RuntimeException("Daily withdrawal limit exceeded");
        }
    }
}