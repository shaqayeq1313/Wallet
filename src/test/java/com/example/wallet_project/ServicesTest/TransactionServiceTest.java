package com.example.wallet_project.ServicesTest;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.model.Transaction;
import com.example.wallet_project.repositories.AccountRepository;
import com.example.wallet_project.repositories.TransactionRepository;
import com.example.wallet_project.services.TransactionService;
import com.example.wallet_project.validation.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionValidator transactionValidator;

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
    public void testDeposit() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction transaction = transactionService.deposit(1L, new BigDecimal("500000"));

        assertNotNull(transaction);
        assertEquals(new BigDecimal("1500000"), account.getAccountBalance());
        assertEquals("DEPOSIT", transaction.getTransactionType());
        assertEquals("SUCCESS", transaction.getTransactionStatus());
        assertNotNull(transaction.getDescription());
        assertNotNull(transaction.getReferenceId());
        assertNotNull(transaction.getFee());

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testWithdraw() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction transaction = transactionService.withdraw(1L, new BigDecimal("500000"));

        assertNotNull(transaction);
        assertEquals(new BigDecimal("500000"), account.getAccountBalance());
        assertEquals("WITHDRAWAL", transaction.getTransactionType());
        assertEquals("SUCCESS", transaction.getTransactionStatus());
        assertNotNull(transaction.getDescription());
        assertNotNull(transaction.getReferenceId());
        assertNotNull(transaction.getFee());

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testGetTransactionsByAccountId() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccount(account)).thenReturn(Arrays.asList(
                new Transaction(1L, LocalDateTime.now(), new BigDecimal("500000"), "WITHDRAWAL", "SUCCESS", account, "Test transaction", "ref1", new BigDecimal("1000")),
                new Transaction(2L, LocalDateTime.now(), new BigDecimal("500000"), "DEPOSIT", "SUCCESS", account, "Test transaction", "ref2", new BigDecimal("1000"))
        ));

        List<Transaction> transactions = transactionService.getTransactionsByAccountId(1L);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findByAccount(account);
    }
}