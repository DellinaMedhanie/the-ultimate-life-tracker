package main;

public class Main {
	public static void main(String[] args) {
		System.out.println("Welcome to the Life Management System");

		// show the sign-in screen first; NavBar (the main app) only opens
		// once someone successfully signs in, from inside LoginFrame
		javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame());
	}
}
