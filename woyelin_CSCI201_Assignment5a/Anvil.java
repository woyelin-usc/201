package woyelin_CSCI201_Assignment5a;

import java.util.concurrent.Semaphore;

public class Anvil extends WorkSpace {
	
	static Semaphore semaphore = new Semaphore(2);
	
	public Anvil(int x, int y) {
		super(x,y);
	}

}
