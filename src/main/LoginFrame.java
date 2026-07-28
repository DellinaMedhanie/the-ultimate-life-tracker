//All files are housed under the package "main" which allows them to see and interact with one another...
package main;

//Imports ALL (hence the "*") building blocks available within the selected GUI
import javax.swing.*;
//Layout and positioning tools...
import java.awt.*;
//Reactions (i.e. button clicking, etc.)
import java.awt.event.*;
//Allow for reading/writing files on computer...
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Single-file version of the sign-in feature: the sign-in screen, the
 * sign-up screen, and the file I/O behind both live here together.
 * Only LoginFrame is public (its name has to match the filename); the
 * other three classes below it are package-private, same idea as the
 * merged PomodoroPanel file.
 */

//The sign-in screen users see when the app first launches.
public class LoginFrame extends JFrame implements ActionListener {

	private Container c;
	private JLabel title;
	private JLabel name;
	private JTextField tname;
	private JLabel pass;
	private JPasswordField tpass;
	private JLabel errorLabel;
	private JButton signIn;
	private JButton createAccount;

	public LoginFrame() {
		setTitle("Sign In");
		setBounds(300, 90, 450, 320);
		setResizable(false);
		//unlike AccountForm (a secondary window), closing THIS window should quit
		//the whole app, since nothing else is open yet
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		c = getContentPane();
		c.setLayout(null);

		title = new JLabel("Welcome back \uD83D\uDC4B");
		title.setFont(new Font("Arial", Font.PLAIN, 24));
		title.setSize(350, 30);
		title.setLocation(60, 30);
		c.add(title);

		name = new JLabel("Username");
		name.setFont(new Font("Arial", Font.PLAIN, 18));
		name.setSize(120, 20);
		name.setLocation(60, 95);
		c.add(name);

		tname = new JTextField();
		tname.setFont(new Font("Arial", Font.PLAIN, 15));
		tname.setSize(200, 25);
		tname.setLocation(190, 95);
		c.add(tname);

		pass = new JLabel("Password");
		pass.setFont(new Font("Arial", Font.PLAIN, 18));
		pass.setSize(120, 20);
		pass.setLocation(60, 140);
		c.add(pass);

		tpass = new JPasswordField();
		tpass.setFont(new Font("Arial", Font.PLAIN, 15));
		tpass.setSize(200, 25);
		tpass.setLocation(190, 140);
		c.add(tpass);

		//shows login errors (empty fields, wrong password) without popping up a dialog every time
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		errorLabel.setSize(350, 20);
		errorLabel.setLocation(60, 175);
		c.add(errorLabel);

		signIn = new JButton("Sign In");
		signIn.setFont(new Font("Arial", Font.PLAIN, 15));
		signIn.setSize(100, 25);
		signIn.setLocation(90, 230);
		signIn.addActionListener(this);
		c.add(signIn);

		createAccount = new JButton("Create Account");
		createAccount.setFont(new Font("Arial", Font.PLAIN, 13));
		createAccount.setSize(150, 25);
		createAccount.setLocation(200, 230);
		createAccount.addActionListener(this);
		c.add(createAccount);

		//so hitting Enter in the password field submits the form, same as clicking Sign In
		tpass.addActionListener(this);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == signIn || e.getSource() == tpass) {
			String username = tname.getText().trim();
			String password = new String(tpass.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				errorLabel.setText("Username and password cannot be empty.");
				return;
			}

			if (UserService.authenticate(username, password)) {
				//remember who's logged in so the rest of the app can reference it later
				CurrentUser.setUsername(username);

				//close this window and open the main app
				this.dispose();
				NavBar.main(null);
			} else {
				errorLabel.setText("Incorrect username or password.");
				tpass.setText("");
			}
		} else if (e.getSource() == createAccount) {
			//hand off to the account-creation form
			new AccountForm();
		}
	}
}

// ---------------------------------------------------------------------------
// Creation of an Account form class. Every user account will be an object
// within this class... (package-private, not public, since only one public
// class is allowed per file)
// ---------------------------------------------------------------------------
class AccountForm
//Account form is a JFrame (a ready-made window class that Java/Swing provides — it already knows how to be a window (have a title bar, be resizable, show up on screen, etc.)
 extends JFrame
 //Allows for users clicking button to create an account to actually work (this line delares that I will code this button later on)...
 //Note to self: extends (get an included behavior from JFrame) and implements (promise to add specific behavior yourself)
 implements ActionListener {

	//Private = only AccountForm has access...
	//Visual componets held within the container (container named c for later use/reference)
	private Container c;
	//Box that holds text lable (named title)
	private JLabel title;
	//Box that holds text lable (named name)
	private JLabel name;
	////Box that holds text input able to be edited by users (named tname (i.e. text name))
	private JTextField tname;
	//Hides characters typed into field (for security)
	private JLabel pass;
	private JPasswordField tpass;
	//Two boxes which hold buttons
	private JButton sub;
	private JButton reset;
	//The above is all defined/declared ahead of time here so that I can reference/use them within any method inside of this class without having to declare them again.

	//Constructor method- used for building the window for a new account form for users to interact with using the boxes created above...
	public AccountForm()
	{
		//Window customization...
		setTitle("Create Account");
		//X-position, Y-position, width, height...
		setBounds(300, 90, 450, 300);
		setResizable(false);

		//"c" is the empty box created earlier and we are making a new box for users to interact with in the account form...
		c = getContentPane();
		//Null used for specific customization of the form/box as opposed to automatic...
		c.setLayout(null);

		//Pattern which will be repeated for every lable created...
		//Text user will see
		title = new JLabel("Create an account \uD83D\uDC64");
		//Font type and size
		title.setFont(new Font("Arial", Font.PLAIN, 24));
		//Size
		title.setSize(350, 30);
		//Page location
		title.setLocation(60, 30);
		//Making it appear on the page...
		c.add(title);

		//And again for the username
		name = new JLabel("Username");
		name.setFont(new Font("Arial", Font.PLAIN, 18));
		name.setSize(120, 20);
		name.setLocation(60, 100);
		c.add(name);

		//Nothing inside the paranthesis because this field should be empty and allow users to type within it...
		tname = new JTextField();
		tname.setFont(new Font("Arial", Font.PLAIN, 15));
		tname.setSize(200, 25);
		tname.setLocation(190, 100);
		c.add(tname);

		//New field to set a password
		pass = new JLabel("Password");
		pass.setFont(new Font("Arial", Font.PLAIN, 18));
		pass.setSize(120, 20);
		pass.setLocation(60, 150);
		c.add(pass);

		//User created desired passord
		tpass = new JPasswordField();
		tpass.setFont(new Font("Arial", Font.PLAIN, 15));
		tpass.setSize(200, 25);
		tpass.setLocation(190, 150);
		c.add(tpass);

		//Creating the button users will click to make an account
		sub = new JButton("Create");
		sub.setFont(new Font("Arial", Font.PLAIN, 15));
		sub.setSize(100, 25);
		sub.setLocation(100, 220);
		//Calls addActionListener function from earlier in the code.  "This" refers to the object we are currently inside of...
		sub.addActionListener(this);
		c.add(sub);

		//Creating a reset button 
		reset = new JButton("Reset");
		reset.setFont(new Font("Arial", Font.PLAIN, 15));
		reset.setSize(100, 25);
		reset.setLocation(220, 220);
		reset.addActionListener(this);
		c.add(reset);

		//Makes the above appear on the page
		setVisible(true);
	}

	//Main meat and potatoes of the form.  Must be public because the buttons created need access to it
	//Void because no values are passed back.
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == sub) {
			String username = tname.getText().trim();
			String password = new String(tpass.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
				return;
			}

			if (UserService.userExists(username)) {
				JOptionPane.showMessageDialog(this, "That username is already taken. Please choose another.");
				return;
			}

			User newUser = new User(username, password);

			// create the folder for this user if it doesn't already exist
			File userFolder = new File("files/" + username);
			if (!userFolder.exists()) {
				userFolder.mkdirs();
			}

			String filePath = "files/" + username + "/account.txt";
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
				writer.write("Username: " + newUser.getUsername());
				writer.newLine();
				writer.write("Password: " + newUser.getPassword());
				writer.newLine();
			} catch (IOException err) {
				System.out.println("Error writing account file");
				err.printStackTrace();
			}

			// also register this user in files/users.txt, which is the master list
			// that sign-in checks against — without this step, someone could
			// create an account here but never be able to sign in with it
			UserService.addUser(username, password);

			JOptionPane.showMessageDialog(this, "Account created for " + username + ". You can now sign in.");
			resetForm();

			// close this form and return to the sign-in screen so the new user can log in
			this.dispose();
			new LoginFrame();
		}
		else if (e.getSource() == reset) {
			resetForm();
		}
	}

	public void resetForm() {
		tname.setText("");
		tpass.setText("");
	}

}

// ---------------------------------------------------------------------------
// Service layer for authentication.
// Reads/writes files/users.txt, which is the master list of every registered
// user (format: "userId,userName,password" header, then one row per user).
// ---------------------------------------------------------------------------
class UserService {

	private static final String USERS_FILE = "files/users.txt";

	/**
	 * Reads every registered user out of users.txt.
	 */
	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		File f = new File(USERS_FILE);

		try (Scanner reader = new Scanner(f)) {
			if (reader.hasNextLine()) {
				reader.nextLine(); // skip the header row ("userId,userName,password")
			}
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line.isBlank()) {
					continue;
				}
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					users.add(new User(parts[1].trim(), parts[2].trim()));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("users.txt not found");
		}

		return users;
	}

	/**
	 * Checks a username/password combo against the registered users.
	 * NOTE: passwords are stored in plain text in users.txt, matching how the
	 * rest of this student project stores data (plain text files, no
	 * encryption). Fine for a class project, but worth flagging: this would
	 * NOT be safe in a real production app.
	 */
	public static boolean authenticate(String username, String password) {
		for (User u : getAllUsers()) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Used by the sign-up form to stop someone from registering a username
	 * that's already taken.
	 */
	public static boolean userExists(String username) {
		for (User u : getAllUsers()) {
			if (u.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Registers a new user by rewriting users.txt with the new row appended.
	 * Assigns the new user the next available userId (one higher than the
	 * current highest).
	 */
	public static void addUser(String username, String password) {
		List<String[]> existingRows = new ArrayList<>(); // each entry is [userId, userName, password]
		int nextId = 1;

		File f = new File(USERS_FILE);
		try (Scanner reader = new Scanner(f)) {
			if (reader.hasNextLine()) {
				reader.nextLine(); // skip header
			}
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line.isBlank()) {
					continue;
				}
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					existingRows.add(parts);
					try {
						int id = Integer.parseInt(parts[0].trim());
						if (id >= nextId) {
							nextId = id + 1;
						}
					} catch (NumberFormatException ignored) {
						// if a row's userId isn't a valid number, just skip using it to compute nextId
					}
				}
			}
		} catch (FileNotFoundException e) {
			// users.txt doesn't exist yet; we'll create it fresh below with just a header + this user
		}

		// rewrite the whole file: header, all existing rows, then the new user.
		// this is simpler than trying to append to a possibly-missing trailing newline,
		// and it's a small file so rewriting it entirely is cheap.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, false))) {
			writer.write("userId,userName,password");
			writer.newLine();
			for (String[] row : existingRows) {
				writer.write(row[0].trim() + "," + row[1].trim() + "," + row[2].trim());
				writer.newLine();
			}
			writer.write(nextId + "," + username + "," + password);
			writer.newLine();
		} catch (IOException err) {
			System.out.println("Error writing users.txt");
			err.printStackTrace();
		}
	}
}

// ---------------------------------------------------------------------------
// Holds the username of whoever is currently signed in, so the rest of the
// app can look up "who am I" after login instead of everything being fixed
// to one hardcoded user.
//
// NOTE FOR THE TEAM: right now, most of the app (TaskPanel, TaskForm,
// TaskDetail, TasksAtAGlance, FinancePanel, TransactionForm,
// TransactionService, PomodoroPanel) still has the username "alice"
// hardcoded directly instead of using this class. Sign-in now correctly
// identifies who's logged in and stores it here, but wiring every other
// panel/service to call CurrentUser.getUsername() instead of hardcoding
// "alice" is a separate follow-up task, not done as part of this feature.
// ---------------------------------------------------------------------------
class CurrentUser {

	private static String username;

	public static void setUsername(String name) {
		username = name;
	}

	public static String getUsername() {
		return username;
	}

	public static boolean isLoggedIn() {
		return username != null;
	}
}
