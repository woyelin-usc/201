package woyelin_CSCI201_Assignment5b;

import java.util.concurrent.Semaphore;

public class PaintingStation extends WorkSpace{
	
	static Semaphore semaphore = new Semaphore(4);
	public PaintingStation(int x, int y) {
		super(x,y);
	}

}
