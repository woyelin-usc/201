package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;

public class WorkSpace implements Serializable {
	
	String name; 
	String status;
	int x, y;
	boolean isWorking = false;
	int time = -1;
	
	boolean inUse = false;
	
	public WorkSpace(int x2, int y2) {
		this.x = x;
		this.y = y;
		this.status = "Open";
	}

}
