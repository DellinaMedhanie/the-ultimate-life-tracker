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
		
		// wrapped in <html> so a long line wraps onto a second line instead
		// of being silently clipped by the default single-line JLabel; each
		// row gets enough height to safely hold two lines of text
		JLabel transaction1 = new JLabel("<html>" + t1 + "</html>");
		transaction1.setBounds(6, 40, 224, 32);
		add(transaction1);
		
		JLabel transaction2 = new JLabel("<html>" + t2 + "</html>");
		transaction2.setBounds(6, 76, 224, 32);
		add(transaction2);
		
		JLabel transaction3 = new JLabel("<html>" + t3 + "</html>");
		transaction3.setBounds(6, 112, 224, 32);
		add(transaction3);
		
		JLabel lblNewLabel = new JLabel("Recent Transactions");
		lblNewLabel.setBounds(40, 6, 160, 16);
		add(lblNewLabel);
	}

	public void readTransactionFile() {
		String name = this.user.getUsername();
		filePath = "files/" + name + "/transactions.txt";

		// no transactions logged yet for this user is a normal state, not an
		// error worth printing a stack trace for
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null && count < 3) {
            	// clean data to not include the transaction id or username 
            	String regex = "^.*\\b" + Pattern.quote(name) + "\\s*\\|\\s*";
            	String cleanLine = line.replaceAll(regex, "");

            	// this is a compact "snapshot" widget, not the full ledger, so
            	// only keep the type/amount/category fields (drop the longer
            	// description and the two dates) so each row reliably fits
            	String[] fields = cleanLine.split("\\s*\\|\\s*");
            	String summary = fields.length >= 3
            			? fields[0] + " | " + fields[1] + " | " + fields[2]
            			: cleanLine;
            	
            	recentTransactions.add(count, summary);
                count++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
