package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TaskDetail {
	
	List<String> taskDetails = new ArrayList<>();

	private JFrame frame;
	private static String taskName = " ";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskDetail window = new TaskDetail(taskName);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TaskDetail(String value) {
		setTaskName(value);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Task detail for " + getTaskName());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(0, 150, 150));
		frame.getContentPane().setLayout(null);
		
		readTextFile(); 
		System.out.println(taskDetails);
		
		JLabel lblNewLabel = new JLabel("Task Name: ");
		lblNewLabel.setBounds(39, 23, 78, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(getTaskName());
		lblNewLabel_1.setBounds(126, 23, 256, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Priority: ");
		lblNewLabel_2.setBounds(39, 51, 61, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		String priority = taskDetails.get(2);
		JLabel lblNewLabel_3 = new JLabel(priority);
		lblNewLabel_3.setBounds(126, 51, 256, 16);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Status");
		lblNewLabel_4.setBounds(39, 79, 61, 16);
		frame.getContentPane().add(lblNewLabel_4);
		
		String status = taskDetails.get(3);
		JLabel lblNewLabel_5 = new JLabel(status);
		lblNewLabel_5.setBounds(126, 79, 266, 16);
		frame.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Due date");
		lblNewLabel_6.setBounds(39, 104, 61, 16);
		frame.getContentPane().add(lblNewLabel_6);
		
		String dueDate = taskDetails.get(4);
		JLabel lblNewLabel_7 = new JLabel(dueDate);
		lblNewLabel_7.setBounds(126, 104, 266, 16);
		frame.getContentPane().add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Notes");
		lblNewLabel_8.setBounds(39, 137, 61, 16);
		frame.getContentPane().add(lblNewLabel_8);
		
		String notes = taskDetails.get(0);
		JLabel lblNewLabel_9 = new JLabel(notes);
		lblNewLabel_9.setBounds(120, 137, 285, 101);
		frame.getContentPane().add(lblNewLabel_9);
				
	}
	
	public void setTaskName(String tName) {
		taskName = tName;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void readTextFile() {
		// read data from tasks file to find names of tasks
		String user = "alice";
		File f = new File("files/" + user + "/tasks.txt");
		Boolean foundTask = false; 
		
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine() && !foundTask)  {
				String data = reader.nextLine(); 
				// if you find the task 
				if (data.contains(getTaskName())) {
					// read the next 5 lines of data
					for (int i = 0; i < 5; i++) {
						data = reader.nextLine();
						String[] splitData = data.split(":");						
						taskDetails.add(splitData[1]);
					}
					foundTask = true;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
	}

}
