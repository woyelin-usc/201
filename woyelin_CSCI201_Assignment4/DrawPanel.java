package woyelin_CSCI201_Assignment4;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawPanel extends JPanel implements Runnable {		
	
	private ArrayList<Tile> tileArrayList;
	private ArrayList<Car> carArrayList;
	
	public DrawPanel(ArrayList<Tile> tileArrayList, ArrayList<Car> carArrayList) {
		this.tileArrayList = tileArrayList;
		this.carArrayList = carArrayList;
	}
	
	// This method fill all grid in green color
	private void fillGrid(Graphics g) {
		g.setColor(Color.GREEN.darker());
		int startX = (getWidth() - 450) / 2;
		int startY = (getHeight() - 450) / 2;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				g.fillRect(startX + i * 50, startY + j * 50, 50, 50);
	}

	// draw "i" on tiles (tile top-left startX, startY)
	private void drawI(int col, char row, int degree, Graphics2D g2d) {

		// get the top left of first grid
		int startX0 = (getWidth() - 450) / 2;
		int startY0 = (getHeight() - 450) / 2;

		// get the top left of CURRENT grid
		int startX = startX0 + (col - 1) * 50;
		int startY = startY0 + (row - 65) * 50;

		g2d = (Graphics2D) g2d.create();
		g2d.rotate(Math.toRadians(degree), startX + 25, startY + 25);
		g2d.fillRect(startX + 20, startY, 10, 50);
	}

	// draw "+" on tiles (tile top-left corner: startX, startY)
	private void drawPlus(int col, char row, Graphics2D g2d) {

		// get the top left of first grid
		int startX0 = (getWidth() - 450) / 2;
		int startY0 = (getHeight() - 450) / 2;

		// get the top left of CURRENT grid
		int startX = startX0 + (col - 1) * 50;
		int startY = startY0 + (row - 65) * 50;

		g2d.fillRect(startX, startY + 20, 50, 10);
		g2d.fillRect(startX + 20, startY, 10, 50);
	}

	// draw "L" on tiles (tile top-left corner: startX, startY)
	private void drawL(int col, char row, int degree, Graphics2D g2d) {

		// get the top left of first grid
		int startX0 = (getWidth() - 450) / 2;
		int startY0 = (getHeight() - 450) / 2;

		// get the top left of CURRENT grid
		int startX = startX0 + (col - 1) * 50;
		int startY = startY0 + (row - 65) * 50;

		g2d = (Graphics2D) g2d.create();
		g2d.rotate(Math.toRadians(-degree), startX + 25, startY + 25);
		g2d.fillRect(startX + 20, startY, 10, 30);
		g2d.fillRect(startX + 20, startY + 20, 30, 10);
	}

	// draw "T" on tiles (tile top-left corner: startX ,startY)
	private void drawT(int col, char row, int degree, Graphics2D g2d) {

		// get the top left of first grid
		int startX0 = (getWidth() - 450) / 2;
		int startY0 = (getHeight() - 450) / 2;

		// get the top left of CURRENT grid
		int startX = startX0 + (col - 1) * 50;
		int startY = startY0 + (row - 65) * 50;

		g2d = (Graphics2D) g2d.create();
		g2d.rotate(Math.toRadians(-degree), startX + 25, startY + 25);
		g2d.fillRect(startX, startY + 20, 50, 10);
		g2d.fillRect(startX + 20, startY + 20, 10, 30);
	}

	// This method parses road
	private void drawTile(ArrayList<Tile> tileArrayList, Graphics g) {

		// get the top left of first grid
		int startX0 = (getWidth() - 450) / 2;
		int startY0 = (getHeight() - 450) / 2;

		for (int i = 0; i < tileArrayList.size(); i++) {
			// get the top left of CURRENT grid
			int startX = startX0 + (tileArrayList.get(i).getCol() - 1) * 50;
			int startY = startY0 + (tileArrayList.get(i).getRow() - 65)
					* 50;
			// get degree and type of road in this tile
			int degree = tileArrayList.get(i).getDegree();
			String type = tileArrayList.get(i).getType();

			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);

			if (type.equalsIgnoreCase("i"))
				drawI(tileArrayList.get(i).getCol(), tileArrayList.get(i)
						.getRow(), degree, g2d);

			else if (type.equalsIgnoreCase("l"))
				drawL(tileArrayList.get(i).getCol(), tileArrayList.get(i)
						.getRow(), degree, g2d);

			else if (type.equalsIgnoreCase("t"))
				drawT(tileArrayList.get(i).getCol(), tileArrayList.get(i)
						.getRow(), degree, g2d);

			else if (type.equalsIgnoreCase("+"))
				drawPlus(tileArrayList.get(i).getCol(), tileArrayList
						.get(i).getRow(), g2d);
		}

	}

	// this method converts a string color to real color instance
	private Color getColor(String color) {
		if (color.equalsIgnoreCase("black"))
			return Color.black;
		else if (color.equalsIgnoreCase("blue"))
			return Color.blue;
		else if (color.equalsIgnoreCase("cyan"))
			return Color.cyan;
		else if (color.equalsIgnoreCase("darkGray")
				|| color.equalsIgnoreCase("DARK_GRAY"))
			return Color.darkGray;
		else if (color.equalsIgnoreCase("gray"))
			return Color.gray;
		else if (color.equalsIgnoreCase("green"))
			return Color.green;
		else if (color.equalsIgnoreCase("lightGray")
				|| color.equalsIgnoreCase("LIGHT_GRAY"))
			return Color.lightGray;
		else if (color.equalsIgnoreCase("magenta"))
			return Color.magenta;
		else if (color.equalsIgnoreCase("orange"))
			return Color.orange;
		else if (color.equalsIgnoreCase("pink"))
			return Color.pink;
		else if (color.equalsIgnoreCase("red"))
			return Color.red;
		else if (color.equalsIgnoreCase("white"))
			return Color.white;
		else
			return Color.yellow;

	}

	// This method draws a car at location given in the parameters (locX,
	// locY)
	private void drawThisCar(Car car, Graphics g) {

		int tileX = (getWidth() - 450) / 2 + (car.getCol() - 1) * 50;
		int tileY = (getHeight() - 450) / 2 + (car.getRow() - 65) * 50;

		// draw circle as car
		g = (Graphics) g.create();
		g.setColor(getColor(car.getColor()));
		g.fillOval(tileX + 10, tileY + 10, 30, 30);

		// draw number of that car
		g.setColor(Color.black);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString( ((Integer)(car.getIndex()+1)).toString(), tileX + 20, tileY + 32);

	}

	// This method draws a car at location given in the parameters (locX, locY)
	private void drawCar(ArrayList<Car> carArrayList, Graphics g) {

		for (int i = 0; i < carArrayList.size(); i++) {
			Car car = carArrayList.get(i);
			// get top-left point of tile in which the car locates in
			int tileX = (getWidth() - 450) / 2 + (car.getCol() - 1) * 50;
			int tileY = (getHeight() - 450) / 2 + (car.getRow() - 65) * 50;
			
			// if car is invisible, paint it to make it visible
			if (car.getVisible()) {

				drawThisCar(car, g);
			}
		}
	}

	// This method draw the black grid outline
	private void drawOutLine(Graphics g, int startX, int startY) {
		g.setColor(Color.BLACK);
		// draw all grid
		g.setColor(Color.BLACK);
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				g.drawRect(startX + i * 50, startY + j * 50, 50, 50);

		// draw the letters from 'A' to 'I'
		for (int i = 0; i < 9; i++)
			g.drawString(Character.toString(((char) (65 + i))),
					startX - 25, (startY + 25) + i * 50);

		// draw the numbers from 1 - 9
		for (int i = 0; i < 9; i++)
			g.drawString(Integer.toString(i + 1), (startX + 25) + i * 50,
					startY - 15);
	}

	public void paintComponent(Graphics g) {

		super.paintComponents(g);
		
		int width = this.getWidth();
		int height = this.getHeight();

		// Find the start point
		int startX = (width - 450) / 2;
		int startY = (height - 450) / 2;

		// fill grid background to dark-green
		fillGrid(g);

		// draw the black road on each tile
		drawTile(tileArrayList, g);

		// draw car on its location
		drawCar(carArrayList, g);

		// Finally draw the border of each tile
		drawOutLine(g, startX, startY);
	}

	@Override
	public void run() {
		
	}

}
