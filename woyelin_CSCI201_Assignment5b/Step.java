package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;
import java.util.Vector;

public class Step implements Serializable {
	
	static final long serialVersionUID = 3L;
	
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
