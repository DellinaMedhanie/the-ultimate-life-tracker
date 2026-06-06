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
		
		JLabel title = new JLabel(); 
		title.setText("Home");
		
		this.add(glancePanel);
		
		this.add(title);
	}

}
