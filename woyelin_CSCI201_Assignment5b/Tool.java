package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;

public class Tool implements Serializable {
	
	static final long serialVersionUID = 2L;
	
	String name;
	int num;
	
	public Tool(String name, int num) {
		this.name = name;
		this.num = num;
	}

}
