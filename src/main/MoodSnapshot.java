package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MoodSnapshot extends JPanel {

	private static final long serialVersionUID = 1L;

	private CurrentUser user;
	private String filePath;
	
	private ArrayList<String> moodEntries = new ArrayList<String>();

	
	/**
	 * Create the panel.
	 */
	public MoodSnapshot(CurrentUser user) {
		this.setBounds(25, 42, 236, 151);
		this.setLayout(null);
		
		// save user data locally 
		this.user = user;
		
		// read mood.txt and put data in moodEntires ArrayList 
		readMoodFile();
		
		// seven day average 
		String dAverage = calcSevenDayAverage();
		// month average 
		String mAverage = calcMonthAverage();
		// year average 
		String yAverage = calcYearAverage();
		
		JLabel lblNewLabel_2 = new JLabel("Average mood rating");
		lblNewLabel_2.setBounds(52, 5, 131, 16);
		this.add(lblNewLabel_2);
		
		JLabel sevenDaysAverage = new JLabel("Past 7 days: " + dAverage);
		sevenDaysAverage.setBounds(0, 43, 230, 16);
		this.add(sevenDaysAverage);
		
		JLabel monthAverage = new JLabel("Month: " + mAverage);
		monthAverage.setBounds(0, 71, 230, 16);
		this.add(monthAverage);
		
		JLabel yearAverage = new JLabel("Year: " + yAverage);
		yearAverage.setBounds(0, 99, 230, 16);
		this.add(yearAverage);
	}

	public void readMoodFile() {
		String name = this.user.getUsername();
		filePath = "files/" + name + "/mood.txt";
		
		try {
			File file = new File(filePath);
			Scanner reader = new Scanner(file);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();

				if (!line.isBlank()) {
					moodEntries.add(line);
				}
			}

			reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String calcSevenDayAverage() {
		ArrayList<String> pastSevenDays = new ArrayList<String>();

		// search through moodEntires
		for (String entry : moodEntries) {
			// split the mood entry by "|" to be able to grab just the date
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 0, since this is the date 
			String date = parts[0];
			
			// Parse the date string and drop the time component
			// need to convert the String to LocalDate to compare with today's date
	        LocalDateTime timestamp = LocalDateTime.parse(date);
	        LocalDate targetDate = timestamp.toLocalDate();
	        
	        // Get what today's date is
	        LocalDate today = LocalDate.now();
	        
	        // Calculate the amount of days between the date of entry and today's date
	        long daysAgo = ChronoUnit.DAYS.between(targetDate, today);
	        
	        // see if it's within the past 7 days and add to pastSevenDays
	        if (daysAgo >= 0 && daysAgo <= 7) {
	            pastSevenDays.add(entry);
	        }
			
		}
		
		Integer totalMoodScore = 0;
		// go through pastSevenDays array and add up mood entry in separate variable 
		for (String entry : pastSevenDays) {
			// split the mood entry by "|" to be able to grab the mood score 
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 1, since this is the mood score  
			String moodScore = parts[1];
			// need to clean the data, since the mood score is "5 - Great". Want just the Integer. 
			// can split by space 
			String[] moodParts = moodScore.split(" ");
			// grab the first item in the array, which will be just the number.
			String moodScoreNum = moodParts[0];
			// convert to Integer and then add to totalMoodScore 
			totalMoodScore += Integer.parseInt(moodScoreNum);
		}
		
		Integer pastSevenDaysLength = pastSevenDays.size();
		// divide by the length of pastSevenDays array to find average
		// account for if there is no data for the past seven days to avoid divide by 0 error
		if (pastSevenDaysLength > 0) {			
			Integer pastSevenDaysAverage = totalMoodScore / pastSevenDaysLength;
			// convert to a string to return to show as a label on the panel 
			return String.valueOf(pastSevenDaysAverage);
		} else {
			return "No data for the past 7 days";
		}
	}
	
	public String calcMonthAverage() {
		ArrayList<String> pastMonth = new ArrayList<String>();
		
		// search through moodEntires
		for (String entry : moodEntries) {
			// split the mood entry by "|" to be able to grab just the date
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 0, since this is the date 
			String date = parts[0];
			
			// Parse the date string and drop the time component
			// need to conver the String to LocalDate to compare with today's date
	        LocalDateTime timestamp = LocalDateTime.parse(date);
	        // use YearMonth to target the Month of the timestamp data 
	        YearMonth targetYearMonth = YearMonth.from(timestamp);
	        
	        // Get the Month that is from today's date
	        YearMonth currentYearMonth = YearMonth.now();
	        
	        // See if the entry is the same as the current month 
	        if (targetYearMonth.equals(currentYearMonth)) {
	            pastMonth.add(entry);
	        } 
		}
		
		Integer totalMoodScore = 0;
		// go through pastMonth array and add up mood entry in separate variable 
		for (String entry : pastMonth) {
			// split the mood entry by "|" to be able to grab the mood score 
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 1, since this is the mood score  
			String moodScore = parts[1];
			// need to clean the data, since the mood score is "5 - Great". Want just the Integer. 
			// can split by space 
			String[] moodParts = moodScore.split(" ");
			// grab the first item in the array, which will be just the number.
			String moodScoreNum = moodParts[0];
			// convert to Integer and then add to totalMoodScore 
			totalMoodScore += Integer.parseInt(moodScoreNum);
		}
		
		Integer pastMonthLength = pastMonth.size();
		// divide by the length of pastSevenDays array to find average
		// account for if there is no data for the past seven days to avoid divide by 0 error
		if (pastMonthLength > 0) {			
			Integer pastMonthAverage = totalMoodScore / pastMonthLength;
			// convert to a string to return to show as a label on the panel 
			return String.valueOf(pastMonthAverage);
		} else {
			return "No data for the past month";
		}
	}

	
	public String calcYearAverage() {
		ArrayList<String> pastYear = new ArrayList<String>();
				
		// search through moodEntires
		for (String entry : moodEntries) {
			// split the mood entry by "|" to be able to grab just the date
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 0, since this is the date 
			String date = parts[0];
			
			// Parse the date string and drop the time component
			// need to conver the String to LocalDate to compare with today's date
	        LocalDateTime timestamp = LocalDateTime.parse(date);
	        // use YearMonth to target the Month of the timestamp data 
	        Year targetYear = Year.from(timestamp);
	        
	        // 2. Get the Year for today
	        Year currentYear = Year.now();
	        
	        // See if the entry is the same as the current month 
	        if (targetYear.equals(currentYear)) {
	            pastYear.add(entry);
	        } 
		}
		
		Integer totalMoodScore = 0;
		// go through pastMonth array and add up mood entry in separate variable 
		for (String entry : pastYear) {
			// split the mood entry by "|" to be able to grab the mood score 
			String[] parts = entry.split("\\s*\\|\\s*");
			// grab index 1, since this is the mood score  
			String moodScore = parts[1];
			// need to clean the data, since the mood score is "5 - Great". Want just the Integer. 
			// can split by space 
			String[] moodParts = moodScore.split(" ");
			// grab the first item in the array, which will be just the number.
			String moodScoreNum = moodParts[0];
			// convert to Integer and then add to totalMoodScore 
			totalMoodScore += Integer.parseInt(moodScoreNum);
		}
		
		Integer pastMonthLength = pastYear.size();
		// divide by the length of pastSevenDays array to find average
		// account for if there is no data for the past seven days to avoid divide by 0 error
		if (pastMonthLength > 0) {			
			Integer pastMonthAverage = totalMoodScore / pastMonthLength;
			// convert to a string to return to show as a label on the panel 
			return String.valueOf(pastMonthAverage);
		} else {
			return "No data for the past year";
		}
	}
}
