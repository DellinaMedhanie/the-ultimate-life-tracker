package main;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TopBar extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TopBar() {
		this.setBounds(17, 6, 666, 45);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setBounds(270, 6, 61, 16);
		add(lblNewLabel);
		
	}
}
