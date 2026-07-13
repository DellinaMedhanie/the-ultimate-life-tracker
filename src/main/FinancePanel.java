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
import javax.swing.JScrollPane;

public class FinancePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	static DefaultListModel<String> listModel;
	static JList<String> transactionList;
	JScrollPane scrollPane;
	JButton deleteButton;
	
	static ArrayList<String> entries = new ArrayList<String>();

	// NOTES: this is for testing purposes only and needs to be changed 
	// to account for which user is logged in
	static String user = "alice";
	static String filePath = "files/" + user + "/transactions.txt";

	
	/**
	 * Create the panel.
	 */
	public FinancePanel() {
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(null);
		
		JLabel title = new JLabel(); 
		title.setBounds(184, 6, 96, 16);
		title.setText("Finance tracker");
		this.add(title);
		
		// creates file if it doesn't exist 
		createFile(); 
		// reads transaction file and adds to entries array
		readFile(filePath); 
		
		JButton btnNewButton = new Button("Add transaction", "transaction");
		btnNewButton.setBounds(46, 216, 150, 29);
		this.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Transactions");
		lblNewLabel.setBounds(48, 30, 123, 16);
		this.add(lblNewLabel);

		listModel = new DefaultListModel<String>();
		transactionList = new JList<String>(listModel);
		
		scrollPane = new JScrollPane(transactionList);
		scrollPane.setBounds(48, 59, 361, 145);
		this.add(scrollPane);
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(300, 216, 117, 29);
		deleteButton.addActionListener(this);
		add(deleteButton);
		
		// shows "no entries yet" message if entries list is empty 
		// otherwise adds entries to the listModel
		updateList();


	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == deleteButton) {
			deleteTransaction();
		}
	}
	
	// Updates the transaction history list
	public static void updateList() {
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


	public static void readFile(String path) {
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
	
	// triggered when someone adds a new transaction
	// reads the transaction.txt file again to update the list shown 
	public static void updateTransactionList(String transaction) {
		// add new transaction to list of entires
		entries.add(transaction.toString());
		// update the listModel to account for new entry
		updateList();
		// revalidate + repaint to re-render the GUI in the scrollpane
		transactionList.revalidate(); 
		transactionList.repaint();
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
		
		String updatedTransaction = "";

		// removes specific entry from the list of transactions
		entries.set(index, updatedTransaction);
		
		// overwrites the transaction file by writing all the other transactions
		// in the transactions entries to the file
		writeFile();

		updateList();

		JOptionPane.showMessageDialog(this, "Transaction deleted.");
	}
	
	public void writeFile() {
		try {
			FileWriter writer = new FileWriter(filePath);

			for (String entry : entries) {
				writer.write(entry + "\n");
			}

			writer.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Transaction could not be saved.");
		}
	}

}
