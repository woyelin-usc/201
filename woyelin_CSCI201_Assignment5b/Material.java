package woyelin_CSCI201_Assignment5b;

import java.io.Serializable;

public class Material implements Serializable{
	
	static final long serialVersionUID = 1L;
	
	
//	private String name;
	 int woodNum, metalNum, plasticNum;
	
	public Material(/*String name,*/ int woodNum, int metalNum, int plasticNum) {
//		this.name = name;
		this.woodNum = woodNum;
		this.metalNum = metalNum;
		this.plasticNum = plasticNum;
	}
	
	public void setWoodNum(int num) {
		this.woodNum = num;
	}
	
	public void setMetalNum(int num) {
		this.metalNum = num;
	}
	
	public void setPlasticNum(int num) {
		this.plasticNum = num;
	}
	
	public int getWoodNum() {
		return woodNum;
	}
	
	public int getMetalNum() {
		return metalNum;
	}
	
	public int getPlasticNum() {
		return plasticNum;
	}
}
