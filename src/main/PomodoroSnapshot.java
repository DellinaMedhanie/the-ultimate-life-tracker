package main;

import javax.swing.JPanel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JLabel;

public class PomodoroSnapshot extends JPanel {

	private static final long serialVersionUID = 1L;

	private CurrentUser user;
	private String filePath; 
	
	private ArrayList<String> recentSessions = new ArrayList<String>();

	
	/**
	 * Create the panel.
	 */
	public PomodoroSnapshot(CurrentUser user) {
		this.user = user;
		this.setBounds(284, 216, 236, 151);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Recent Pomodoro Sessions");
		lblNewLabel.setBounds(22, 6, 180, 16);
		add(lblNewLabel);

		
		recentSessions.add("");
		recentSessions.add("");
		recentSessions.add("");
		
		readPomodoroLogFile(); 
		
		// pomodoro session data 1
		String p1 = recentSessions.get(0); 
		// pomodoro session data 2
		String p2 = recentSessions.get(1); 
		// pomodoro session data 3
		String p3 = recentSessions.get(2);
		
		JLabel session1 = new JLabel(p1);
		session1.setBounds(6, 42, 224, 16);
		add(session1);
		
		JLabel session2 = new JLabel(p2);
		session2.setBounds(6, 81, 224, 16);
		add(session2);
		
		JLabel session3 = new JLabel(p3);
		session3.setBounds(6, 124, 224, 16);
		add(session3);
	}

	public void readPomodoroLogFile() {
		String name = this.user.getUsername();
		filePath = "files/" + name + "/pomodoro_log.txt";
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null && count < 3) {
            	recentSessions.add(count, line);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
