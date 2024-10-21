package com.example.wallet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface includes methods to find transactions by account ID within a date range
 * and to retrieve all transactions associated with a specific account.
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<Transaction> findByAccount(Account account);
}