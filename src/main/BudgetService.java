package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Service Layer
public class BudgetService {

	// creates budgets.txt for the user if it doesn't already exist
	private void createFile(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				// make sure the "files/<username>" directory exists too,
				// in case this is the very first file created for the user
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (Exception e) {
			System.out.println("Could not create budgets.txt.");
		}
	}

	// reads every saved budget for a user (all months) from budgets.txt
	// and converts each line back into a Budget object
	public List<Budget> getAllBudgets(CurrentUser user) {
		List<Budget> budgets = new ArrayList<>();
		String username = user.getUsername();
		String filePath = "files/" + username + "/budgets.txt";

		createFile(filePath);

		try (Scanner reader = new Scanner(new File(filePath))) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (!line.isBlank()) {
					Budget budget = parseBudgetLine(line);
					if (budget != null) {
						budgets.add(budget);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read budgets.txt.");
		}

		return budgets;
	}

	// parses a single "budgetId | userId | category | monthlyLimit | month | createdAt" line
	private Budget parseBudgetLine(String line) {
		String[] parts = line.split("\\s*\\|\\s*");
		if (parts.length < 6) {
			return null;
		}
		try {
			String budgetId = parts[0];
			String userId = parts[1];
			String category = parts[2];
			double monthlyLimit = Double.parseDouble(parts[3]);
			YearMonth month = YearMonth.parse(parts[4]);
			LocalDateTime createdAt = LocalDateTime.parse(parts[5]);
			return new Budget(budgetId, userId, category, monthlyLimit, month, createdAt);
		} catch (Exception e) {
			// skip malformed lines instead of crashing the whole read
			System.out.println("Skipping malformed budget line: " + line);
			return null;
		}
	}

	// only the budgets set for a specific month (e.g. only July 2026's budgets)
	public List<Budget> getBudgetsForMonth(CurrentUser user, YearMonth month) {
		List<Budget> monthBudgets = new ArrayList<>();
		for (Budget budget : getAllBudgets(user)) {
			if (budget.getMonth().equals(month)) {
				monthBudgets.add(budget);
			}
		}
		return monthBudgets;
	}

	// creates a new budget, or updates the limit if one already exists
	// for that category + month (so users don't end up with duplicate budgets
	// for the same category in the same month)
	public void setBudget(Budget newBudget, CurrentUser user) {
		String username = user.getUsername();
		String filePath = "files/" + username + "/budgets.txt";
		createFile(filePath);

		List<Budget> budgets = getAllBudgets(user);

		boolean found = false;
		for (int i = 0; i < budgets.size(); i++) {
			Budget existing = budgets.get(i);
			if (existing.getCategory().equalsIgnoreCase(newBudget.getCategory())
					&& existing.getMonth().equals(newBudget.getMonth())) {
				// keep the original budgetId/createdAt, just update the limit
				Budget updated = new Budget(existing.getBudgetId(), existing.getUserId(),
						existing.getCategory(), newBudget.getMonthlyLimit(), existing.getMonth(),
						existing.getCreatedAt());
				budgets.set(i, updated);
				found = true;
				break;
			}
		}

		if (!found) {
			budgets.add(newBudget);
		}

		writeAllBudgets(budgets, filePath);
	}

	// removes a budget (used by the "Delete" button on the Budget panel)
	public void deleteBudget(String budgetId, CurrentUser user) {
		String username = user.getUsername();
		String filePath = "files/" + username + "/budgets.txt";
		createFile(filePath);

		List<Budget> budgets = getAllBudgets(user);
		budgets.removeIf(b -> b.getBudgetId().equals(budgetId));

		writeAllBudgets(budgets, filePath);
	}

	// copies every budget from the previous month into the currently selected
	// month, as long as the user hasn't already set a budget for that category
	// this month (so it won't stomp on anything they already entered)
	public void copyFromLastMonth(CurrentUser user, YearMonth currentMonth) {
		YearMonth previousMonth = currentMonth.minusMonths(1);
		List<Budget> lastMonthBudgets = getBudgetsForMonth(user, previousMonth);
		List<Budget> currentMonthBudgets = getBudgetsForMonth(user, currentMonth);

		for (Budget lastMonthBudget : lastMonthBudgets) {
			boolean alreadyExists = false;
			for (Budget current : currentMonthBudgets) {
				if (current.getCategory().equalsIgnoreCase(lastMonthBudget.getCategory())) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				Budget copy = new Budget(user.getUsername(), lastMonthBudget.getCategory(),
						lastMonthBudget.getMonthlyLimit(), currentMonth);
				setBudget(copy, user);
			}
		}
	}

	// reads transactions.txt and sums up EXPENSE amounts by category, for a given month
	// this is what actual "spent" is measured against the budget's monthlyLimit
	public Map<String, Double> getSpentByCategory(CurrentUser user, YearMonth month) {
		Map<String, Double> spentByCategory = new LinkedHashMap<>();

		String username = user.getUsername();
		String transactionsPath = "files/" + username + "/transactions.txt";
		Path path = Paths.get(transactionsPath);

		if (!Files.exists(path)) {
			return spentByCategory;
		}

		try {
			List<String> lines = Files.readAllLines(path);
			for (String line : lines) {
				if (line.isBlank()) {
					continue;
				}
				// same "id | userId | type | amount | category | description | date | createdAt" format
				// that Transaction.toString() and FinancePanel already use
				String[] parts = line.split("\\s*\\|\\s*");
				if (parts.length < 7) {
					continue;
				}

				String type = parts[2].trim();
				if (!type.equalsIgnoreCase("EXPENSE")) {
					// budgets only track spending, not income
					continue;
				}

				double amount;
				try {
					amount = Double.parseDouble(parts[3].trim());
				} catch (NumberFormatException e) {
					continue;
				}

				String category = parts[4].trim();

				LocalDate date;
				try {
					date = LocalDate.parse(parts[6].trim());
				} catch (Exception e) {
					continue;
				}

				if (YearMonth.from(date).equals(month)) {
					spentByCategory.merge(category, amount, Double::sum);
				}
			}
		} catch (IOException e) {
			System.out.println("Could not read transactions.txt.");
		}

		return spentByCategory;
	}

	// combines each budget for the month with how much has actually been spent,
	// returning one readable status line per budget, ready to show in the Budget panel's list
	public List<String> getBudgetStatus(CurrentUser user, YearMonth month) {
		List<String> statusLines = new ArrayList<>();

		List<Budget> budgets = getBudgetsForMonth(user, month);
		Map<String, Double> spentByCategory = getSpentByCategory(user, month);

		for (Budget budget : budgets) {
			double spent = spentByCategory.getOrDefault(budget.getCategory(), 0.0);
			double remaining = budget.getRemainingAmount(spent);
			boolean overBudget = budget.isOverBudget(spent);

			String status = overBudget ? "OVER BUDGET" : "on track";

			String line = budget.getBudgetId() + " | " + budget.getCategory()
					+ " | Budget: $" + String.format("%.2f", budget.getMonthlyLimit())
					+ " | Spent: $" + String.format("%.2f", spent)
					+ " | Remaining: $" + String.format("%.2f", remaining)
					+ " | " + status;

			statusLines.add(line);
		}

		return statusLines;
	}

	// writes the full list of budgets back to budgets.txt, overwriting the old file
	// (same pattern FinancePanel.writeFile() uses for transactions)
	private void writeAllBudgets(List<Budget> budgets, String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			for (Budget budget : budgets) {
				writer.write(budget.toString() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
