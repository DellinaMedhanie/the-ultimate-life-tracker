package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TaskTracker extends JPanel {
	
	List<String> taskData = new ArrayList<>();
	List<String> tasks = new ArrayList<>();
	
	
	TaskTracker() {
		
		readTextFile();
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		this.setBounds(50, 50, 700, 700);
		this.setLayout(null);
		
		JLabel testing = new JLabel(); 
		testing.setText("This is the task tracker");
		testing.setBounds(300, 0, 200, 100);
		
		Button taskButton = new Button("Add a task", "add task");
		Dimension buttonSize = taskButton.getPreferredSize();
		taskButton.setBounds(500, 10, buttonSize.width, buttonSize.height);
		
		String[] items = {"movie", "walk", "cookies"};
		
		
		this.add(testing);
		this.add(taskButton);
		this.add(TaskList(tasks));
		this.setVisible(true);
;		
//		this.add(taskList);		
		
	}
	
	public JScrollPane TaskList(List<String> taskData) {
		
		JPanel taskList = new JPanel();
		
		

//			this.add(testing);
//			this.add(taskButton);
		
//			taskList.setBounds(100, 100, 300, 300);
		
//			taskList.setBackground(Color.MAGENTA);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for (int i = 0; i < taskData.size(); i++) {
			model.addElement(taskData.get(i));
		};
		
		JList<String> list = new JList<>(model);
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JList<String> sourceList = (JList<String>) e.getSource();
					String selectedValue = sourceList.getSelectedValue();
					
					if (selectedValue != null) {
						detailView(selectedValue);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		JLabel taskLabel = new JLabel();
		taskLabel.setText("ALl tasks");
		taskLabel.setBounds(175, 80, 50, 50);
		
		scrollPane.setBounds(175, 100, 400, 450);		
		
		
		
		return scrollPane;
	}
	
	public JPanel detailView(String taskValue) {
		
		return new JPanel();
	}
	
	public void readTextFile() {
		// read data from tasks file to find names of tasks
		File f = new File("files/alice/tasks.txt");
		
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
