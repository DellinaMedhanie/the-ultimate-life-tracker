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

public class NavBar extends JFrame {

	private JFrame frame;	
	private static JPanel cardPanel;
		
	private TopBar tBar;
	private MoodPanel moodTracker;
	private static TaskPanel taskTracker;
	private FinancePanel financeTracker; 
	private PomodoroPanel pomodoroTimer;
	private HomePanel homePage;
	
	private final CurrentUser user; 

	/**
	 * Create the application.
	 */
	public NavBar(CurrentUser userObj) {
		this.user =  userObj;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {		
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(700, 600);
		frame.setTitle("Life Management System");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(new Color(0, 100, 100));

		tBar = new TopBar(); 
		moodTracker = new MoodPanel(this.user); 
		taskTracker = new TaskPanel(this.user); 
		financeTracker = new FinancePanel(this.user); 
		pomodoroTimer = new PomodoroPanel(this.user); 
		homePage = new HomePanel(); 
		
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
		
		JButton logOut = new JButton("Log out");
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// close this window/frame 
				frame.dispose();
				// calls fresh login screen via main
				new Main().main(new String [0]);
			}
		});
		logOut.setBounds(6, 403, 117, 29);
		panel.add(logOut);
		
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
	
	public void reFreshCardPanel() {
		// first remove all Panels to "blank slate" the GUI
		cardPanel.removeAll();
		// creates a new taskPanel to trigger a re-read on the task data file 
		// and update the list to be rendered on the GUI panel
		taskTracker = new TaskPanel(this.user);
		// add this updated taskTracker to the cardPanel which now includes 
		// the newly added task
		cardPanel.add(taskTracker);
		// this tells the layout manager to recalculate component sizes and positions
		cardPanel.revalidate(); 
		// visually redraws component on the screen  
		cardPanel.repaint(); 
	}
	
    // Allows other files to access the window
    public JFrame getFrame() {
        return this.frame;
    }
}
