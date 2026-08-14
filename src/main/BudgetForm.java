package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// pop-up form used to set/edit a monthly budget for a category
// deliberately mirrors TransactionForm's layout/behavior so the app feels consistent
public class BudgetForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final CurrentUser user;
	private final BudgetController budgetController;
	// the month this budget is being set for - comes from whatever month
	// is currently selected on the Budget panel
	private final YearMonth month;

	private JPanel contentPane;
	private JLabel amountLabel;
	private JFormattedTextField amountTextField;
	private JLabel categoryLabel;
	private JComboBox<String> category;
	private JButton saveButton;
	private JButton resetButton;

	private ArrayList<String> categories = new ArrayList<>();

	public BudgetForm(CurrentUser user, BudgetController budgetController, YearMonth month) {
		this.user = user;
		this.budgetController = budgetController;
		this.month = month;
		initialize();
	}

	public void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 250, 400, 220);
		setTitle("Set budget for " + month);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		categoryLabel = new JLabel("Category");
		categoryLabel.setBounds(18, 16, 113, 16);
		contentPane.add(categoryLabel);

		// only offer EXPENSE categories, since budgets only make sense for spending
		getExpenseCategories();
		String[] categoryArray = categories.toArray(new String[0]);
		category = new JComboBox<>(categoryArray);
		category.setBounds(18, 38, 180, 27);
		contentPane.add(category);

		amountLabel = new JLabel("Monthly limit");
		amountLabel.setBounds(220, 16, 150, 16);
		contentPane.add(amountLabel);

		DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
		amountTextField = new JFormattedTextField(currencyFormat);
		amountTextField.setValue(0.00);
		amountTextField.setBounds(220, 38, 115, 27);

		// only allow numbers and a decimal point, same approach as TransactionForm
		amountTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == '.') {
					amountTextField.setEditable(true);
					amountLabel.setText("Monthly limit");
				} else {
					amountTextField.setEditable(false);
					amountLabel.setText("Only enter digits (0-9)");
				}
			}
		});

		// disable saving until an amount has actually been entered
		amountTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void checkField() {
				saveButton.setEnabled(amountTextField.getText().trim().length() > 0);
			}

			@Override
			public void insertUpdate(DocumentEvent e) { checkField(); }

			@Override
			public void removeUpdate(DocumentEvent e) { checkField(); }

			@Override
			public void changedUpdate(DocumentEvent e) { checkField(); }
		});

		contentPane.add(amountTextField);

		saveButton = new JButton("Save");
		saveButton.setBounds(65, 130, 117, 29);
		saveButton.addActionListener(this);
		contentPane.add(saveButton);

		resetButton = new JButton("Reset");
		resetButton.setBounds(200, 130, 117, 29);
		resetButton.addActionListener(this);
		contentPane.add(resetButton);
	}

	// gets only EXPENSE-type categories from the user's categories.txt file,
	// creating the file with defaults first if it doesn't exist yet
	// (same defaults/logic TransactionForm.getCategories() uses)
	public void getExpenseCategories() {
		String name = this.user.getUsername();
		File f = new File("files/" + name + "/categories.txt");

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
				writer.write("CategoryName,Type");
				writer.newLine();
				writer.write("Salary,INCOME");
				writer.newLine();
				writer.write("Freelance,INCOME");
				writer.newLine();
				writer.write("Other Income,INCOME");
				writer.newLine();
				writer.write("Housing,EXPENSE");
				writer.newLine();
				writer.write("Food & Dining,EXPENSE");
				writer.newLine();
				writer.write("Transport,EXPENSE");
				writer.newLine();
				writer.write("Healthcare,EXPENSE");
				writer.newLine();
				writer.write("Entertainment,EXPENSE");
				writer.newLine();
				writer.write("Shopping,EXPENSE");
				writer.newLine();
				writer.write("Education,EXPENSE");
				writer.newLine();
				writer.write("Utilities,EXPENSE");
				writer.newLine();
				writer.write("Other Expense,EXPENSE");
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (Scanner reader = new Scanner(f)) {
			if (reader.hasNextLine()) {
				reader.nextLine(); // skip header
			}
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] splitData = data.split(",");
				if (splitData.length == 2 && splitData[1].trim().equalsIgnoreCase("EXPENSE")) {
					categories.add(splitData[0]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			String userId = this.user.getUsername();
			double amountData = Double.parseDouble(amountTextField.getValue().toString());
			String categoryData = (String) category.getSelectedItem();

			if (categoryData == null) {
				return;
			}

			Budget budget = new Budget(userId, categoryData, amountData, month);
			
			// if the budget category is already present for month/year selected
			if (budgetExists()) {	
				// then show a warning that the user will over-write the current budget category 
				
				String[] options = {"Yes, overwrite it", "No"};

		        // Show the dialog box
		        int choice = JOptionPane.showOptionDialog(
		            null,                               // Parent component
		            "This budget already exists and will be over-written. Proceed?",   // Message
		            "Overwrite budget?",                 // Dialog title
		            JOptionPane.DEFAULT_OPTION,         // Option type
		            JOptionPane.WARNING_MESSAGE,        // Message type
		            null,                               // Icon (null uses default)
		            options,                            // Your custom button array
		            options[0]                          // Default focused button
		        );

		        // Execute different actions based on the index chosen
		        if (choice == 0) {
		            // overwrite the budget if the user requests 
					budgetController.onAddBudget(budget);				
		        } 
		        // otherwise, don't save the budget and the dialog closes and goes
		        // back to the budget form 
		        
			} else {
				// hand off to the controller, which saves it via BudgetService
				// and tells the Budget panel to refresh
				budgetController.onAddBudget(budget);				
			}
			

			resetForm();
		} else if (e.getSource() == resetButton) {
			resetForm();
		}
	}

	public void resetForm() {
		amountTextField.setValue(0.00);
		if (category.getItemCount() > 0) {
			category.setSelectedIndex(0);
		}
	}
	
	public boolean budgetExists() {
		String username = user.getUsername();
		String filePath = "files/" + username + "/budgets.txt";

		try (Scanner reader = new Scanner(new File(filePath))) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (!line.isBlank()) {
					Budget budgetLine = parseBudgetLine(line);
					// if the line from the budget.txt equals the month and year
					// of the budget trying to be set right now 
					if (budgetLine.month.getMonthValue() == month.getMonthValue() &&
							budgetLine.month.getYear() == month.getYear() && 
							budgetLine.category.equals(category.getSelectedItem())) {
						return true;
					}

				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read budgets.txt.");
		}
		return false; 
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
}
