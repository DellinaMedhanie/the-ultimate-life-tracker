package main;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TasksAtAGlance extends JPanel {
	TasksAtAGlance() {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		this.setBounds(100, 80, 500, 300);
		JLabel testing = new JLabel(); 
		testing.setText("Tasks at a glance");

		// quick button addTask()
		Button taskButton = new Button("Add a task", "add task");
		Dimension buttonSize = taskButton.getPreferredSize();
		taskButton.setBounds(500, 10, buttonSize.width, buttonSize.height);
		
		this.add(testing);
		this.add(taskButton);
		this.setLayout(flowLayout);
	}
}
