package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Service layer
public class TransactionService {
	
	public static void addTransaction(Transaction transactionDetails) {
		// writes transaction details to a transaction file for the user 
		// TODO: this needs to change based on who the user is
		// hard coded right now for testing purposes
		String user = "alice";
		 
		String file = "files/" + user + "/transactions.txt";
		 try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));) {
			 writer.newLine();
			 writer.write(transactionDetails.toString());
			 writer.newLine();
			 writer.close();
			 
			// call method in FinancePanel to re-render panel to include the 
			// newly added transaction data 
			FinancePanel.updateTransactionList(transactionDetails.toString());
			 
		 } catch (IOException err) {
			 System.out.println("Whoops, sad day, we got an error :(");
			 err.printStackTrace();
		 }	
	}
	
	// NOTE: this is implemented in the FinancePanel for ease of implementation 
	// can be moved here if needed
//	public void editTransaction() {} 
	
//	public void deleteTransaction() {}


}
