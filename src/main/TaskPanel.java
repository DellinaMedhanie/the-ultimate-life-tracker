package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class TaskPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JButton taskButton;
	
	List<String> taskData = new ArrayList<>();
	List<String> tasks = new ArrayList<>();
	
	private JScrollPane taskList;
	
	private final CurrentUser user;
	
	public TaskPanel(CurrentUser user) {
		// save variable locally 
		this.user = user;
		
		readTextFile();
		
		this.setLayout(null);
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		JLabel label = new JLabel(); 
		label.setText("Task Tracker");
		label.setBounds(217, 20, 99, 20);
		
		// need to pass user object to button to reference and save signed-in user's data
		taskButton = new JButton();
		taskButton.setText("Add task");
		taskButton.addActionListener(this);
				
		Dimension buttonSize = taskButton.getPreferredSize();
		taskButton.setBounds(362, 34, buttonSize.width, buttonSize.height);		
		
		taskList = TaskList(tasks);
		
		this.add(label);
		this.add(taskButton);
		this.add(taskList);
		
		JLabel lblNewLabel = new JLabel("All tasks");
		lblNewLabel.setBounds(75, 52, 61, 16);
		add(lblNewLabel);
		this.setVisible(true);
				
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == taskButton) {
			// need to pass in TaskPanel instance to be able to reference from TaskForm
			// to be able to update the UI in this taskPanel
			TaskForm form = new TaskForm(this.user, this);
		} 
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
						TaskDetail detailView = new TaskDetail(selectedValue, TaskPanel.this.user, TaskPanel.this);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(75, 75, 400, 350);		
		
		return scrollPane;
	}
	
	
	public void readTextFile() {
		// gets signed-in username from CurrentUser 
		// from the local instance of CurrentUser 
		String name = this.user.getUsername();
		System.out.println(name);
		File f = new File("files/" + name + "/tasks.txt");

		try {			
			if (!f.exists()) {
				f.createNewFile();
			}
		} catch (Exception e) {
			System.out.println("file could not be created");
		}
		
		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine(); 
				if (data.contains("Title")) {
					String[] splitData = data.split(":");
					// read three lines to get to the priority line in the text file 
					data = reader.nextLine(); 
					data = reader.nextLine(); 
					
					// this is the line that reads "Priotity: ____ " 
					data = reader.nextLine(); 
					String[] splitPriority = data.split(":");
					
					// read another line to get the status line 
					// "Status: ____ " 
					data = reader.nextLine(); 
					String[] splitStatus = data.split(":");
					
					// read another line to get the duedate 
					data = reader.nextLine(); 
					String[] splitDate = data.split(":");
					
					tasks.add(splitData[1] + " | " + splitPriority[1] + " | " + splitStatus[1] + " | " + splitDate[1]);

				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
	}
	
	public void refreshUI() {
		// need to remove the TaskList from the UI
		this.remove(taskList);
		// remove all tasks in the locally saved ArrayList
		tasks.clear();
		// read the tasks.txt to refresh the task ArrayList
		readTextFile();
		// re-add the updated task list with the fresh data
		taskList = TaskList(tasks);
		this.add(taskList);
		this.revalidate();
		this.repaint();
	}
	
}
