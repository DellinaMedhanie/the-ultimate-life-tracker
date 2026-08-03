package main;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private CurrentUser user; 
	
	private TransactionSnapshot transactionSnapshot;
	private PomodoroSnapshot pomodoroSnapshot;
	
	/**
	 * Create the panel.
	 */
	public HomePanel(CurrentUser user) {
		// save instance of user locally 
		this.user = user; 
		
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Home");
		lblNewLabel.setBounds(264, 6, 61, 16);
		add(lblNewLabel);
		
		JPanel taskSnapshot = new JPanel();
		taskSnapshot.setBounds(25, 42, 236, 151);
		add(taskSnapshot);
		
		JPanel moodSnapshot = new JPanel();
		add(moodSnapshot);
		
		pomodoroSnapshot = new PomodoroSnapshot(this.user);
		add(pomodoroSnapshot);
		
		transactionSnapshot = new TransactionSnapshot(this.user);
		add(transactionSnapshot);
		
		
	}
}