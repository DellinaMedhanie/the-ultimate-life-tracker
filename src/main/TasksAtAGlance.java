
package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TasksAtAGlance extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JTable atAGlancePanel;
	
	String[] tasks;
	

	public TasksAtAGlance() {
		this.setBounds(100, 80, 346, 227);
		this.setLayout(null);

		// shows top 3 tasks by soonest due date 
		tasks = findTopThreeTasks();
		System.out.println(tasks);
		
		// quick button addTask()
		Button taskButton = new Button("Add a task", "add task");
		taskButton.setBounds(191, 6, 111, 29);
		
		this.add(taskButton);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(0, 1, 0, 0));

		String firstTask = tasks[0];			
		JLabel label = new JLabel(firstTask);
		containerPanel.add(label);
		
		String secondTask = tasks[1];
		JLabel label_1 = new JLabel(secondTask);
		containerPanel.add(label_1);
		
		String thirdTask = tasks[2];
		JLabel label_2 = new JLabel(thirdTask);
		containerPanel.add(label_2);
		
		JScrollPane scrollPane = new JScrollPane(containerPanel);
		scrollPane.setBounds(38, 41, 294, 175);
		add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("Top 3 critical tasks");
		lblNewLabel.setBounds(6, 11, 166, 16);
		add(lblNewLabel);
	}
	
	public String[] findTopThreeTasks() {
		String[] topTasks = new String[3];	
		ArrayList<String> allTasks = new ArrayList<>();
		
		String user = "alice";
		File f = new File("files/" + user + "/tasks.txt");
		
		// read data from tasks file
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); 
				// the only data needed for filtering the top 3 tasks 
				// are the title and the due date 
				
				// "----------" is line at the beginning of each new task 
				if (data.contains("----------")) {
					// "read" the next line to skip it (it's the "Created At" field)
					reader.nextLine();
					// this is the line that contains the Title
					// so it's added to the allTasks array to be filtered through later 
					data = reader.nextLine();
					allTasks.add(data);
					// "read" the next lines to skip them 
					// "Notes" field
					reader.nextLine();
					// "Type"
					reader.nextLine();
					// "Priority"
					reader.nextLine(); 
					// "Status"
					reader.nextLine(); 
					// this is the line that has the due date 
					// so it's added to the array to be filtered through later 
					data = reader.nextLine(); 
					allTasks.add(data);
				}
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
		
		// look through all tasks to sort them by due date 
		
		String dateToday = LocalDate.now().toString();
		// split data into YYYY, MM, and DD
		String[] splitDate = dateToday.split("-");
		// convert YYYY string into int 
		int todayYear = Integer.parseInt(splitDate[0]);
		// convert MM string into int 
		int todayMonth = Integer.parseInt(splitDate[1]);
		// convert DD string into int
		int todayDate = Integer.parseInt(splitDate[2]);
		// type cast date to LocalDate
		LocalDate today = LocalDate.of(todayYear, todayMonth, todayDate);
		
		// using insertion sort algorithm to sort the tasks
		// this should probably change to having the task data be objects 
		// instead of strings in an ArrayList, but implementing this 
		// for now to test out functionality
		ArrayList<String> sortedTasks = new ArrayList<>();

		for (int i = 0; i < allTasks.size(); i++) {			
			if (allTasks.get(i).contains("Due date")) {
				String dueDate = allTasks.get(i);
				// first split the line to be "Due date" and "YYYY-MM-DD"
				String[] splitdata = dueDate.split(": ");
				// then split this data into YYYY, MM, and DD
				String[] formattedData = splitdata[1].split("-");
				// convert YYYY string into int 
				int year = Integer.parseInt(formattedData[0]);
				// convert MM string into int 
				int month = Integer.parseInt(formattedData[1]);
				// convert DD string into int 
				int day = Integer.parseInt(formattedData[2]);
				// type cast date to LocalDate
				LocalDate due = LocalDate.of(year, month, day);
				
				// gives days between today and due date 
				// if it's a negative number, the due date is already past and the task is overdue 
				// if it's a 0, the due date is today
				// if it's a positive number, the due date is in the future
				long days = today.until(due, ChronoUnit.DAYS);
				
				
				
			};
		}

		
		return topTasks;
	}
}












