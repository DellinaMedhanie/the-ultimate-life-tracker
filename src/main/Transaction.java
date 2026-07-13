package main;

import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Model layer
public class Transaction {
	/*
	 * transaction: String (UUID) -> Unique identifier auto-generated on creation
	 * userId: String -> Links the transaction to the authenticated user 
	 * type: Enum (INCOME/EXPENSE) -> Whether this is money received or money spent 
	 * amount: double -> Monetary value - always positive; type determines direction 
	 * category: String -> User-selected category (e.g. Food, Transport, Salary) 
	 * description: string -> Optional short note about the transaction 
	 * date: LocalDate -> Date the transaction occurred (not necessarily today)
	 * createdAt: LocalDateTime -> Timestamp when the record was created in the system 
	 *  
	 */
	String transaction; 
	String userId; 
	Transaction.Type type;
	double amount; 
	String category;
	String description; 
	LocalDate date; 
	LocalDateTime createdAt; 
	
	public enum Type {
		INCOME, 
		EXPENSE, 
		UNKNOWN
	}
	
	Transaction(String id, Transaction.Type transactionType, double transactionAmount, String transactionCategory, String transactionDescription, LocalDate transactionDate) {
		// create hash based on timestamp of localdate.now() for it to be unique
		int hash = LocalDateTime.now().hashCode();
		transaction = Integer.toString(hash);
		// user id needs to be passed into creating a transaction object 
		userId = id;
		type = transactionType;
		amount = transactionAmount;
		category = transactionCategory; 
		description = transactionDescription; 
		date = transactionDate; 
		createdAt = LocalDateTime.now();
	}
	
	public String getTransaction() {
		return transaction;
	}
	
	// no setter method for transaction because it is a unique identifier
	
	public String getUserId() {
		return userId;
	}
	
	// no setter method for userId because this is linked to user account and should not be modified 
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type newType) {
		type = newType;
	} 
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double newAmount) {
		amount = newAmount;
	} 
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String newCategory) {
		category = newCategory;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String newDescription) {
		description = newDescription;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate newDate) {
		date = newDate;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	// don't need a setter method for createdAt, since this is a unique timestamp based on when 
	// it was created and should not be modified 
	
	@Override
	public String toString() {
		return getTransaction() + " | " + getUserId() + " | " + getType() + 
				 " | " + getAmount() + " | " + getCategory() + " | " + getDescription() +
				 " | " + getDate() + " | " + getCreatedAt();
	}
}









