package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;


// TODO: implement character limit for text boxes 

public class TransactionForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JFormattedTextField amountTextField;
	private JLabel amountLabel;
	private JComboBox category;
	private JLabel categoryLabel;
	private JLabel descriptionLabel;
	private JTextArea description;
	private JLabel dateLabel;
	private JComboBox day;
	private JComboBox month;
	private JComboBox year;
	private JButton saveButton;
	private JButton resetButton;
	private JButton saveEditsButton;

	// this variable is to keep track of transaction id for editing a transaction
	private JLabel hiddenLabelTransactionId;
	
	// list of days for JComboBox for choosing a date
	private String dates[]
	     = { "1", "2", "3", "4", "5",
	         "6", "7", "8", "9", "10",
	         "11", "12", "13", "14", "15",
	         "16", "17", "18", "19", "20",
	         "21", "22", "23", "24", "25",
	         "26", "27", "28", "29", "30",
	         "31" };
	
	// list of months for JComboBox for choosing a date 
	private String months[]
	     = { "Jan", "Feb", "Mar", "Apr",
	         "May", "Jun", "July", "Aug",
	         "Sep", "Oct", "Nov", "Dec" };
	
	// list of years for JComboBox for choosing a year
	// have years starting from current year (2026) until 
	// arbitrary year of 2037
	private String years[]
	     = { "2026", "2027", "2028", "2029", 
	    	"2030", "2031", "2032", "2033",
	    	"2034", "2035", "2036", "2037"};
	
	private ArrayList<String> categories = new ArrayList<>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransactionForm frame = new TransactionForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	// default constructor that initializes 
	// with all default values in the form fields 
	public TransactionForm() {
		initialize();
	}
	
	public void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 250, 450, 300);
		setTitle("Add new transaction");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		amountLabel = new JLabel("Amount");
		amountLabel.setBounds(257, 11, 187, 27);
		contentPane.add(amountLabel);
		
		DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
		NumberFormatter moneyFormatter = new NumberFormatter(currencyFormat);
		// sets exact value expected
		moneyFormatter.setValueClass(Integer.class);
		// Stops the user from typing invalid characters
		moneyFormatter.setAllowsInvalid(false); 
		// Commits value on each keystroke
        moneyFormatter.setCommitsOnValidEdit(true); 
        // sets min and max values 
        // cannot enter negative numbers
        moneyFormatter.setMinimum(0);
        // restricted to 9999
        moneyFormatter.setMaximum(9999);

        amountTextField = new JFormattedTextField(currencyFormat);
        amountTextField.setValue(0.00);
		amountTextField.setBounds(277, 43, 115, 26);
//		 add a document listener to know when the amount field is filled out
//		 to enable the save button
		amountTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void checkField() {
                if (amountTextField.getText().trim().length() > 0) {
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }

            // these are necessary to override to have the DocumentListener work correctly            
            @Override
            public void insertUpdate(DocumentEvent e) { checkField(); }

            @Override
            public void removeUpdate(DocumentEvent e) { checkField(); }

            @Override
            public void changedUpdate(DocumentEvent e) { checkField(); }
        });
		
		// https://www.tutorialspoint.com/article/how-can-we-make-jtextfield-accept-only-numbers-in-java
		amountTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				// only allow numbers in between 0 and 9 or a . for the decimal point 
				if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == '.') {
					amountTextField.setEditable(true);
					amountLabel.setText("Enter the amount");
				} else {
					// set the text field to not be able to enter the character
					amountTextField.setEditable(false);
					// give a message to the user 
					amountLabel.setText("Only enter digits (0-9)");
				}
			}
		});
            
		contentPane.add(amountTextField);
		
		categoryLabel = new JLabel("Category");
		categoryLabel.setBounds(18, 95, 61, 16);
		contentPane.add(categoryLabel);

		// gets categories from categories.txt
		getCategories();
		// converts categories (ArrayList) to a String[] list 
		// to be able to pass into JComboBox
		String[] categoryArray = categories.toArray(new String[0]);
		category = new JComboBox(categoryArray);
		category.setBounds(91, 91, 129, 27);
		contentPane.add(category);
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(30, 139, 113, 16);
		contentPane.add(descriptionLabel);
		
		// right now, the description is left to be an optional field that 
		// the user enters in, similar to the "notes" for the mood tracker
		description = new JTextArea();
		description.setBounds(36, 157, 370, 59);
		contentPane.add(description);
		
		dateLabel = new JLabel("Date of transaction");
		dateLabel.setBounds(18, 16, 129, 16);
		contentPane.add(dateLabel);
		
		day = new JComboBox(dates);
		day.setBounds(6, 44, 72, 27);
		// set the day value to default to current date
	    day.setSelectedIndex(findCurrentDateIndex());
		contentPane.add(day);
		
		month = new JComboBox(months);
		month.setBounds(81, 44, 94, 27);
		// set the month value to default to current month
	    month.setSelectedIndex(findCurrentMonthIndex());
		contentPane.add(month);
		
		year = new JComboBox(years);
		year.setBounds(177, 44, 88, 27);
		// set year value to default to current year
		year.setSelectedIndex(findCurrentYearIndex());
		contentPane.add(year);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(85, 237, 117, 29);
		// add Action Listener to the save button to be able to 
		// react to when the user clicks "save" 
		saveButton.addActionListener(this);
		// have button be disabled as a default
		// and is enabled after amount and description have been filled out
//		saveButton.setEnabled(false);
		contentPane.add(saveButton);
		
		resetButton = new JButton("Reset");
		resetButton.setBounds(201, 237, 117, 29);
		// add Action Listener to the reset button to be able to 
		// react to when the user clicks "reset"
		resetButton.addActionListener(this);
		contentPane.add(resetButton);

	}
	
	 
	 // constructor that populates the fields in the form to be the values 
	 // provided by the existing transaction 
	 // this is for when the user is trying to edit a transaction
	 public TransactionForm(String transactionDetails) {
		 // first initialize the form 
		 initialize();
		 
		 // break the long string into individual values 
		 // shape of the data 
		 // transactionID | userId | TYPE | Amount | Category | description | dateOfTransaction | createdAt
		 // sample shape of data being split is 
		 // 375916340 | alice | EXPENSE | 10.0 | Education | course | 2026-07-12 | 2026-07-12T01:08:09.183754
		 String[] values = transactionDetails.split(" | ");
		 
		 // data needed to populate form is amount, category, description, and date
		 String amountValue = values[6];
		 String categoryValue = values[8];
		 String descriptionValue = values[10];
		 
		 // create a hidden value on the form to be submitted when clicking "save edits" 
		 // to keep track of the transactionId 
		 String transactionId = values[0];
		 hiddenLabelTransactionId = new JLabel(transactionId);
		 hiddenLabelTransactionId.setVisible(false);
		 contentPane.add(hiddenLabelTransactionId);
;		 
		 // date value needs to be split into year, month, and day
		 String dateValue = values[12];
		 String[] splitDate = dateValue.split("-");
		 String yearValue = splitDate[0];
		 
		 // find the index of the year in the years array
		 int yearIndex = Arrays.asList(years).indexOf(yearValue);
		 
		 String monthValue = splitDate[1]; 
		 // filter through values staring with 0 (i.e. 01, 02, etc.) 
		 int monthIndex;
		 if (monthValue.matches("0.")) {
			 // break string into a substring that removes the 0
			 String removedZeroFromMonth = monthValue.substring(1, 2);
			 // type cast value into an int to search in month array 
			 // subtract 1 to account for month array starting at zero 
			 monthIndex = Integer.parseInt(removedZeroFromMonth) -1;
		 } else {
			 // if the monthValue doesn't start with 0, then it doesn't need to be 
			 // split into a substring and can type cast value as is
			 // subtract 1 to account for month array starting at zero 
			 monthIndex = Integer.parseInt(monthValue) - 1;
		 } 
		 
		 // day needs to be filtered through for values starting with 0 (like month)
		 String dayValue = splitDate[2];
		 int dayIndex; 
		 if (dayValue.matches("0.")) {
			 // break string into a substring that removes the 0
			 String removedZeroFromDay = dayValue.substring(1, 2);
			 // type cast into int to use as index for day array
			 // subtract 1 to account for day array starting at zero 
			 dayIndex = Integer.parseInt(removedZeroFromDay) - 1;
		 } else {
			 // if the monthValue doesn't start with 0, then it doesn't need to be 
			 // split into a substring 
			 // subtract 1 to account for day array starting at zero 
			 dayIndex = Integer.parseInt(dayValue) - 1;
		 } 
		 
		 // find the index of the categoryValue by searching through categories array
		 int categoryIndex = categories.indexOf(categoryValue);
		 
		 // sets the fields to be the transaction details 
		 amountTextField.setText(amountValue);
		 description.setText(descriptionValue);
		 category.setSelectedIndex(categoryIndex);
		 
		 // set year, month, and day based on index from arrays
		 year.setSelectedIndex(yearIndex);
		 month.setSelectedIndex(monthIndex);
		 day.setSelectedIndex(dayIndex);
		 
		 // hide the save button for adding a new transaction
		 contentPane.remove(saveButton);
		 // and add a new edit save button to handle saving edits to the transaction 
		 saveEditsButton = new JButton("Save edits");
		 saveEditsButton.setBounds(85, 237, 117, 29);
		 saveEditsButton.addActionListener(this);
		 contentPane.add(saveEditsButton);
	 }
	
	// gets category data from user's categories.txt file 
	// because user's can have custom categories 
	
	// TODO: change hard coded text to link to whatever user 
	// is currently logged in 
	
	// using default, hard-coded text for testing reading user data
	String user = "alice";
	
	public void getCategories() {		
		File f = new File("files/" + user + "/categories.txt");
				
		try (Scanner reader = new Scanner(f)) {
			// skips reading the first line in the file
			// which is header for the file
			if (reader.hasNextLine()) {
				reader.nextLine();
			}
			
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); 
				String[] splitData = data.split(",");
				categories.add(splitData[0]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
		
	}
	
	public int findCurrentMonthIndex() {
		 int monthNum = LocalDate.now().getMonthValue();
		 return monthNum - 1;
	}
	 
	public int findCurrentDateIndex() {
		 int dateNum = LocalDate.now().getDayOfMonth();
		 return dateNum - 1;
	}
	 
	public int findCurrentYearIndex() {
		 int dateNum = LocalDate.now().getYear();
		 int buffer = dateNum - 2026;
		 // years only account for before 2037
		 if (dateNum > 2037) {
			 return 0;
		 }
		 return buffer;
	}
	
	public void actionPerformed(ActionEvent e) {	

		if (e.getSource() == saveButton || e.getSource() == saveEditsButton) {
			
			// TODO: figure out userId, just using user text right now
			String userId = user; 
			double amountData = Double.parseDouble(amountTextField.getValue().toString());
			String categoryData = category.getSelectedItem().toString();
			
			// since the type is linked to the category, the user doesn't input 
			// this information, and it's automatically added to the Transaction 
			// object based on what category it is
			Transaction.Type typeData = determineType(categoryData);
			
			String descriptionData = description.getText();
			
			// convert date string to a LocalDate to have the correct format of "YYYY-MM-DD"
			String dateStr = createDateString(year.getSelectedItem().toString(), month.getSelectedItem().toString(), day.getSelectedItem().toString());
			// convert the String into LocalDate data type
			LocalDate dateData = LocalDate.parse(dateStr);
			
			if (e.getSource() == saveButton) {				
				// create new transaction object to then pass into the transaction controller to write the data to the file there
				Transaction transaction = new Transaction(userId, typeData, amountData, categoryData, descriptionData, dateData); 
				TransactionService.addTransaction(transaction);
			} else if (e.getSource() == saveEditsButton) {
				// don't create a new transaction object, since it would override the transactionId
				// pass the transactionId separately, and have the other variables chain together in a string
				String transactionId = hiddenLabelTransactionId.getText();
				String transactionDetails = userId + " | " + typeData.toString() + " | " + Double.toString(amountData) + " | " + 
						categoryData + " | " + descriptionData + " | " + dateData + " | " + LocalDate.now().toString();
				TransactionService.editTransaction(transactionId, transactionDetails);
			}
			
			// clear inputs after saving without closing the frame
			// in case the user wants to add another transaction
			resetForm();

			// show message to user to give feedback that the transaction was saved
			JOptionPane.showMessageDialog(this, "Transaction saved.");

		
		} else if (e.getSource() == resetButton) {
			resetForm();
		} 
		
	}
	
	public void resetForm() {
		// this resets all the fields to their default states 
	     String def = "";
	     description.setText(def);
	     amountTextField.setText(def);
	     day.setSelectedIndex(findCurrentDateIndex());
	     month.setSelectedIndex(findCurrentMonthIndex());
	     year.setSelectedIndex(findCurrentYearIndex());
	     category.setSelectedIndex(0);
	}
	
	public String createDateString(String year, String month, String day) {
		// when the user selects the month, it's in Text (i.e. Jan, Feb)
		// and it needs to be converted into the numerical equivalent 
		// To do this, search through the months array to find what index it is 
		
		// Add 1 to the index to account for starting at 0 
		int index = Arrays.asList(months).indexOf(month) + 1;
		// if it's 10 or under, a "0" needs to be added before it to 
		// have the correct formatting (i.e. 01, 02)
		String correctMonthFormat;
		if (index < 10) {
			correctMonthFormat = "0" + String.valueOf(index);
		} else {
			correctMonthFormat = String.valueOf(index);
		}
		
		// if the String day is less than 2, then it means 
		// it's a single digit, and "0" needs to be added to the String 
		// in order to have the correct formatting (i.e. 01, 02)
		if (day.length() < 2) {
			day = "0" + day;
		} 
				
		// format needed is "YYYY-MM-DD"
		String dateString = year + "-" + correctMonthFormat + "-" + day;
		return dateString;
	}
	
	public Transaction.Type determineType(String categoryName) {
		// Search through categories.txt file to 
		
		// TODO: change to link to dynamically change based on which user is logged in
		// to search through their custom categories 
		// it's hard coded right now for testing 
		
		File f = new File("files/" + user + "/categories.txt");
		
		try (Scanner reader = new Scanner(f)) {
			// skips reading the first line in the file
			// which is header for the file
			if (reader.hasNextLine()) {
				reader.nextLine();
			}
			
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); 
				
				if (data.contains(categoryName)) {
					String[] splitData = data.split(",");
					if (splitData[1].equals("INCOME")) {
						return Transaction.Type.INCOME;
					} else if (splitData[1].equals("EXPENSE")) {
						return Transaction.Type.EXPENSE;
					} else {
						return Transaction.Type.UNKNOWN;
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
	
		// return type "Unknown" if it cannot determine whether it's 
		// income or expense 
		return Transaction.Type.UNKNOWN;
	}

}





