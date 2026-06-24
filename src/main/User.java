package main;

public class User {
	private String username;
	private String password;

	public User() {
		// default constructor, in case it's needed elsewhere
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

/* 
User specs from design doc

user_id, UUID, Foreign key to User
widget_layout, JSON, Ordered array of { widget_type, module, config }
default_view, Enum, Home Page default view on load
nav_collapsed, Boolean, Sidebar collapsed state
theme, Enum, light | dark | system
*/