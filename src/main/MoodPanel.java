package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class MoodPanel extends JPanel implements ActionListener {

	static final long serialVersionUID = 1L;

// Components for the form and history 
	JComboBox<String> moodBox;
	JTextArea noteBox;
	JButton saveButton;
	JButton deleteButton;

	DefaultListModel<String> listModel;
	JList<String> moodList;


	JLabel trendLabel;

// Stores user entries
	ArrayList<String> entries = new ArrayList<String>();


	String filePath = "moods.txt";


	public MoodPanel() {
		this.setBounds(getVisibleRect());
		this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		this.setLayout(null);


		createFile();

		readFile();


		JLabel title = new JLabel("Mood Tracker");
		title.setBounds(200, 10, 150, 25);
		add(title);

// Mood entry components
		JLabel moodLabel = new JLabel("Mood:");
		moodLabel.setBounds(30, 55, 80, 20);
		add(moodLabel);


		String[] moods = {
				"Select Mood",
				"1 - Very Low",
				"2 - Low",
				"3 - Neutral",
				"4 - Good",
				"5 - Great"
		};


		moodBox = new JComboBox<String>(moods);
		moodBox.setBounds(100, 55, 160, 25);
		moodBox.addActionListener(this);
		add(moodBox);

// Optional note entry components
		JLabel noteLabel = new JLabel("Note:");
		noteLabel.setBounds(30, 95, 80, 20);
		add(noteLabel);


		noteBox = new JTextArea();
		noteBox.setLineWrap(true);
		noteBox.setWrapStyleWord(true);

		JScrollPane noteScroll = new JScrollPane(noteBox);
		noteScroll.setBounds(100, 95, 260, 60);
		add(noteScroll);


		saveButton = new JButton("Save");
		saveButton.setBounds(380, 110, 90, 30);
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		add(saveButton);

// History components 
		JLabel historyLabel = new JLabel("Mood History");
		historyLabel.setBounds(30, 180, 150, 20);
		add(historyLabel);


		listModel = new DefaultListModel<String>();
		moodList = new JList<String>(listModel);

		JScrollPane historyScroll = new JScrollPane(moodList);
		historyScroll.setBounds(30, 205, 330, 190);
		add(historyScroll);

// Delete button for editing a saved data entry
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(380, 205, 90, 30);
		deleteButton.addActionListener(this);
		add(deleteButton);

// Trend components
		trendLabel = new JLabel("Trend Summary: No entries yet");
		trendLabel.setBounds(360, 260, 180, 25);
		add(trendLabel);

		updateList();
		updateTrend();
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == moodBox) {
			if (moodBox.getSelectedIndex() > 0) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}

		if (e.getSource() == saveButton) {
			saveMood();
		}

		if (e.getSource() == deleteButton) {
			deleteMood();
		}
	}

// Saves mood entry and the optional note if added
	public void saveMood() {
		if (moodBox.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Please select a mood.");
			return;
		}

		String mood = moodBox.getSelectedItem().toString();
		String note = noteBox.getText().replace("\n", " ");

// All the information saved when an entry is recorded
		String entry = LocalDateTime.now() + " | " + mood + " | Note: " + note;

		entries.add(entry);
		writeFile();

// Clears the form after entry is saved
		moodBox.setSelectedIndex(0);
		noteBox.setText("");
		saveButton.setEnabled(false);

		updateList();
		updateTrend();

		JOptionPane.showMessageDialog(this, "Mood saved.");
	}

// Allows user to delete any entry they select
	public void deleteMood() {
		int index = moodList.getSelectedIndex();

		if (index == -1 || entries.size() == 0) {
			JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
			return;
		}

		if (moodList.getSelectedValue().equals("No Entries Yet")) {
			JOptionPane.showMessageDialog(this, "There are no entries to delete.");
			return;
		}


		entries.remove(index);
		writeFile();

		updateList();
		updateTrend();

		JOptionPane.showMessageDialog(this, "Mood deleted.");
	}

// Updates the mood history list
	public void updateList() {
		listModel.clear();

		if (entries.size() == 0) {
			listModel.addElement("No Entries Yet");
		} else {
			for (String entry : entries) {
				listModel.addElement(entry);
			}
		}
	}

// Shows a simple trend summary 
	public void updateTrend() {
		if (entries.size() == 0) {
			trendLabel.setText("Trend Summary: No data");
		} else {
			String latestEntry = entries.get(entries.size() - 1);
			String[] parts = latestEntry.split("\\|");
			String latestMood = parts[1].trim();

			trendLabel.setText("Latest: " + latestMood);
		}
	}


	public void createFile() {
		try {
			File file = new File(filePath);

			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			System.out.println("Could not create moods.txt.");
		}
	}


	public void readFile() {
		try {
			File file = new File(filePath);
			Scanner reader = new Scanner(file);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();

				if (!line.isBlank()) {
					entries.add(line);
				}
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("Could not read moods.txt.");
		}
	}

// Adds all mood entries to moods.txt file for storage
	public void writeFile() {
		try {
			FileWriter writer = new FileWriter(filePath);

			for (String entry : entries) {
				writer.write(entry + "\n");
			}

			writer.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Mood could not be saved.");
		}
	}
}
