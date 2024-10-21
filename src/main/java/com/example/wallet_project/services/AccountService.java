package com.example.wallet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet_project.model.Account;
import com.example.wallet_project.repositories.AccountRepository;
import com.example.wallet_project.validation.AccountValidator;

import java.util.List;

@Service
public class AccountService {
	
	
    @Autowired
    private AccountRepository accountRepository;

    // create a new account
    public Account createAccount(Account account) {
        AccountValidator.validateAccount(account);
        return accountRepository.save(account);// Save the account to the database
    }

    // update an existing account by ID
    public Account updateAccount(Long id, Account account) {
    	// Retrieve the existing account or throw an exception if not found
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountBalance(account.getAccountBalance());
        existingAccount.setIBAN(account.getIBAN());
        AccountValidator.validateAccount(existingAccount);
        return accountRepository.save(existingAccount);
    }


    // delete an account by ID
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    // retrieve an account by ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // retrieve all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}