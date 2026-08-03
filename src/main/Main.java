package main;

public class Main {
	// keep track of the login Frame
	// this is necessary for proper logout/login functionality 
	// to make sure there is no stale data/frames 
	private static LoginFrame activeLoginFrame;
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Life Management System");

		// show the sign-in screen first; NavBar (the main app) only opens
		// once someone successfully signs in, from inside LoginFrame
		
		javax.swing.SwingUtilities.invokeLater(() -> {
            // If a login frame is already open somewhere, close it first
			// this would happen if it's not the first time the program has 
			// been run and someone has logged out
			// this is to prevent stale data/frames 
            if (activeLoginFrame != null) {
                activeLoginFrame.dispose();
            }

            // Create a brand NEW instance of the login frame to 
            // initiate a fresh program and get fresh data
            // This is necessary to be able to save CurrentUser.getusername() 
            // as a variable instead of needing to call the function 
            // every time you want to fetch the current username data 
            activeLoginFrame = new LoginFrame();
            
            // 3. FORCE it to be visible (critical for new instances)
            activeLoginFrame.setVisible(true); 
        });
	}
}
