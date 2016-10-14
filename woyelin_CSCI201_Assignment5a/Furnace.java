package woyelin_CSCI201_Assignment5a;

import java.util.concurrent.Semaphore;

public class Furnace extends WorkSpace {
	
	static Semaphore semaphore = new Semaphore(2);
	
	public Furnace(int x, int y) {
		super(x,y);
	}

}
