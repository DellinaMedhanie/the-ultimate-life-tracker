package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 * Single-file version of the Pomodoro Timer feature: UI, state machine, and
 * file I/O all live here. See PomodoroSession and PomodoroService below the
 * main class for the model/service pieces (kept package-private since only
 * one public class is allowed per file).
 */
public class PomodoroPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// a full cycle is 4 focus sessions, each followed by a break
	// (the 4th break is the long break)
	private static final int SESSIONS_PER_CYCLE = 4;

	private static final int DEFAULT_FOCUS_MINUTES = 25;
	private static final int DEFAULT_BREAK_MINUTES = 5;
	private static final int DEFAULT_LONG_BREAK_MINUTES = 15;

	// tracks what the timer is currently doing
	private enum Phase {
		IDLE, FOCUS_RUNNING, FOCUS_PAUSED, AWAITING_BREAK, BREAK_RUNNING, BREAK_PAUSED, AWAITING_FOCUS
	}

	private Phase phase = Phase.IDLE;
	private int currentSessionNumber = 1;
	private int plannedSeconds;
	private int remainingSeconds;
	private boolean muted = false;

	// javax.swing.Timer is used here (rather than java.util.Timer) since it runs
	// its callback on the Event Dispatch Thread, which is what Swing components need
	private Timer countdownTimer;

	private JLabel statusLabel;
	private JLabel timeLabel;
	private JComboBox<String> taskCombo;
	private JSpinner focusSpinner;
	private JSpinner breakSpinner;
	private JSpinner longBreakSpinner;
	private JButton startButton;
	private JButton pauseButton;
	private JButton endEarlyButton;
	private JCheckBox muteCheckbox;
	private DefaultListModel<String> logModel;

	/**
	 * Create the panel.
	 */
	public PomodoroPanel() {
		this.setLayout(null);
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));

		JLabel title = new JLabel("Pomodoro Timer", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		title.setBounds(20, 10, 500, 25);
		this.add(title);

		statusLabel = new JLabel("Ready to start a new Pomodoro cycle", SwingConstants.CENTER);
		statusLabel.setBounds(20, 42, 500, 20);
		this.add(statusLabel);

		timeLabel = new JLabel(formatTime(DEFAULT_FOCUS_MINUTES * 60), SwingConstants.CENTER);
		timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 40f));
		timeLabel.setBounds(20, 65, 500, 55);
		this.add(timeLabel);

		JLabel taskLabel = new JLabel("Focus on task:");
		taskLabel.setBounds(30, 130, 90, 20);
		this.add(taskLabel);

		taskCombo = new JComboBox<>();
		taskCombo.addItem("No task specified");
		for (String taskTitle : PomodoroService.getTaskTitles()) {
			taskCombo.addItem(taskTitle);
		}
		taskCombo.setBounds(125, 130, 250, 24);
		this.add(taskCombo);

		JLabel focusLabel = new JLabel("Focus (min):");
		focusLabel.setBounds(30, 165, 85, 20);
		this.add(focusLabel);

		focusSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_FOCUS_MINUTES, 1, 180, 1));
		focusSpinner.setBounds(120, 163, 55, 24);
		this.add(focusSpinner);

		JLabel breakLabel = new JLabel("Break (min):");
		breakLabel.setBounds(190, 165, 85, 20);
		this.add(breakLabel);

		breakSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_BREAK_MINUTES, 1, 60, 1));
		breakSpinner.setBounds(280, 163, 55, 24);
		this.add(breakSpinner);

		JLabel longBreakLabel = new JLabel("Long break (min):");
		longBreakLabel.setBounds(350, 165, 105, 20);
		this.add(longBreakLabel);

		longBreakSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_LONG_BREAK_MINUTES, 1, 60, 1));
		longBreakSpinner.setBounds(460, 163, 55, 24);
		this.add(longBreakSpinner);

		startButton = new JButton("Start Session");
		startButton.setBounds(30, 205, 130, 32);
		startButton.setFocusable(false);
		this.add(startButton);

		pauseButton = new JButton("Pause");
		pauseButton.setBounds(170, 205, 100, 32);
		pauseButton.setFocusable(false);
		pauseButton.setEnabled(false);
		this.add(pauseButton);

		endEarlyButton = new JButton("End Early");
		endEarlyButton.setBounds(280, 205, 100, 32);
		endEarlyButton.setFocusable(false);
		endEarlyButton.setEnabled(false);
		this.add(endEarlyButton);

		muteCheckbox = new JCheckBox("Mute sound");
		muteCheckbox.setBounds(400, 210, 120, 24);
		muteCheckbox.addActionListener(e -> muted = muteCheckbox.isSelected());
		this.add(muteCheckbox);

		JLabel logLabel = new JLabel("Session log:");
		logLabel.setBounds(30, 250, 100, 20);
		this.add(logLabel);

		logModel = new DefaultListModel<>();
		for (String entry : PomodoroService.getLog()) {
			logModel.addElement(entry);
		}
		JList<String> logList = new JList<>(logModel);
		JScrollPane logScroll = new JScrollPane(logList);
		logScroll.setBounds(30, 272, 485, 140);
		this.add(logScroll);

		// wire up the button behavior now that all the components exist
		startButton.addActionListener(e -> onStartClicked());
		pauseButton.addActionListener(e -> onPauseClicked());
		endEarlyButton.addActionListener(e -> onEndEarlyClicked());

		countdownTimer = new Timer(1000, e -> onTick());
	}

	private void onStartClicked() {
		if (phase == Phase.IDLE || phase == Phase.AWAITING_FOCUS) {
			startFocusSession();
		} else if (phase == Phase.AWAITING_BREAK) {
			startBreak();
		}
	}

	private void startFocusSession() {
		plannedSeconds = ((Integer) focusSpinner.getValue()) * 60;
		remainingSeconds = plannedSeconds;
		phase = Phase.FOCUS_RUNNING;

		statusLabel.setText("Focus session " + currentSessionNumber + " of " + SESSIONS_PER_CYCLE);
		timeLabel.setText(formatTime(remainingSeconds));

		setRunningControlsEnabled(true);
		countdownTimer.start();
	}

	private void startBreak() {
		boolean isLongBreak = currentSessionNumber == SESSIONS_PER_CYCLE;
		int breakMinutes = isLongBreak ? (Integer) longBreakSpinner.getValue() : (Integer) breakSpinner.getValue();
		plannedSeconds = breakMinutes * 60;
		remainingSeconds = plannedSeconds;
		phase = Phase.BREAK_RUNNING;

		statusLabel.setText((isLongBreak ? "Long break" : "Break") + " " + currentSessionNumber + " of "
				+ SESSIONS_PER_CYCLE);
		timeLabel.setText(formatTime(remainingSeconds));

		setRunningControlsEnabled(true);
		countdownTimer.start();
	}

	private void onPauseClicked() {
		if (phase == Phase.FOCUS_RUNNING) {
			countdownTimer.stop();
			phase = Phase.FOCUS_PAUSED;
			pauseButton.setText("Resume");
		} else if (phase == Phase.BREAK_RUNNING) {
			countdownTimer.stop();
			phase = Phase.BREAK_PAUSED;
			pauseButton.setText("Resume");
		} else if (phase == Phase.FOCUS_PAUSED) {
			countdownTimer.start();
			phase = Phase.FOCUS_RUNNING;
			pauseButton.setText("Pause");
		} else if (phase == Phase.BREAK_PAUSED) {
			countdownTimer.start();
			phase = Phase.BREAK_RUNNING;
			pauseButton.setText("Pause");
		}
	}

	private void onEndEarlyClicked() {
		if (phase == Phase.FOCUS_RUNNING || phase == Phase.FOCUS_PAUSED
				|| phase == Phase.BREAK_RUNNING || phase == Phase.BREAK_PAUSED) {
			completeCurrentSegment();
		}
	}

	private void onTick() {
		remainingSeconds--;
		timeLabel.setText(formatTime(remainingSeconds));

		if (remainingSeconds <= 0) {
			completeCurrentSegment();
		}
	}

	/**
	 * Called whenever the current focus session or break ends, whether it counted
	 * all the way down naturally or was ended early.
	 */
	private void completeCurrentSegment() {
		countdownTimer.stop();
		int elapsedSeconds = plannedSeconds - Math.max(remainingSeconds, 0);

		if (phase == Phase.FOCUS_RUNNING || phase == Phase.FOCUS_PAUSED) {
			logFocusSession(elapsedSeconds);
			notifyUser("Focus session " + currentSessionNumber + " complete!", "Time for a break.");

			phase = Phase.AWAITING_BREAK;
			boolean isLongBreak = currentSessionNumber == SESSIONS_PER_CYCLE;
			startButton.setText(isLongBreak ? "Start Long Break" : "Start Break");
			statusLabel.setText("Focus session " + currentSessionNumber + " of " + SESSIONS_PER_CYCLE
					+ " complete. Ready for a break?");

		} else if (phase == Phase.BREAK_RUNNING || phase == Phase.BREAK_PAUSED) {
			if (currentSessionNumber == SESSIONS_PER_CYCLE) {
				notifyUser("Pomodoro cycle complete!", "You finished all " + SESSIONS_PER_CYCLE
						+ " focus sessions. Great work!");
				currentSessionNumber = 1;
				phase = Phase.IDLE;
				startButton.setText("Start Session");
				statusLabel.setText("Pomodoro cycle complete! Ready to start a new cycle.");
			} else {
				notifyUser("Break complete!", "Ready for the next focus session.");
				currentSessionNumber++;
				phase = Phase.AWAITING_FOCUS;
				startButton.setText("Start Session " + currentSessionNumber);
				statusLabel.setText("Break complete. Ready for focus session " + currentSessionNumber + "?");
			}
		}

		timeLabel.setText(formatTime(0));
		setRunningControlsEnabled(false);
	}

	private void logFocusSession(int elapsedSeconds) {
		String task = (String) taskCombo.getSelectedItem();
		double minutes = elapsedSeconds / 60.0;
		PomodoroSession session = new PomodoroSession(task, minutes, LocalDateTime.now());
		PomodoroService.logSession(session);
		logModel.addElement(session.toString());
	}

	private void notifyUser(String title, String message) {
		if (!muted) {
			Toolkit.getDefaultToolkit().beep();
		}
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Enables/disables the controls that shouldn't change while a countdown is actively running.
	 */
	private void setRunningControlsEnabled(boolean running) {
		pauseButton.setEnabled(running);
		pauseButton.setText("Pause");
		endEarlyButton.setEnabled(running);
		startButton.setEnabled(!running);

		// duration settings shouldn't change mid-countdown, but can be adjusted between segments
		focusSpinner.setEnabled(!running);
		breakSpinner.setEnabled(!running);
		longBreakSpinner.setEnabled(!running);
	}

	private String formatTime(int totalSeconds) {
		int minutes = Math.max(totalSeconds, 0) / 60;
		int seconds = Math.max(totalSeconds, 0) % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

}

// ---------------------------------------------------------------------------
// Model layer
// Represents one completed focus session that gets written to the Pomodoro log.
// Package-private (not public) since only one public class is allowed per file.
// ---------------------------------------------------------------------------
class PomodoroSession {
	/*
	 * task: String -> the task the user was focusing on (nullable, "No task specified" if none chosen)
	 * durationMinutes: double -> how long was actually spent on the task (handles sessions ended early)
	 * loggedAt: LocalDateTime -> timestamp of when the focus session was completed/ended
	 */
	private String task;
	private double durationMinutes;
	private LocalDateTime loggedAt;

	public PomodoroSession(String task, double durationMinutes, LocalDateTime loggedAt) {
		this.task = (task == null || task.isBlank()) ? "No task specified" : task;
		this.durationMinutes = durationMinutes;
		this.loggedAt = loggedAt;
	}

	public String getTask() {
		return task;
	}

	public double getDurationMinutes() {
		return durationMinutes;
	}

	public LocalDateTime getLoggedAt() {
		return loggedAt;
	}

	@Override
	public String toString() {
		return "Task: " + task + " | Duration (min): " + String.format("%.1f", durationMinutes)
				+ " | Logged at: " + loggedAt;
	}
}

// ---------------------------------------------------------------------------
// Service layer
// Handles reading the task list and writing/reading the Pomodoro log file.
// Package-private (not public) since only one public class is allowed per file.
// ---------------------------------------------------------------------------
class PomodoroService {

	// TODO: this needs to change based on who the user is
	// hard coded right now for testing purposes, matches the rest of the app
	private static final String USER = "alice";

	/**
	 * Reads the user's task titles from their tasks file, the same way TaskPanel does,
	 * so the Pomodoro timer can offer them as a "focus on this task" option.
	 */
	public static List<String> getTaskTitles() {
		List<String> tasks = new ArrayList<>();
		File f = new File("files/" + USER + "/tasks.txt");

		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				if (data.contains("Title")) {
					String[] splitData = data.split(":");
					if (splitData.length > 1) {
						tasks.add(splitData[1].trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("No tasks file found for " + USER);
		}

		return tasks;
	}

	/**
	 * Appends a completed focus session to the Pomodoro log file for the user.
	 * The log tracks how long was spent, when it was logged, and what task (if any)
	 * it was spent on.
	 */
	public static void logSession(PomodoroSession session) {
		File dir = new File("files/" + USER);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String file = "files/" + USER + "/pomodoro_log.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.write(session.toString());
			writer.newLine();
		} catch (IOException err) {
			System.out.println("Whoops, sad day, we got an error :(");
			err.printStackTrace();
		}
	}

	/**
	 * Reads back the full Pomodoro log so it can be displayed to the user.
	 */
	public static List<String> getLog() {
		List<String> entries = new ArrayList<>();
		File f = new File("files/" + USER + "/pomodoro_log.txt");

		try (Scanner reader = new Scanner(f)) {
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				if (!data.isBlank()) {
					entries.add(data);
				}
			}
		} catch (FileNotFoundException e) {
			// no sessions logged yet, that's fine
		}

		return entries;
	}
}
