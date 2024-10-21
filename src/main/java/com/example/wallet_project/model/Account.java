package com.example.wallet_project.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal accountBalance;

    @Column(nullable = false)
    private LocalDate accountCreationDate;
  
    // Shomare sheba
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[A-Z]{2}[0-9]{24}$")
    @JsonProperty("IBAN")
    private String IBAN;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    public Account() {
	}
    
	public Account(Long id, String accountNumber, BigDecimal accountBalance, LocalDate accountCreationDate,String IBAN, Person person) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
		this.accountCreationDate = accountCreationDate;
		this.IBAN = IBAN;
		this.person = person;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public LocalDate getAccountCreationDate() {
		return accountCreationDate;
	}

	public void setAccountCreationDate(LocalDate accountCreationDate) {
		this.accountCreationDate = accountCreationDate;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String IBAN) {
		this.IBAN = IBAN;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber + ", accountBalance=" + accountBalance
				+ ", accountCreationDate=" + accountCreationDate + ", IBAN=" + IBAN + ", person=" + person + "]";
	}

    
}
