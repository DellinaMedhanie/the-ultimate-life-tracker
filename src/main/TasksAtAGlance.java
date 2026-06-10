
package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TasksAtAGlance extends JPanel {
	private static final long serialVersionUID = 1L;
	
	List<String> tasks = new ArrayList<>();
	

	public TasksAtAGlance() {
		this.setBounds(100, 80, 346, 227);
		this.setLayout(null);

		// shows top 3 tasks by soonest due date / highest priority
		readTextFile();
		System.out.println(tasks);
		
		// quick button addTask()
		Button taskButton = new Button("Add a task", "add task");
		taskButton.setBounds(191, 6, 111, 29);
		
		this.add(taskButton);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(0, 1, 0, 0));

		String firstTask = tasks.get(0);			
		JLabel label = new JLabel(firstTask);
		containerPanel.add(label);
		
		String secondTask = tasks.get(1);
		JLabel label_1 = new JLabel(secondTask);
		containerPanel.add(label_1);
		
		String thirdTask = tasks.get(2);
		JLabel label_2 = new JLabel(thirdTask);
		containerPanel.add(label_2);
		
		JScrollPane scrollPane = new JScrollPane(containerPanel);
		scrollPane.setBounds(38, 41, 294, 175);
		add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("Top 3 critical tasks");
		lblNewLabel.setBounds(6, 11, 166, 16);
		add(lblNewLabel);
	}
	
	public void readTextFile() {
		// read data from tasks file to find names of tasks
		String user = "alice";
		File f = new File("files/" + user + "/tasks.txt");
		
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); 
				if (data.contains("Title")) {
					String[] splitData = data.split(":");
					tasks.add(splitData[1]);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
	}
}
