package woyelin_CSCI201_Assignment5b;

import java.util.concurrent.Semaphore;

public class TableSaw extends WorkSpace{
	
	static Semaphore semaphore = new Semaphore(3);
	
	public TableSaw(int x, int y) {
		super(x,y);
	}
	
}
