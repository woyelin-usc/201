package woyelin_CSCI201_Assignment5a;

public class Task {
	
	private String name;
	String status;
	
	public Task(String name) {
		this.name = name;
		status = "Not Built";
	}
	
	public String getName() {
		return this.name;
	}
}
