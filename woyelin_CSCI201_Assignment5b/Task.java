package woyelin_CSCI201_Assignment5b;

public class Task {
	
	Recipe recipe;
	OrderThread orderThread;
	String status;
	
	public Task(Recipe recipe, OrderThread orderThread) {
		this.recipe = recipe;
		this.orderThread = orderThread;
		status = "Not Built";
	}
}
