package woyelin_CSCI201_Assignment5b;

import java.util.concurrent.Semaphore;

public class Press extends WorkSpace {

	static Semaphore semaphore = new Semaphore(1);
	
	public Press (int x, int y) {
		super(x,y);
	}
	
}
