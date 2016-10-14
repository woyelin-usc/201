package woyelin_CSCI201_Assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Timer;

public class Simulator extends JFrame {

	private JMenuBar mb;
	private JMenuItem mi;

	private JTable table;
	private JScrollPane tableScrollPane;

	private String filePath;

	private JPanel wholeDrawPanel, tablePanel;
	static DrawPanel drawPanel;

	private ArrayList<Car> carArrayList;
	ArrayList<Tile> tileArrayList;

	// This method parse road
	private void parseTile(ArrayList<Tile> tileArrayList, NodeList tileList) {

		for (int i = 0; i < tileList.getLength(); i++) {
			Node n = tileList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				int col = Integer.parseInt(e.getAttribute("column"));
				char row = (char) (i / 9 + 65);
				String type = e.getAttribute("type");
				int degree = Integer.parseInt(e.getAttribute("degree"));

				Tile newTile = new Tile(col, row, type, degree);
				// add newTile to tileArrayList
				tileArrayList.add(newTile);
			}
		}
	}

	// This method only parses car
	private void parseCar(ArrayList<Car> carArrayList, NodeList carList) {

		for (int i = 0; i < carList.getLength(); i++) {
			Node n = carList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				String color = e.getAttribute("color");
				int ai = Integer.parseInt(e.getAttribute("ai"));
				double speed = Double.parseDouble(e.getAttribute("speed"));
				// child refers to this car's location
				NodeList child = e.getChildNodes();
				for (int j = 0; j < child.getLength(); j++) {
					Node location = child.item(j);
					if (location.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) location;
						int col = Integer.parseInt(ele.getAttribute("x"));
						char row = ele.getAttribute("y").charAt(0);
						Car newCar = new Car(color, ai, speed, col, row, i, tileArrayList, table);
						Thread t = new Thread(newCar);
						t.start();
						// for each car, create a row
						DefaultTableModel model = (DefaultTableModel)table.getModel();
						model.addRow(new Object[] {i+1,newCar.getCol(), newCar.getRow()});
						// add new car to carArrayList
						carArrayList.add(newCar);
						Thread tt = new Thread(newCar);
					}
				}
			}
		}
	}

	// This method configure menu bar and add it to the top
	private void configureMenuBar() {
		mb = new JMenuBar();
		mi = new JMenuItem("Open File...");
		mb.add(mi);
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open");
				int returnVal = fc.showDialog(Simulator.this, "Open");

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filePath = fc.getSelectedFile().toString();

					try {
						// begin to parse using dom
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document doc = builder.parse(new File(filePath));
						doc.getDocumentElement().normalize();

						// first parse tile:
						// tileList contains every node named "tile"
						tileArrayList = new ArrayList<Tile>();
						NodeList tileList = doc.getElementsByTagName("tile");
						parseTile(tileArrayList, tileList);

						// then parse car:
						// carList contains every car
						carArrayList = new ArrayList<Car>();
						NodeList carList = doc.getElementsByTagName("car");
						parseCar(carArrayList, carList);

					} catch (SAXException | IOException| ParserConfigurationException ee) {
						ee.printStackTrace();
					}

					
					if (drawPanel != null) 
						 wholeDrawPanel.remove(drawPanel);
						 wholeDrawPanel.updateUI();
						 drawPanel = new DrawPanel(tileArrayList,carArrayList);
						 drawPanel.setBackground(Color.white);
						 wholeDrawPanel.add(drawPanel, BorderLayout.CENTER);
					
				}
			}
		});
		setJMenuBar(mb);
	}

	// This method is to create and add the JTable
	private void configureTable() {
		Object[] columnNames = { "Car#", "X", "Y" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable();
		table.setModel(tableModel);
		table.setBackground(Color.LIGHT_GRAY);
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(200, 600));
		wholeDrawPanel.add(tableScrollPane, BorderLayout.EAST);
	}

	// This method is used to configure draw area: (JTable + drawPanel)
	private void configureDrawArea() {
		
		wholeDrawPanel = new JPanel();
		wholeDrawPanel.setLayout(new BorderLayout());

//		// add into it the area into which we draw
//		drawPanel = new DrawPanel(tileArrayList, carArrayList);
//		drawPanel.setBackground(Color.white);
//		wholeDrawPanel.add(drawPanel, BorderLayout.CENTER);

		add(wholeDrawPanel, BorderLayout.CENTER);

	}

	public Simulator() {
		super("Roadway Simulator");
		setSize(800, 600);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		configureDrawArea();
		configureTable();
		configureMenuBar();

		setMinimumSize(new Dimension(740, 580));
		setVisible(true);
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Simulator my = new Simulator();
//		while(true) {
//			my.repaint();
//		}
	}
}