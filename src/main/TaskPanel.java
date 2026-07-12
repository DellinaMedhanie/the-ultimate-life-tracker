package main;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.LineBorder;

public class TaskPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	List<String> taskData = new ArrayList<>();
	List<String> tasks = new ArrayList<>();
	
	
	public TaskPanel() {
		
		readTextFile();
		
		this.setLayout(null);
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		JLabel label = new JLabel(); 
		label.setText("Task Tracker");
		label.setBounds(217, 20, 99, 20);
		
		Button taskButton = new Button("Add a task", "add task");
		Dimension buttonSize = taskButton.getPreferredSize();
		taskButton.setBounds(362, 34, buttonSize.width, buttonSize.height);		
		
		this.add(label);
		this.add(taskButton);
		this.add(TaskList(tasks));
		
		JLabel lblNewLabel = new JLabel("All tasks");
		lblNewLabel.setBounds(75, 52, 61, 16);
		add(lblNewLabel);
		this.setVisible(true);
				
	}
	
	public JScrollPane TaskList(List<String> taskData) {
				
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
						System.out.println(selectedValue);
						TaskDetail detailView = new TaskDetail(selectedValue);
						detailView.main(null);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(75, 75, 400, 350);		
		
		return scrollPane;
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
