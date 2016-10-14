package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

public class Workbench extends WorkSpace implements Serializable {
	
	static Semaphore semaphore = new Semaphore(3);
	
	public Workbench(int x, int y) {
		super(x,y);
	}
	
}
