package woyelin_CSCI201_Assignment5a;

import java.util.Vector;

public class Recipe {
	
	Material material;
	Vector<Step> stepVt;
	String name;
	
	public Recipe(String name,Material material, Vector<Step> stepVt) {
		this.material = material;
		this.stepVt = stepVt;
		this.name = name;
	}
}
