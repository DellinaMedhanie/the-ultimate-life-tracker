package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton implements ActionListener {
	
	JButton button; 
	String actionState;
	CurrentUser user; 
	
	public Button(String text, String action, CurrentUser user) {
		// save user object locally
		this.user = user;
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
				// pass locally saved user object into the form
//				TaskForm form = new TaskForm(this.user);
			}
		} else {
			System.out.println("some eclipse error...");
		}
	}
	
}
