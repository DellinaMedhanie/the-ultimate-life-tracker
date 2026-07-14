package main;

// code based on this example for a registration form in swing
// https://www.geeksforgeeks.org/java/java-swing-simple-user-registration-form/

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.FileWriter;   
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

class TaskForm
 extends JFrame
 implements ActionListener {

 // Components of the Form
 private Container c;
 private JLabel title;
 private JLabel name;
 private JTextField tname;
 private JLabel add;
 private JTextArea tadd;
 private JLabel tType;
 private JComboBox aType;
 private JLabel tPriority;
 private JComboBox aPriority;
 private JLabel tStatus;
 private JComboBox aStatus;
 private JLabel dob;
 private JComboBox date;
 private JComboBox month;
 private JComboBox year;
 private JButton sub;
 private JButton reset;

 private String dates[]
     = { "1", "2", "3", "4", "5",
         "6", "7", "8", "9", "10",
         "11", "12", "13", "14", "15",
         "16", "17", "18", "19", "20",
         "21", "22", "23", "24", "25",
         "26", "27", "28", "29", "30",
         "31" };
 private String months[]
     = { "Jan", "Feb", "Mar", "Apr",
         "May", "Jun", "July", "Aug",
         "Sep", "Oct", "Nov", "Dec" };
 private String years[]
     = { "2026", "2027", "2028", "2029", 
    	"2030", "2031", "2032", "2033",
    	"2034", "2035", "2036", "2037"};
 
// will expand out to include Subtasks in future versions
 private String type[] 
	= {"Task"};
  
 private String status[] 
	= {"Not started", "In progress", "blocked", 
		"complete", "cancelled"};
 
 private String priority[] 
	= {"Low", "Medium", "High", "Critical"};

 // constructor, to initialize the components
 // with default values.
 public TaskForm()
 {
     setTitle("Add a Task");
     setBounds(300, 90, 600, 600);
     setResizable(false);

     c = getContentPane();
     c.setLayout(null);

     title = new JLabel("Add a task 📝");
     title.setFont(new Font("Arial", Font.PLAIN, 30));
     title.setSize(300, 30);
     title.setLocation(200, 30);
     c.add(title);

     name = new JLabel("Title");
     name.setFont(new Font("Arial", Font.PLAIN, 20));
     name.setSize(100, 20);
     name.setLocation(100, 100);
     c.add(name);

     tname = new JTextField();
     tname.setFont(new Font("Arial", Font.PLAIN, 15));
     tname.setSize(190, 20);
     tname.setLocation(200, 100);
     c.add(tname);
     
     add = new JLabel("Notes");
     add.setFont(new Font("Arial", Font.PLAIN, 20));
     add.setSize(100, 20);
     add.setLocation(100, 150);
     c.add(add);

     tadd = new JTextArea();
     tadd.setFont(new Font("Arial", Font.PLAIN, 15));
     tadd.setSize(200, 75);
     tadd.setLocation(200, 150);
     tadd.setLineWrap(true);
     c.add(tadd);
     
     tType = new JLabel("Type");
     tType.setFont(new Font("Arial", Font.PLAIN, 20));
     tType.setSize(100, 25);
     tType.setLocation(100, 250);
     c.add(tType);
     
     aType = new JComboBox(type);
     aType.setFont(new Font("Arial", Font.PLAIN, 15));
     aType.setSize(150, 20);
     aType.setLocation(200, 250);
     c.add(aType);
     
     tPriority = new JLabel("Priority");
     tPriority.setFont(new Font("Arial", Font.PLAIN, 20));
     tPriority.setSize(100, 25);
     tPriority.setLocation(100, 300);
     c.add(tPriority);
     
     aPriority = new JComboBox(priority);
     aPriority.setFont(new Font("Arial", Font.PLAIN, 15));
     aPriority.setSize(150, 20);
     aPriority.setLocation(200, 300);
     c.add(aPriority);
     
     tStatus = new JLabel("Status");
     tStatus.setFont(new Font("Arial", Font.PLAIN, 20));
     tStatus.setSize(100, 25);
     tStatus.setLocation(100, 350);
     c.add(tStatus);
     
     aStatus = new JComboBox(status);
     aStatus.setFont(new Font("Arial", Font.PLAIN, 15));
     aStatus.setSize(150, 20);
     aStatus.setLocation(200, 350);
     c.add(aStatus);

     dob = new JLabel("Due date");
     dob.setFont(new Font("Arial", Font.PLAIN, 20));
     dob.setSize(100, 20);
     dob.setLocation(100, 400);
     c.add(dob);

     month = new JComboBox(months);
     month.setFont(new Font("Arial", Font.PLAIN, 15));
     month.setSize(85, 20);
     month.setLocation(200, 400);
     month.setSelectedIndex(findCurrentMonthIndex());
     c.add(month);
     
     date = new JComboBox(dates);
     date.setFont(new Font("Arial", Font.PLAIN, 15));
     date.setSize(75, 20);
     date.setLocation(280, 400);
     date.setSelectedIndex(findCurrentDateIndex());
     c.add(date);

     year = new JComboBox(years);
     year.setFont(new Font("Arial", Font.PLAIN, 15));
     year.setSize(85, 20);
     year.setLocation(355, 400);
     year.setSelectedIndex(findCurrentYearIndex());
     c.add(year);

     sub = new JButton("Add");
     sub.setFont(new Font("Arial", Font.PLAIN, 15));
     sub.setSize(100, 20);
     sub.setLocation(150, 500);
     sub.addActionListener(this);
     c.add(sub);

     reset = new JButton("Reset");
     reset.setFont(new Font("Arial", Font.PLAIN, 15));
     reset.setSize(100, 20);
     reset.setLocation(270, 500);
     reset.addActionListener(this);
     c.add(reset);

     setVisible(true);
     
     findCurrentMonthIndex();
 } 

 // method actionPerformed()
 // to get the action performed
 // by the user and act accordingly
 public void actionPerformed(ActionEvent e)
 {
	 // TODO: this needs to change based on who the user is
	 String user = "alice";
	 String file = "files/" + user + "/tasks.txt";
     if (e.getSource() == sub) {
    	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));) {
    		 writer.newLine();
    		 writer.write("----------");
    		 writer.newLine();
    		 writer.write("Created at: " + LocalDateTime.now().toString());
    		 writer.newLine();
    		 writer.write("Title: " + tname.getText());
    		 writer.newLine();
    		 writer.write("Notes: " + tadd.getText());
    		 writer.newLine();
    		 writer.write("Type: " + aType.getSelectedItem().toString());
    		 writer.newLine();
    		 writer.write("Priority: " + aPriority.getSelectedItem().toString());
    		 writer.newLine(); 
    		 writer.write("Status: " + aStatus.getSelectedItem().toString());
    		 writer.newLine();
    		 // write to format "YYYY-MM-DD"
    		 String dateString = createDateString(year.getSelectedItem().toString(), month.getSelectedItem().toString(), date.getSelectedItem().toString());
    		 writer.write("Due date: " + dateString);
    		 writer.newLine(); 
    		 writer.close();
    		 
    		 NavBar.reFreshCardPanel();
    	 } catch (IOException err) {
    		 System.out.println("Whoops, sad day, we got an error :(");
    		 err.printStackTrace();
    	 }
    	 resetForm();
     }

     else if (e.getSource() == reset) {
    	 resetForm();
     }
 }
 
 public void resetForm() {
     String def = "";
     tname.setText(def);
     tadd.setText(def);
     date.setSelectedIndex(findCurrentDateIndex());
     month.setSelectedIndex(findCurrentMonthIndex());
     year.setSelectedIndex(findCurrentYearIndex());
     aType.setSelectedIndex(0);
     aPriority.setSelectedIndex(0);
     aStatus.setSelectedIndex(0);
 }
 
 public int findCurrentMonthIndex() {
	 int monthNum = LocalDate.now().getMonthValue();
	 return monthNum - 1;
 }
 
 public int findCurrentDateIndex() {
	 int dateNum = LocalDate.now().getDayOfMonth();
	 return dateNum - 1;
 }
 
 public int findCurrentYearIndex() {
	 int dateNum = LocalDate.now().getYear();
	 int buffer = dateNum - 2026;
	 // years only account for before 2037
	 if (dateNum > 2037) {
		 return 0;
	 }
	 return buffer;
 }
 
	public String createDateString(String year, String month, String day) {
		// when the user selects the month, it's in Text (i.e. Jan, Feb)
		// and it needs to be converted into the numerical equivalent 
		// To do this, search through the months array to find what index it is 
		
		// Add 1 to the index to account for starting at 0 
		int index = Arrays.asList(months).indexOf(month) + 1;
		// if it's 10 or under, a "0" needs to be added before it to 
		// have the correct formatting (i.e. 01, 02)
		String correctMonthFormat;
		if (index < 10) {
			correctMonthFormat = "0" + String.valueOf(index);
		} else {
			correctMonthFormat = String.valueOf(index);
		}
		
		// if the String day is less than 2, then it means 
		// it's a single digit, and "0" needs to be added to the String 
		// in order to have the correct formatting (i.e. 01, 02)
		if (day.length() < 2) {
			day = "0" + day;
		} 
				
		// format needed is "YYYY-MM-DD"
		String dateString = year + "-" + correctMonthFormat + "-" + day;
		
		return dateString;
	}
 
}
