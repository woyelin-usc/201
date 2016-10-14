package woyelin_CSCI201_Assignment5a;

import java.util.concurrent.Semaphore;

public class Workbench extends WorkSpace {
	
	static Semaphore semaphore = new Semaphore(3);
	
	public Workbench(int x, int y) {
		super(x,y);
	}
	
}
