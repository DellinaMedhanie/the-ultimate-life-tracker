package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class BudgetPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final CurrentUser user;
	private final BudgetController budgetController;

	private DefaultListModel<String> listModel;
	private JList<String> budgetList;
	private JScrollPane scrollPane;

	private JComboBox<String> monthSelector;
	private JComboBox<String> yearSelector;

	private JButton addButton;
	private JButton deleteButton;
	private JButton copyButton;

	// currently displayed month, defaults to the current real-world month
	private YearMonth selectedMonth;

	// same month list TransactionForm uses, kept here too so the dropdown text matches
	private String months[]
		 = { "Jan", "Feb", "Mar", "Apr",
			 "May", "Jun", "July", "Aug",
			 "Sep", "Oct", "Nov", "Dec" };

	private String years[]
		 = { "2026", "2027", "2028", "2029",
			"2030", "2031", "2032", "2033",
			"2034", "2035", "2036", "2037"};

	/**
	 * Create the panel.
	 */
	public BudgetPanel(CurrentUser user) {
		this.user = user;
		this.budgetController = new BudgetController(user, this);
		this.selectedMonth = YearMonth.from(LocalDate.now());

		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(null);

		JLabel title = new JLabel();
		title.setBounds(180, 6, 160, 16);
		title.setText("Budget tracker");
		this.add(title);

		JLabel monthLabel = new JLabel("Month:");
		monthLabel.setBounds(48, 30, 60, 27);
		this.add(monthLabel);

		monthSelector = new JComboBox<>(months);
		monthSelector.setBounds(100, 27, 90, 27);
		monthSelector.setSelectedIndex(selectedMonth.getMonthValue() - 1);
		monthSelector.addActionListener(this);
		this.add(monthSelector);

		yearSelector = new JComboBox<>(years);
		yearSelector.setBounds(196, 27, 88, 27);
		int yearIndex = selectedMonth.getYear() - 2026;
		yearSelector.setSelectedIndex(yearIndex >= 0 && yearIndex < years.length ? yearIndex : 0);
		yearSelector.addActionListener(this);
		this.add(yearSelector);

		listModel = new DefaultListModel<>();
		budgetList = new JList<>(listModel);

		scrollPane = new JScrollPane(budgetList);
		scrollPane.setBounds(48, 65, 361, 140);
		this.add(scrollPane);

		addButton = new JButton("Add budget");
		addButton.setBounds(46, 216, 130, 29);
		addButton.addActionListener(this);
		this.add(addButton);

		copyButton = new JButton("Copy last month");
		copyButton.setBounds(184, 216, 130, 29);
		copyButton.addActionListener(this);
		this.add(copyButton);

		deleteButton = new JButton("Delete");
		deleteButton.setBounds(322, 216, 90, 29);
		deleteButton.addActionListener(this);
		this.add(deleteButton);

		// initial render for the current month
		refreshForMonth(selectedMonth);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			addBudget();
		} else if (e.getSource() == deleteButton) {
			deleteBudget();
		} else if (e.getSource() == copyButton) {
			copyFromLastMonth();
		} else if (e.getSource() == monthSelector || e.getSource() == yearSelector) {
			monthChanged();
		}
	}

	// opens the pop-up form to set a new budget for the currently selected month
	public void addBudget() {
		BudgetForm form = new BudgetForm(this.user, this.budgetController, selectedMonth);
		form.setVisible(true);
	}

	// deletes whichever budget entry is currently selected in the list
	public void deleteBudget() {
		int index = budgetList.getSelectedIndex();

		if (index == -1 || listModel.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please select a budget to delete.");
			return;
		}

		String selected = budgetList.getSelectedValue();
		if (selected == null || selected.equals("No budgets set for this month")) {
			JOptionPane.showMessageDialog(this, "There are no budgets to delete.");
			return;
		}

		// the budgetId is the first field before the first " | "
		String budgetId = selected.split("\\s*\\|\\s*")[0];
		budgetController.onDeleteBudget(budgetId, selectedMonth);

		JOptionPane.showMessageDialog(this, "Budget deleted.");
	}

	// copies every budget set for the previous month into the currently selected month
	public void copyFromLastMonth() {
		budgetController.onCopyFromLastMonth(selectedMonth);
		JOptionPane.showMessageDialog(this, "Copied budgets from " + selectedMonth.minusMonths(1) + ".");
	}

	// triggered when the user changes the month or year dropdown
	private void monthChanged() {
		int monthIndex = monthSelector.getSelectedIndex() + 1;
		int year = Integer.parseInt((String) yearSelector.getSelectedItem());
		YearMonth newMonth = YearMonth.of(year, monthIndex);
		budgetController.onMonthChange(newMonth);
	}

	// re-reads budgets + spending for the given month and refreshes what's on screen
	// this is called by BudgetController after any add/delete/copy/month-change
	public void refreshForMonth(YearMonth month) {
		this.selectedMonth = month;

		// keep dropdowns in sync in case this was triggered by a copy/add rather
		// than the dropdowns themselves
		monthSelector.setSelectedIndex(month.getMonthValue() - 1);
		int yearIndex = month.getYear() - 2026;
		if (yearIndex >= 0 && yearIndex < years.length) {
			yearSelector.setSelectedIndex(yearIndex);
		}

		List<String> statusLines = budgetController.getBudgetService().getBudgetStatus(this.user, month);

		listModel.clear();
		if (statusLines.isEmpty()) {
			listModel.addElement("No budgets set for this month");
		} else {
			for (String line : statusLines) {
				listModel.addElement(line);
			}
		}

		scrollPane.revalidate();
		scrollPane.repaint();
	}
}
