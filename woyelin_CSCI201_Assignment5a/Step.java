package woyelin_CSCI201_Assignment5a;

import java.util.Vector;

public class Step {
	
	Vector<Tool> toolVt;
	int duration;
	
	// Anvil, Workbench, PaintingStation, TableSaw, Furnace, Press
	String location;
	
	public Step(Vector<Tool> toolVt, int duration, String location) {
		this.toolVt = toolVt;
		this.duration = duration;
		this.location = location;
	}

}
