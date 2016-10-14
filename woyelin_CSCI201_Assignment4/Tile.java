package woyelin_CSCI201_Assignment4;

public class Tile {
	
	private int col;
	private char row;
	private String type;
	private int degree;
	
	public Tile(int col, char row, String type, int degree) {
		this.col = col; this.row = row; this.type = type; this.degree = degree;
	}
	
	public int getCol() {
		return col;
	}
	public char getRow() {
		return row;
	}
	public String getType() {
		return type;
	}
	public int getDegree() {
		return degree;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	public void setRow(char row) {
		this.row = row;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public boolean left() {
		
		if(type.equalsIgnoreCase("+"))
			return true;
		if(type.equalsIgnoreCase("i") && (degree == 90 || degree == 270))
			return true;
		if(type.equalsIgnoreCase("l") && (degree==90 || degree == 180))
			return true;
		if(type.equalsIgnoreCase("t") && (degree==0 || degree == 180 || degree == 270))
			return true;
		
		return false;
	}
	
	public boolean right() {
		if(type.equalsIgnoreCase("+"))
			return true;
		if(type.equalsIgnoreCase("i") && (degree == 90 || degree == 270))
			return true;
		if(type.equalsIgnoreCase("l") && (degree==0 || degree == 270))
			return true;
		if(type.equalsIgnoreCase("t") && (degree==0 || degree == 180 || degree == 90))
			return true;
		return false;
	}
	
	public boolean up() {
		if(type.equalsIgnoreCase("+"))
			return true;
		if(type.equalsIgnoreCase("i") && (degree == 0 || degree == 180))
			return true;
		if(type.equalsIgnoreCase("l") && (degree==0 || degree == 90))
			return true;
		if(type.equalsIgnoreCase("t") && (degree==90 || degree == 180 || degree == 270))
			return true;
		return false;
	}
	
	public boolean down() {
		if(type.equalsIgnoreCase("+"))
			return true;
		if(type.equalsIgnoreCase("i") && (degree == 0 || degree == 180))
			return true;
		if(type.equalsIgnoreCase("l") && (degree==180|| degree == 270))
			return true;
		if(type.equalsIgnoreCase("t") && (degree==0 || degree == 90 || degree == 270))
			return true;
		return false;
	}
}
