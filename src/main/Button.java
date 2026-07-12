package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton implements ActionListener {
	
	JButton button; 
	String actionState;
	
	public Button(String text, String action) {
		button = new JButton();
		this.setBounds(200, 100, 100, 50);
		this.addActionListener(this);
		this.setText(text);
		this.setFocusable(false);
		actionState = action;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this) {
			if (actionState == "add task") {				
				TaskForm form = new TaskForm();
			}
			else if (actionState == "transaction") {
				DashboardController.onAddTransaction();
			}
		} else {
			System.out.println("some eclipse error...");
		}
	}
	
}
