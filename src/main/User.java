package main;

public class User {
	private int userId; 

	
	User() {
		// when creating a new user, automatically generate id
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