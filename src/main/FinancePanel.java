package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class FinancePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> listModel;
	private JList<String> transactionList;
	private JScrollPane scrollPane;
	private JButton addButton;
	private JButton deleteButton;
	private JButton editButton;
	
	private ArrayList<String> entries = new ArrayList<String>();

	// gets signed-in user name from CurrentUser 
    private CurrentUser user;
    String filePath;

	/**
	 * Create the panel.
	 */
	public FinancePanel(CurrentUser user) {
		// save user variable locally 
		this.user = user; 
		// get username of signed in user 
		String name = this.user.getUsername();
		// find the corresponding file for the signed in user 
		filePath = "files/" + name + "/transactions.txt";
		
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(null);
		
		JLabel title = new JLabel(); 
		title.setBounds(184, 6, 140, 16);
		title.setText("Finance tracker");
		this.add(title);
		
		// creates file if it doesn't exist 
		createFile(); 
		// reads transaction file and adds to entries array
		readFile(filePath); 
		
		JLabel lblNewLabel = new JLabel("Transactions");
		lblNewLabel.setBounds(48, 30, 123, 16);
		this.add(lblNewLabel);

		listModel = new DefaultListModel<String>();
		transactionList = new JList<String>(listModel);
		
		scrollPane = new JScrollPane(transactionList);
		scrollPane.setBounds(48, 59, 361, 145);
		this.add(scrollPane);
		
		addButton = new JButton("Add transaction");
		addButton.setBounds(46, 216, 150, 29);
		addButton.addActionListener(this);
		this.add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(300, 216, 117, 29);
		deleteButton.addActionListener(this);
		this.add(deleteButton);
		
		editButton = new JButton("Edit transaction");
		editButton.setBounds(46, 254, 150, 29);
		editButton.addActionListener(this);
		this.add(editButton);
		
		// shows "no entries yet" message if entries list is empty 
		// otherwise adds entries to the listModel
		updateList();

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			addTransaction();
		} else if (e.getSource() == deleteButton) {
			deleteTransaction();
		} else if (e.getSource() == editButton) {
			editTransaction();
		}
	}
	
	public void addTransaction() {
		// pass in instance of financePanel to be able to update/reference later
		TransactionForm form = new TransactionForm(this.user, this);
		// need to call this to have the form be visible when it pops up
		form.setVisible(true);
	}
	
	// Allows user to delete any entry they select
	public void deleteTransaction() {
		int index = transactionList.getSelectedIndex();

		if (index == -1 || entries.size() == 0) {
			JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
			return;
		}

		if (transactionList.getSelectedValue().equals("No Entries Yet")) {
			JOptionPane.showMessageDialog(this, "There are no entries to delete.");
			return;
		}

		// removes specific entry from the list of transactions
		entries.remove(index);
		// overwrites the transaction file by writing all the other transactions
		// in the transactions entries to the file
		writeFile();
		updateList();

		JOptionPane.showMessageDialog(this, "Transaction deleted.");
	}
	
	public void editTransaction() {
		int index = transactionList.getSelectedIndex();

		if (index == -1 || entries.size() == 0) {
			JOptionPane.showMessageDialog(this, "Please select an entry to edit.");
			return;
		}

		if (transactionList.getSelectedValue().equals("No Entries Yet")) {
			JOptionPane.showMessageDialog(this, "There are no entries to edit.");
			return;
		}
		
		// creates a new Transaction form populated with the fields given in the details String
		// when form is saved, it calls the TransactionService code that calls the saveEdit() code below
		// pass in instance of financePanel 
		TransactionForm form = new TransactionForm(this.user, entries.get(index), this);
		// need to call this to have the form be visible when it pops up
		form.setVisible(true);
		
	}
	
	// triggered when someone adds a new transaction
	// reads the transaction.txt file again to update the list shown 
	public void updateTransactionList(CurrentUser user) {
		this.user = user;
		// get username of signed in user 
		String name = this.user.getUsername();
		// find the corresponding file for the signed in user 
		filePath = "files/" + name + "/transactions.txt";
		readFile(filePath);
		// update the listModel to account for new entry
		updateList();
		// revalidate + repaint to re-render the GUI in the scrollpane
		scrollPane.revalidate(); 
		scrollPane.repaint();
		
		
		// show message to user to give feedback that the transaction was saved
		JOptionPane.showMessageDialog(this, "Transaction saved.");
	}
	
	// method is called from Transaction Form when saving edits made to an existing transaction 
	public void saveEdits(String transactionId, String transactionDetails, CurrentUser user) {
		// search for entries index based on userId of transaction details 
		this.user = user;
		
		String isFound = "n";
		for (String transaction : entries) {
			if (transaction.contains(transactionId) && (isFound == "n")) {
				int index = entries.indexOf(transaction);
				// updates specific entry from the list of transactions
				String fullTransactionDetails = transactionId + " | " + transactionDetails;
				// overwrite index of transaction with updated transaction details
				entries.set(index, fullTransactionDetails);
				// set isFound to yes  
				isFound = "y";
			} 
		}			
		
		// overwrites the transaction file by re-writing all transactions including
		// the updated transaction to the file
		writeFile();
		// get username of signed in user 
		String name = this.user.getUsername();
		// find the corresponding file for the signed in user 
		filePath = "files/" + name + "/transactions.txt";
		readFile(filePath);
		updateList();

		// revalidate + repaint to re-render the GUI in the scrollpane
		transactionList.revalidate(); 
		transactionList.repaint();

	}
	
	public void writeFile() {
		try {
			// get username of signed in user 
			String name = this.user.getUsername();
			// find the corresponding file for the signed in user 
			filePath = "files/" + name + "/transactions.txt";
			FileWriter writer = new FileWriter(filePath);

			for (String entry : entries) {
				writer.write(entry + "\n");
			}

			writer.close();
		} catch (Exception e) {
		}
	}
	
	// Updates the transaction history list
	public void updateList() {
		listModel.clear();

		if (entries.size() == 0) {
			listModel.addElement("No Entries Yet");
		} else {
			for (String entry : entries) {
				listModel.addElement(entry);
			}
		}
	}

	public void createFile() {
		try {
			File file = new File(filePath);

			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			System.out.println("Could not create transactions.txt.");
		}
	}


	public void readFile(String path) {
		entries.clear();
		try {
			File file = new File(path);
			Scanner reader = new Scanner(file);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();

				if (!line.isBlank()) {
					entries.add(line);
				}
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("Could not read transactions.txt.");
		}
	}

}
