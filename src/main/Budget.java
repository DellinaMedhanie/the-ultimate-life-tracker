package main;

import java.time.LocalDateTime;
import java.time.YearMonth;

// Model layer
public class Budget {
	/*
	 * budgetId: String -> Unique identifier auto-generated on creation
	 * userId: String -> Links the budget to the authenticated user
	 * category: String -> The spending category this budget applies to (e.g. Food & Dining)
	 * monthlyLimit: double -> The maximum amount the user wants to spend in this category for the month
	 * month: YearMonth -> Which month (e.g. 2026-07) this budget applies to
	 * createdAt: LocalDateTime -> Timestamp when the record was created in the system
	 */
	String budgetId;
	String userId;
	String category;
	double monthlyLimit;
	YearMonth month;
	LocalDateTime createdAt;

	public Budget(String userId, String category, double monthlyLimit, YearMonth month) {
		// create hash based on timestamp for it to be unique, same approach as Transaction
		int hash = LocalDateTime.now().hashCode();
		budgetId = Integer.toString(hash);
		this.userId = userId;
		this.category = category;
		this.monthlyLimit = monthlyLimit;
		this.month = month;
		this.createdAt = LocalDateTime.now();
	}

	// full constructor, used when re-hydrating a Budget from a saved line in budgets.txt
	public Budget(String budgetId, String userId, String category, double monthlyLimit, YearMonth month, LocalDateTime createdAt) {
		this.budgetId = budgetId;
		this.userId = userId;
		this.category = category;
		this.monthlyLimit = monthlyLimit;
		this.month = month;
		this.createdAt = createdAt;
	}

	public String getBudgetId() {
		return budgetId;
	}

	// no setter method for budgetId because it is a unique identifier

	public String getUserId() {
		return userId;
	}

	// no setter method for userId, same reasoning as Transaction: it's linked to the
	// user account and shouldn't be modified after creation

	public String getCategory() {
		return category;
	}

	public void setCategory(String newCategory) {
		category = newCategory;
	}

	public double getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(double newMonthlyLimit) {
		monthlyLimit = newMonthlyLimit;
	}

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth newMonth) {
		month = newMonth;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// how much of the budget is left, given how much has already been spent
	// in that category this month. Can be negative if the user is over budget.
	public double getRemainingAmount(double amountSpent) {
		return monthlyLimit - amountSpent;
	}

	// whether the amount spent so far in this category has exceeded the monthly limit
	public boolean isOverBudget(double amountSpent) {
		return amountSpent > monthlyLimit;
	}

	@Override
	public String toString() {
		return getBudgetId() + " | " + getUserId() + " | " + getCategory() + " | "
				+ getMonthlyLimit() + " | " + getMonth() + " | " + getCreatedAt();
	}
}
