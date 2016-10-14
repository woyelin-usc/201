package woyelin_CSCI201_Assignment4;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Car implements Runnable {
	private String color;
	private int ai;
	private double speed;
	private int col;
	private char row;
	private boolean visible;
	private int index;
	
	private int previousDir;
	
	private JTable table;

	// this variable is used for AI 1 to store the previous tile car just
	// visited
	private int previousTileIndex;

	// this array is usd for AI 4 to store the order of direction a car visited
	// in the past
	// 0: initial 1: north 2: south 3:west 4: east
	// dir[0] is the one you visited most recent
	// dir[3] is the direction you visited in the longest
	private int dir[] = { 0, 0, 0, 0 };

	// this boolean array is used to store whether a car (AI 2) has chose this
	// direction in the past
	// previous[0] = up, previous[1] = down, previous[2] = left, previous[3] = right
	private boolean previous[] = new boolean[4];
	
	
	// store the east and west most col for the whole map for AI 3
	private int eastMostCol, westMostCol;
	// store the expected direction for AI 3 car
	// up: 1 down: 2 west 3  east 4
	// initial: east 4
	private int expectDir = 4;

	private ArrayList<Tile> tileArrayList;

	// This method get the east and west mot column index
	public void setEastWestMostCol() {
		eastMostCol = 0;
		westMostCol = 10;
		for(int i=1;i< tileArrayList.size();i++) {
			
			String type = tileArrayList.get(i).getType();
			if(type.equalsIgnoreCase("blank"))
				continue;
			int currCol = tileArrayList.get(i).getCol();
			if(currCol > eastMostCol)
				eastMostCol = currCol;
			if(currCol < westMostCol )
				westMostCol = currCol;
		}
	}
	
	
	public Car(String color, int ai, double speed, int col, char row,
			int index, ArrayList<Tile> tileArrayList, JTable table) {
		this.color = color;
		this.ai = ai;
		this.speed = speed;
		this.col = col;
		this.row = row;
		this.visible = true;
		this.index = index;
		this.tileArrayList = tileArrayList;
		setEastWestMostCol();
		this.table = table;
		for(int i=0;i<4;i++)
			previous[i] = false;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setAi(int ai) {
		this.ai = ai;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setRow(char row) {
		this.row = row;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getColor() {
		return color;
	}

	public int getAi() {
		return ai;
	}

	public double speed() {
		return speed;
	}

	public int getCol() {
		return col;
	}

	public char getRow() {
		return row;
	}

	public boolean getVisible() {
		return visible;
	}

	public int getIndex() {
		return index;
	}

	public int getCurrentTileIndex() {
		// row col: both from 0 - 8 NOT 1 to 9
		int row = getRow() - 65;
		int col = getCol() - 1;
		int index = 9 * row + col;
		return index;
	}
	
	// Given a previous direction, get the next -90 direction
	public int getNextDir(int previousDir) {
		switch(previousDir) {
		case 1:
			return 4;
		case 2:
			return 3;
		case 3:
			return 1;
		default:
			return 2;
		}
	}

	public void moveType4(int direction) {
		if (direction == 1)
			row -= 1;
		else if (direction == 2)
			row += 1;
		else if (direction == 3)
			col -= 1;
		else
			col += 1;
	}
	
	// this method is to check whether given direction on currTile is valid for AI 4
	public boolean validDirection(int direction, Tile currTile) {
		if (direction == 1)
			return currTile.up();
		if (direction == 2)
			return currTile.down();
		if (direction == 3)
			return currTile.left();
		else
			return currTile.right();
	}
	
	// given a direction, calculate the change of tile index from previous to current tile
	public int change(int direction) {
		if(direction == 1)  
			return -9;
		if(direction == 2)
			return 9;
		if(direction == 3)
			return -1;
		else
			return 1;
	}
	
	public void type1(int count) {

		// before car moves, car is in currTile
		int currTileIndex = getCurrentTileIndex();
		Tile currTile = tileArrayList.get(currTileIndex);

		// First move of AI1, always attempts to move left
		// if left is not valid, move in next direction clockwise
		if (count == 5) {

			// if left is valid, go left
			if (currTile.left()) {
				col -= 1;
				previousDir = 3;
			}
			// if left is not valid, go up
			else if (currTile.up()) {
				row -= 1;
				previousDir = 1;
			}
			// if up is not valid, go right
			else if (currTile.right()) {
				col += 1;
				previousDir = 4;
			}
			// if right is not valid, go down
			else {
				row += 1;
				previousDir = 2;
			}

			previousTileIndex = currTileIndex;
			return;
		}

		// Non-first move, always go right
		// if right is not valid, go down; else, go left; else, go up
		else {

			while (true) {
				
				int expectNextDir = previousDir;
				
				for (int i = 0; i < 4; i++) {
					expectNextDir = getNextDir(expectNextDir);

					if (validDirection(expectNextDir, currTile)) {
						// check whether go back to previous tile
						if (previousTileIndex != getCurrentTileIndex()+ change(expectNextDir)) {
							previousTileIndex = getCurrentTileIndex();
							moveType4(expectNextDir);
							previousDir = expectNextDir;
							return;
						}
					}
				}
			}
		}
	}

	public void type3() {
		
		int currTileIndex = getCurrentTileIndex();
		Tile currTile = tileArrayList.get(currTileIndex);

		// update the current expected direction
		if(getCol() == westMostCol)
			expectDir = 4;
		if(getCol() == eastMostCol)
			expectDir = 3;
		
		// always try to move to expected direction first
		// if expect direction is valid,  move toward expect direction
		if(validDirection(expectDir, currTile)) {
			moveType4(expectDir);
		}
		
		// if current tile CANNOT move toward expected direction
		else {
			while (true) {
				int rand = ((int) (Math.random() * 1000)) % 4 + 1;
				// if random direction is valid, move toward that; 
				// otherwise, regenerate another random direction
				if (validDirection(rand, currTile)) {
					moveType4(rand);
					return;
				}
			}
		}
	}

	// check whether dir[] contains a certain direction represented by number
	public boolean contain(int[] dir, int num) {
		for (int i = 0; i < dir.length; i++)
			if (dir[i] == num)
				return true;
		return false;
	}

	// this method update the order of direction list based on last AI 4 move
	public void updateOrderDirection(int[] dir, int index) {
		for (int i = index; i > 0; i--) {
			int temp = dir[i];
			dir[i] = dir[i - 1];
			dir[i - 1] = temp;
		}
	}

    // get the index of a given value in the array
	public int getIndex(int[] dir, int val) {
		for (int i = 0; i < 4; i++)
			if (dir[i] == val)
				return i;
		return -1;
	}

	public void type4() {

		int currTileIndex = getCurrentTileIndex();
		Tile currTile = tileArrayList.get(currTileIndex);

		// if there are still some directions that hasn't been visited before
		if (contain(dir, 0)) {
			while (true) {
				// generate a number from 1 to 4
				int rand = ((int) (Math.random() * 1000)) % 4 + 1;

				// if this is a valid direction
				if (validDirection(rand, currTile)) {
					// make sure it doesn't appear in the dir[], otherwise,
					// regenerate
					if (!contain(dir, rand)) {
						moveType4(rand);
						dir[3] = rand;
						updateOrderDirection(dir, 3);
						return;
					}
					continue;
				}
			}
		}

		// if all directions been visit, choose a direction that hasn't been visited in the longest
		else {
			int index = 3;
			while (true) {
				int direction = dir[index];
				if (validDirection(direction, currTile)) {
					moveType4(direction);
					updateOrderDirection(dir, index);
					return;
				}
				// if not valid, pick the second longest direction
				index--;
			}
		}
	}

	public boolean allDirectionVisited() {
		for(int i=0;i<4;i++) 
			if(!previous[i])
				return false;
		return true;
	}

	public void cleanAllDirection() {
		for (int i = 0; i < 4; i++)
			previous[i] = false;
	}

	public void type2() {
		int currTileIndex = getCurrentTileIndex();
		Tile currTile = tileArrayList.get(currTileIndex);
		
		// first check whether all directions have been visited
		if(allDirectionVisited()) 
			cleanAllDirection();
		
		while(true) {
			// generate a random number from 0 - 3
			int rand = ((int) (Math.random()*1000) )%4;
			// if the random generated direction hasn't been visited, visit this direction
			if((previous[rand]==false) && validDirection(rand+1, currTile) ) {
				moveType4(rand+1);
				previous[rand] = true;
				return;
			}
		}
	}

	public void move(int count) {
		int type = getAi();
		switch (type) {
		case 1:
			type1(count);
			break;
		case 2:
			type2();
			break;
		case 3:
			type3();
			break;
		case 4:
			type4();
			break;
		}
	}

	// This method is used to update the car's new position based on last move
	public void updateTable() {
		int row = index;
		// first modify the column
		table.setValueAt(getCol(), row, 1);
		// then modify the row
		table.setValueAt(getRow(), row, 2);
	}
	
	@Override
	public void run() {

		int count = 0;

		while (true) {
			// every time count hits 3(first move for AI 1) or 6, we should move
			if (count == 5) {
				move(count);
				count++;
			} else if (count == 11) {
				move(count);
				count = 6;
			} else
				count++;

			try {
				Thread.sleep((long) (1000 / (speed * 3)));
				updateTable();
				visible = !visible;
				Simulator.drawPanel.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
