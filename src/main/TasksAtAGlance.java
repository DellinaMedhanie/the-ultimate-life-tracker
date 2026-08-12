//
//package main;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//
//public class TasksAtAGlance extends JPanel {
//	private static final long serialVersionUID = 1L;
//	
//	JTable atAGlancePanel;
//	
//	ArrayList<String> tasks;
//	
//
//	public TasksAtAGlance() {
//		this.setBounds(100, 80, 346, 227);
//		this.setLayout(null);
//
//		// shows top 3 tasks by soonest due date 
//		tasks = findTopThreeTasks();
//		System.out.println(tasks);
//		
//		// quick button addTask()
//		Button taskButton = new Button("Add a task", "add task");
//		taskButton.setBounds(191, 6, 111, 29);
//		
//		this.add(taskButton);
//		
//		JPanel containerPanel = new JPanel();
//		containerPanel.setLayout(new GridLayout(0, 1, 0, 0));
//
//		JLabel label;
//		if (tasks.size() > 0) {			
//			String firstTask = tasks.get(0) + " | " + tasks.get(1);			
//			label = new JLabel(firstTask);
//			containerPanel.add(label);
//			
//			String secondTask = tasks.get(2) + " | " + tasks.get(3);
//			JLabel label_1 = new JLabel(secondTask);
//			containerPanel.add(label_1);
//			
//			String thirdTask = tasks.get(4) +  " | " + tasks.get(5);
//			JLabel label_2 = new JLabel(thirdTask);
//			containerPanel.add(label_2);
//		} else {
//			// if the size of the task array is empty, then there are no tasks
//			label = new JLabel("Enter a task to see your top tasks");
//			containerPanel.add(label);
//		}
//	
//		
//		JScrollPane scrollPane = new JScrollPane(containerPanel);
//		scrollPane.setBounds(38, 41, 294, 175);
//		add(scrollPane);
//		
//		JLabel lblNewLabel = new JLabel("Top 3 critical tasks");
//		lblNewLabel.setBounds(6, 11, 166, 16);
//		add(lblNewLabel);
//	}
//	
//	public ArrayList<String> findTopThreeTasks() {
//		// will have top three tasks based on soonest due date
//		ArrayList<String> topTasks = new ArrayList<>();	
//		// array for storing all task data pulled from tasks.txt file
//		ArrayList<String> allTasks = new ArrayList<>();
//		
//		// NOTE: needs to be changed to dynamically be whichever user is 
//		// logged in. Hard coded for testing purposes
//		String user = "alice";
//		File f = new File("files/" + user + "/tasks.txt");
//		
//		// read data from tasks file
//		try (Scanner reader = new Scanner(f)) {
//			while (reader.hasNextLine()) {
//				String data = reader.nextLine(); 
//				// the only data needed for filtering the top 3 tasks 
//				// are the title and the due date 
//				
//				// "----------" is line at the beginning of each new task 
//				if (data.contains("----------")) {
//					// "read" the next line to skip it (it's the "Created At" field)
//					reader.nextLine();
//					// this is the line that contains the Title
//					// so it's added to the allTasks array to be filtered through later 
//					data = reader.nextLine();
//					allTasks.add(data);
//					// "read" the next lines to skip them 
//					// "Notes" field
//					reader.nextLine();
//					// "Type"
//					reader.nextLine();
//					// "Priority"
//					reader.nextLine(); 
//					// "Status"
//					reader.nextLine(); 
//					// this is the line that has the due date 
//					// so it's added to the array to be filtered through later 
//					data = reader.nextLine(); 
//					allTasks.add(data);
//				}
//				
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println("Error occured");
//			e.printStackTrace();
//		}
//		
//		
//		// find today's date to compare against the due date of the task
//		String dateToday = LocalDate.now().toString();
//		// split data into YYYY, MM, and DD
//		String[] splitDate = dateToday.split("-");
//		// convert YYYY string into int 
//		int todayYear = Integer.parseInt(splitDate[0]);
//		// convert MM string into int 
//		int todayMonth = Integer.parseInt(splitDate[1]);
//		// convert DD string into int
//		int todayDate = Integer.parseInt(splitDate[2]);
//		// type cast date to LocalDate
//		LocalDate today = LocalDate.of(todayYear, todayMonth, todayDate);
//		
//		
//				
//		for (int i = 0; i < allTasks.size(); i++) {	
//			// fill the topTasks array with up to the first three tasks 
//			// to have something to compare 
//			// each tasks is two strings with the shape of the data: 
//			// Title: task name, Due Date: YYYY-MM-DD
//			// so need to go up to the index of 5 to get both the title 
//			// and due date to have the 3 tasks
//			
//			// first check if the task array is less than 6 
//			if (allTasks.size() < 6) {
//				// then only loop through however long the array is
//				if (i < allTasks.size()) {
//					topTasks.add(allTasks.get(i));					
//				}
//			// otherwise if the task array is equal to or more than 6
//			// then only add up to 6 total items (or 3 tasks) 
//			} else {
//				if (i < 6) {
//					topTasks.add(allTasks.get(i));
//				} 
//			}			
//			
//			// once the top three tasks array is filled
//			if (topTasks.size() >= 6) {
//				// find the due date string for the next task
//				if (allTasks.get(i).contains("Due date")) {
//					String dueDate = allTasks.get(i);
//					LocalDate due = convertToLocalDate(dueDate);
//					
//					// gives days between today and due date 
//					// if it's a negative number, the due date is already past and the task is overdue 
//					// if it's a 0, the due date is today
//					// if it's a positive number, the due date is in the future
//					long days = today.until(due, ChronoUnit.DAYS);
//					
//					// compare the due date with the tasks in the array 
//					// start with comparing the due date of middle task in array (index 3)
//					String compareDueDateString = topTasks.get(3);
//					LocalDate compareDueDate = convertToLocalDate(compareDueDateString); 
//					long compareDays = today.until(compareDueDate, ChronoUnit.DAYS);
//					// if the task being looked at is less than the task due date at index 3
//					// then the new task has a sooner due date than this task 
//					if (days < compareDays) {
//						// then compare it to the first task due date in the array (index 1)
//						compareDueDateString = topTasks.get(1);
//						compareDueDate = convertToLocalDate(compareDueDateString);
//						compareDays = today.until(compareDueDate, ChronoUnit.DAYS);
//						// if the task due date is less than this task, then it replaces the index
//						// and the first and second task shift down, and the third task is removed
//						if (days < compareDays) {
//							// add the current index of allTasks to the topTasks array
//							// get the index minus one to get the Title string 
//							topTasks.add(0, allTasks.get(i - 1));
//							// the current index of allTasks is the due date string 
//							topTasks.add(1, allTasks.get(i));
//						} else {
//							// otherwise, it replaces index 3
//							
//							// add the current index of allTasks to the topTasks array
//							// get the index minus one to get the Title string 
//							topTasks.add(3, allTasks.get(i - 1));
//							// the current index of allTasks is the due date string 
//							topTasks.add(4, allTasks.get(i));
//						}
//						// the elements' other indices are shifted down, so just need to
//						// remove the last two elements of the array
//						topTasks.remove(topTasks.size() - 1);
//						topTasks.remove(topTasks.size() - 1);
//					} else {
//						// if the task's due date is greater than the task due date at index 3
//						// then compare whether it's greater than to the last task due date in the array (index 5) 
//						compareDueDateString = topTasks.get(5);
//						compareDueDate = convertToLocalDate(compareDueDateString);
//						compareDays = today.until(compareDueDate, ChronoUnit.DAYS);
//						if (days < compareDays) {
//							// if it's less than index 5, then it replaces it as the last item in the array 
//							
//							// add the current index of allTasks to the topTasks array
//							// get the index minus one to get the Title string 
//							topTasks.add(5, allTasks.get(i - 1));
//							// the current index of allTasks is the due date string 
//							topTasks.add(6, allTasks.get(i));
//							topTasks.remove(topTasks.size() - 1);
//							topTasks.remove(topTasks.size() - 1);
//						}
//						// if the due date is greater than the last item in the array, 
//						// then it's not added to the array
//					}
//				
//				}
//				
//			};
//		}
//		
//
//
//		
//		return topTasks;
//	}
//	
//	public LocalDate convertToLocalDate(String stringDate) {
//		
//		// first split the line to be "Due date" and "YYYY-MM-DD"
//		String[] splitdata = stringDate.split(": ");
//		// then split this data into YYYY, MM, and DD
//		String[] formattedData = splitdata[1].split("-");
//		// convert YYYY string into int 
//		int year = Integer.parseInt(formattedData[0]);
//		// convert MM string into int 
//		int month = Integer.parseInt(formattedData[1]);
//		// convert DD string into int 
//		int day = Integer.parseInt(formattedData[2]);
//		// type cast date to LocalDate
//		LocalDate due = LocalDate.of(year, month, day);
//		
//		return due;
//	}
//}
//
//
//
//
//
//
//
//
//
//
//
//
