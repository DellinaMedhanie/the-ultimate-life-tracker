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
			 writer.write(transactionDetails.getTransaction() + " | " + transactionDetails.getUserId() + " | " + transactionDetails.getType() + 
					 " | " + transactionDetails.getAmount() + " | " + transactionDetails.getCategory() + " | " + transactionDetails.getDescription() +
					 " | " + transactionDetails.getDate() + " | " + transactionDetails.getCreatedAt());
			 writer.newLine();
			 writer.close();
			 
		 } catch (IOException err) {
			 System.out.println("Whoops, sad day, we got an error :(");
			 err.printStackTrace();
		 }	
	}
	
	// search for transaction in the file 
	// overwrite the transaction based on what needs changing
	public void editTransaction() {} 
	
	
	// search for transaction in the file
	// delete the transaction from the file 
	public void deleteTransaction() {}


}
