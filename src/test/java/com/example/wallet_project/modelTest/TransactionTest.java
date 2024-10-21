package com.example.wallet_project.modelTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Transaction transaction;
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("100000"));
        account.setAccountCreationDate(LocalDateTime.now().toLocalDate());

        transaction = createValidTransaction();
        transaction.setAccount(account);
    }

    @Test
    public void testTransactionGettersAndSetters() {
        assertEquals(1L, transaction.getId());
        assertEquals(LocalDateTime.now(), transaction.getTransactionDate());
        assertEquals(new BigDecimal("500000"), transaction.getAmount());
        assertEquals("DEPOSIT", transaction.getTransactionType());
        assertEquals("SUCCESS", transaction.getTransactionStatus());
        assertEquals(account, transaction.getAccount());
        assertEquals("Test transaction", transaction.getDescription());
        assertNotNull(transaction.getReferenceId());
        assertEquals(new BigDecimal("1000"), transaction.getFee());
    }

    @Test
    public void testTransactionToString() {
        String expectedToString = "Transaction [id=1, transactionDate=" + LocalDateTime.now() + ", amount=500000, transactionType=DEPOSIT, transactionStatus=SUCCESS, account=" + account + ", description=Test transaction, referenceId=" + transaction.getReferenceId() + ", fee=1000]";
        assertEquals(expectedToString, transaction.toString());
    }

    private Transaction createValidTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(new BigDecimal("500000"));
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionStatus("SUCCESS");
        transaction.setDescription("Test transaction");
        transaction.setReferenceId(UUID.randomUUID().toString());
        transaction.setFee(new BigDecimal("1000"));
        return transaction;
    }
}
