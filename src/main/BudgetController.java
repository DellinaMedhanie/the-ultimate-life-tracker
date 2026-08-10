package main;

import java.time.YearMonth;

// Controller Layer
public class BudgetController {

	private final CurrentUser user;
	private final BudgetService budgetService;
	// reference back to the panel so we can refresh what's on screen
	// after a change, same relationship TransactionService has with FinancePanel
	private BudgetPanel budgetPanel;

	public BudgetController(CurrentUser user, BudgetPanel budgetPanel) {
		this.user = user;
		this.budgetPanel = budgetPanel;
		this.budgetService = new BudgetService();
	}

	// called when the user picks a different month in the panel's month/year dropdowns
	public void onMonthChange(YearMonth newMonth) {
		budgetPanel.refreshForMonth(newMonth);
	}

	// called when the user saves a new (or edited) budget from the BudgetForm
	public void onAddBudget(Budget budget) {
		budgetService.setBudget(budget, user);
		budgetPanel.refreshForMonth(budget.getMonth());
	}

	// called when the user clicks "Copy from last month"
	public void onCopyFromLastMonth(YearMonth currentMonth) {
		budgetService.copyFromLastMonth(user, currentMonth);
		budgetPanel.refreshForMonth(currentMonth);
	}

	// called when the user deletes a budget from the list
	public void onDeleteBudget(String budgetId, YearMonth currentMonth) {
		budgetService.deleteBudget(budgetId, user);
		budgetPanel.refreshForMonth(currentMonth);
	}

	public BudgetService getBudgetService() {
		return budgetService;
	}
}
