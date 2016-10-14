package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;
import java.util.Vector;

public class Recipe implements Serializable {
	
	static final long serialVersionUID = 4L;
	
	Material material;
	Vector<Step> stepVt;
	String name;
	int cost;
	int totalTime;
	
//	String status;
//	OrderThread orderThread;
	
	public Recipe(String name, Material material, Vector<Step> stepVt, int cost) {
		this.material = material;
		this.stepVt = stepVt;
		this.name = name;
		this.cost = cost;
//		this.status = "Not Built";

		totalTime = 0;
		for(Step step: stepVt) {
			totalTime += step.duration;
		}
	}
	
//	public void print() {
//		System.out.println("Name: "+this.name);
//		System.out.println("Cost: "+this.cost);
//		for(int i=0;i<stepVt.size();i++) {
//			System.out.print("Use ");
//			for(int j=0;j<stepVt.get(i).toolVt.size();j++) {
//				System.out.print(stepVt.get(i).toolVt.get(j).name+" ");
//			}
//			System.out.println(" at "+stepVt.get(i).location);
//		}
//		System.out.println();
//	}
}
