package main;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private TasksAtAGlance glancePanel = new TasksAtAGlance();

	/**
	 * Create the panel.
	 */
	public HomePanel() {
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Home");
		lblNewLabel.setBounds(194, 7, 61, 16);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tasks At a Glance");
		lblNewLabel_1.setBounds(25, 36, 123, 16);
		add(lblNewLabel_1);
		
		glancePanel.setBounds(25, 62, 345, 232);
		add(glancePanel);
		
		
		
	}

}
