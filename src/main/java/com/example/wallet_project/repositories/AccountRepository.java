package com.example.wallet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet_project.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}