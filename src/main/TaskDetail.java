package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class TaskDetail extends JFrame implements ActionListener {
	
	List<String> taskDetails = new ArrayList<>();

	private JFrame frame;
	private static String taskName = " ";
	private final CurrentUser user;
	
	private String taskTitle; 
	private String taskPriority; 
	private String taskStatus; 
	
	private JButton saveButton;
	private JComboBox status;
	private JComboBox priority;
	private TaskPanel tPanel; 
		
	private String statusStates[] 
			= {"Not started", "In progress", "blocked", 
				"complete", "cancelled"};

	private String priorityStates[] = {"Low", "Medium", "High", "Critical"};

	/**
	 * Create the application.
	 */
	public TaskDetail(String value, CurrentUser user, TaskPanel tPanel) {
		this.user = user;
		this.tPanel = tPanel;
		// need to use "//" before "|" to escape special reserved character in regex that means "OR".
		String[] valueData = value.split(" \\| ");
		taskTitle = valueData[0].trim();
		taskPriority = valueData[1].trim();
		taskStatus = valueData[2].trim();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		// don't use setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// otherwise it will close both the detail frame and the main window frame
		frame.setTitle("Task detail for " + taskTitle);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(0, 150, 150));
		frame.getContentPane().setLayout(null);
		
		readTextFile(); 
		
		JLabel lblNewLabel = new JLabel("Task Name: ");
		lblNewLabel.setBounds(39, 23, 78, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(taskTitle);
		lblNewLabel_1.setBounds(126, 23, 256, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Priority: ");
		lblNewLabel_2.setBounds(39, 51, 61, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		priority = new JComboBox(priorityStates);
		// get index of priorityStates based on taskDetails priority 
		int priorityIndex = Arrays.asList(priorityStates).indexOf(taskPriority); 
		priority.setSelectedItem(taskPriority);
		priority.setBounds(126, 51, 256, 16);
		frame.getContentPane().add(priority);
		
		JLabel lblNewLabel_4 = new JLabel("Status");
		lblNewLabel_4.setBounds(39, 79, 61, 16);
		frame.getContentPane().add(lblNewLabel_4);
		
		status = new JComboBox(statusStates);
		int statusIndex = Arrays.asList(statusStates).indexOf(taskStatus); 
		status.setSelectedIndex(statusIndex);
		status.setBounds(126, 79, 266, 16);
		frame.getContentPane().add(status);
		
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
		
		saveButton = new JButton("Save ");
		saveButton.setBounds(300, 209, 117, 29);
		saveButton.addActionListener(this);
		frame.getContentPane().add(saveButton);
				
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {			
			saveEdits(priority.getSelectedItem().toString(), status.getSelectedItem().toString());
		} 
	}
	
	public void readTextFile() {
		// read data from tasks file to find names of tasks
		String name = this.user.getUsername();
		File f = new File("files/" + name + "/tasks.txt");
		Boolean foundTask = false; 
		
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine() && !foundTask)  {
				String data = reader.nextLine(); 
				// if you find the task 
				if (data.contains(taskTitle)) {
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
	
	public void saveEdits(String savedPriority, String savedStatus) {
		
		// read data from tasks file to find names of tasks
		String name = this.user.getUsername();
		File f = new File("files/" + name + "/tasks.txt");
		Boolean foundTask = false; 
		
		try {
	        // Read all lines from the file into memory
	        java.nio.file.Path path = f.toPath();
	        java.util.List<String> lines = java.nio.file.Files.readAllLines(path);

	        // Loop through the memory list to find and replace the line
	        for (int i = 0; i < lines.size(); i++) {
	            String data = lines.get(i);

	            // find the task by the name of the task 
	            if (data.contains(taskTitle)) {
	            	// overwrite the priority + status of the task 
	            	lines.set(i + 3, "Priority: " + savedPriority);
	            	lines.set(i + 4, "Status: " + savedStatus);
	                
	                foundTask = true;
	            }
	        }

	        // 3. If found, write the modified list back to disk
	        if (foundTask) {
	            java.nio.file.Files.write(path, lines);
	        } else {
	            System.out.println("Task not found in file.");
	        }

	    } catch (IOException e) {
	        System.out.println("Error occurred during file read/write");
	        e.printStackTrace();
	    }
				
		frame.dispose();
		tPanel.refreshUI(); 
		JOptionPane.showMessageDialog(this, "edits saved successfully");
	}
}
