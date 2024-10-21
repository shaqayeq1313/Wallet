package com.example.wallet_project.RepositoriesTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.repositories.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account account;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setAccountNumber("502229109447");
        account.setIBAN("IR123456789012345678901234");
        account.setAccountBalance(new BigDecimal("1000000"));
        account.setAccountCreationDate(LocalDateTime.now().toLocalDate());

        entityManager.persist(account);
        entityManager.flush();

        transaction1 = createTransaction(account, LocalDateTime.now().minusHours(1), new BigDecimal("500000"), "DEPOSIT", "SUCCESS", "Transaction Test", "ref1", new BigDecimal("1000"));
        transaction2 = createTransaction(account, LocalDateTime.now(), new BigDecimal("300000"), "WITHDRAWAL", "SUCCESS",  "Transaction Test", "ref2", new BigDecimal("1000"));

        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.flush();
    }

    @Test
    public void testFindByAccountIdAndTransactionDateBetween() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Transaction> transactions = transactionRepository.findByAccountIdAndTransactionDateBetween(account.getId(), startOfDay, endOfDay);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    public void testFindByAccount() {
        List<Transaction> transactions = transactionRepository.findByAccount(account);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    private Transaction createTransaction(Account account, LocalDateTime transactionDate, BigDecimal amount, String transactionType, String transactionStatus, 
    		String description, String referenceId, BigDecimal fee) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionDate(transactionDate);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setDescription(description);
        transaction.setReferenceId(referenceId);
        transaction.setFee(fee);
        return transaction;
    }
}