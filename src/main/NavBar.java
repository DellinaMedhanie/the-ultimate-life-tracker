package main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class NavBar {

	private JFrame frame;	
	private static JPanel cardPanel;
		
	private TopBar tBar = new TopBar();
	private MoodPanel moodTracker = new MoodPanel();
	private static TaskPanel taskTracker = new TaskPanel();
	private FinancePanel financeTracker = new FinancePanel(); 
	private PomodoroPanel pomodoroTimer = new PomodoroPanel();
	private HomePanel homePage = new HomePanel();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NavBar window = new NavBar();
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
	public NavBar() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(700, 600);
		frame.setTitle("Life Management System");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(new Color(0, 100, 100));
		
		JPanel panel = new JPanel();
		panel.setBounds(17, 51, 124, 438);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Home");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(homePage);
				cardPanel.revalidate();
				cardPanel.repaint();
			}
		});
		btnNewButton.setBounds(6, 32, 117, 29);
		panel.add(btnNewButton);
		
		JButton btnTasks = new JButton("Tasks");
		btnTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(taskTracker);
				cardPanel.revalidate();
				cardPanel.repaint();
			}
		});
		btnTasks.setBounds(6, 73, 117, 29);
		panel.add(btnTasks);
		
		JButton btnNewButton_2 = new JButton("Mood");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(moodTracker);
				cardPanel.revalidate();
				cardPanel.repaint();
			}
		});
		btnNewButton_2.setBounds(6, 114, 117, 29);
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_2_1 = new JButton("Finances");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(financeTracker);
				cardPanel.revalidate();	
				cardPanel.repaint();
			}
		});
		btnNewButton_2_1.setBounds(6, 155, 117, 29);
		panel.add(btnNewButton_2_1);
		
		JButton btnNewButton_2_1_1 = new JButton("Pomodoro");
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(pomodoroTimer);
				cardPanel.revalidate();	
				cardPanel.repaint();
			}
		});
		btnNewButton_2_1_1.setBounds(6, 196, 117, 29);
		panel.add(btnNewButton_2_1_1);
		
		frame.getContentPane().add(tBar);

		
		cardPanel = new JPanel();
		cardPanel.setBounds(140, 51, 543, 438);
		frame.getContentPane().add(cardPanel);
		cardPanel.setLayout(new CardLayout(0, 0));
		
		
		// on initialization, have homePage be the default
		cardPanel.add(homePage);
		cardPanel.revalidate();
		cardPanel.repaint();
	}
	
	public static void reFreshCardPanel() {
		// first remove all Panels to "blank slate" the GUI
		cardPanel.removeAll();
		// creates a new taskPanel to trigger a re-read on the task data file 
		// and update the list to be rendered on the GUI panel
		taskTracker = new TaskPanel();
		// add this updated taskTracker to the cardPanel which now includes 
		// the newly added task
		cardPanel.add(taskTracker);
		// this tells the layout manager to recalculate component sizes and positions
		cardPanel.revalidate(); 
		// visually redraws component on the screen  
		cardPanel.repaint(); 
	}
}
