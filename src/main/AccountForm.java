//All files are housed under the package "main" which allows them to see and interact with one another...
//Allows for the integration of this file with the User.java file...
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
import java.io.FileWriter;
import java.io.IOException;

//Creation of an Account form class.  Every user account will be an object within this class...
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
		title = new JLabel("Create an account 👤");
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
		//KEEP ANNOTATING CODE!
		if (e.getSource() == sub) {
			String username = tname.getText();
			String password = new String(tpass.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
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

			JOptionPane.showMessageDialog(this, "Account created for " + username + ".");
			resetForm();
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
