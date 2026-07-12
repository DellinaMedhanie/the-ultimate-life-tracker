package main;

// Model layer 
public class Category {
	// getters/setters, equals(), toString()
	String name; 
	Type type;
	
	public enum Type {
		INCOME, 
		EXPENSE, 
		UNKNOWN
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type newType) {
		type = newType;
	}
	
	@Override 
	public String toString() {
		return "Category name: " +  name + ", Category type: " + type.toString();
	}
}
