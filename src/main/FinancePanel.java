package main;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class FinancePanel extends JPanel {

	private static final long serialVersionUID = 1L;

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
		
		JButton btnNewButton = new Button("Add transaction", "transaction");
		btnNewButton.setBounds(154, 25, 150, 29);
		add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Transactions");
		lblNewLabel.setBounds(48, 30, 123, 16);
		add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(48, 59, 361, 145);
		add(scrollPane);

	}

}
