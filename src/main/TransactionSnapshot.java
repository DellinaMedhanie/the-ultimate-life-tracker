package main;

import javax.swing.JPanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JLabel;

public class TransactionSnapshot extends JPanel {

	private static final long serialVersionUID = 1L;

	private CurrentUser user; 
	private String filePath; 
	
	private ArrayList<String> recentTransactions = new ArrayList<String>();

	/**
	 * Create the panel.
	 */
	public TransactionSnapshot(CurrentUser user) {
		this.user = user; 
		
		this.setBounds(25, 221, 236, 146);
		this.setLayout(null);
		
		recentTransactions.add("");
		recentTransactions.add("");
		recentTransactions.add("");
		
		readTransactionFile(); 
		
		// transaction1 data
		String t1 = recentTransactions.get(0); 
		// transaction2 data
		String t2 = recentTransactions.get(1); 
		// transaction3 data 
		String t3 = recentTransactions.get(2);
		
		JLabel transaction1 = new JLabel(t1);
		transaction1.setBounds(6, 42, 209, 16);
		add(transaction1);
		
		JLabel transaction2 = new JLabel(t2);
		transaction2.setBounds(6, 81, 209, 16);
		add(transaction2);
		
		JLabel transaction3 = new JLabel(t3);
		transaction3.setBounds(6, 124, 209, 16);
		add(transaction3);
		
		JLabel lblNewLabel = new JLabel("Recent Transactions");
		lblNewLabel.setBounds(49, 6, 137, 16);
		add(lblNewLabel);
	}

	public void readTransactionFile() {
		String name = this.user.getUsername();
		filePath = "files/" + name + "/transactions.txt";
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null && count < 3) {
            	// clean data to not include the transaction id or username 
            	String regex = "^.*\\b" + Pattern.quote(name) + "\\s*\\|\\s*";
            	String cleanLine = line.replaceAll(regex, "");
            	
            	recentTransactions.add(count, cleanLine);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
