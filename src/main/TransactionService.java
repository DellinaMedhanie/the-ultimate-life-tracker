package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JPanel;

// Service layer
public class TransactionService {
	
	// save local instance of financePanel's data 
	private FinancePanel financePanel;
	
	public void addTransaction(Transaction transactionDetails, CurrentUser user, FinancePanel financePanel) {
		String username = user.getUsername();
		// writes transaction details to a transaction file for the user 
		String file = "files/" + username + "/transactions.txt";
		Path path = Paths.get(file);
		try {
			 List<String> lines = Files.readAllLines(path);
			 lines.add(0, transactionDetails.toString());
			 Files.write(path, lines);
			 financePanel.updateTransactionList(user);

		} catch (IOException e) {
            e.printStackTrace();
		}
	}
	

	
	public void editTransaction(String transactionId, String transactionDetails, CurrentUser user, FinancePanel financePanel) {
		this.financePanel = financePanel;
		// call method in FinancePanel to re-render panel to include the 
		// newly added transaction data 
		financePanel.saveEdits(transactionId, transactionDetails, user);
	} 
	
	// NOTE: this is implemented in the FinancePanel for ease of implementation 
	// can be moved here if needed
//	public void deleteTransaction() {}


}
