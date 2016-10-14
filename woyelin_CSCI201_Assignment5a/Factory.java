package woyelin_CSCI201_Assignment5a;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Factory extends JFrame {
	
	long startTime = 0;
	long endTime = 0;
	
	private Font stringFont = new Font("Monoco", Font.BOLD, 16);
	
	private JMenuBar menuBar;
	private JMenuItem openItem;
	
	JPanel wholePanel;
	JPanel rightPanel = new JPanel(new BorderLayout());
	private JLabel taskBoardLabel;
	JPanel taskBoardPanel;
	private JScrollPane taskBoardScrollPane;
	
	FactoryPanel factoryPanel;
	
	private ImageIcon woodii = new ImageIcon("./images/wood.png");
	private ImageIcon metalii = new ImageIcon("./images/metal.png");
	private ImageIcon plasticii = new ImageIcon("./images/plastic.png");
	private ImageIcon screwdriverii = new ImageIcon("./images/screwdriver.png");
	private ImageIcon hammerii  = new ImageIcon("./images/hammer.png");
	private ImageIcon paintbrushii  = new ImageIcon("./images/paintbrush.png");
	private ImageIcon plierii  = new ImageIcon("./images/pliers.png");
	private ImageIcon scissorii = new ImageIcon("./images/scissors.png");
	private ImageIcon anvilii = new ImageIcon("./images/anvil.png");
	private ImageIcon workbenchii = new ImageIcon("./images/workbench.png");
	private ImageIcon furnaceii = new ImageIcon("./images/furnace.png");
	private ImageIcon tablesawii = new ImageIcon("./images/tablesaw.png");
	private ImageIcon paintstationii = new ImageIcon("./images/paintingstation.png");
	private ImageIcon pressii = new ImageIcon("./images/press.png");
	private ImageIcon workii = new ImageIcon("./images/worker.png");
	
	private Image woodi = woodii.getImage();
	private Image metali = metalii.getImage();
	private Image plastici = plasticii.getImage();
	private Image screwdriveri = screwdriverii.getImage();
	private Image hammeri = hammerii.getImage();
	private Image paintbrushi = paintbrushii.getImage();
	private Image plieri = plierii.getImage();
	private Image scissori = scissorii.getImage();
	private Image anvili = anvilii.getImage();
	private Image workbenchi = workbenchii.getImage();
	private Image furnacei = furnaceii.getImage();
	private Image tablesawi = tablesawii.getImage();
	private Image paintstationi = paintstationii.getImage();
	private Image pressi = pressii.getImage();
	private Image worki = workii.getImage();

	int woodNum, metalNum, plasticNum ;
	int workerNum, screwdriverNum, hammerNum, paintbrushNum,pliersNum, scissorNum;
	int screwdriverLeft, hammerLeft, paintbrushLeft, pliersLeft, scissorLeft, workerLeft;
	
	File dir;
	File[] rcpFiles, factoryFile;
	
	Vector<Task> taskVt = new Vector<Task>();
	Vector<Worker> workerVt = new Vector<Worker>();
	Vector<Recipe> recipeVt = new Vector<Recipe>();
	
	Vector<Anvil> anvilVt = new Vector<Anvil>();
	Vector<Workbench> workbenchVt = new Vector<Workbench>();
	Vector<Furnace> furnaceVt = new Vector<Furnace>();
	Vector<TableSaw> tablesawVt = new Vector<TableSaw>();
	Vector<PaintingStation> paintingstationVt = new Vector<PaintingStation>();
	Vector<Press> pressVt = new Vector<Press>();
	
	public void parseRcp() {
		
		if(!taskVt.isEmpty())
			taskVt.clear();
		if(!recipeVt.isEmpty())
			recipeVt.clear();
		
		for (File file : rcpFiles) {
			try {
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				String firstLine = br.readLine();
				
				// parse out the name and number
				int start = firstLine.indexOf("[");
				int end = firstLine.indexOf("]");
				String name = firstLine.substring(start+1, end);
				// delete all possible blank spaces inside [ ]
				name = name.replaceAll("\\s*", "");
				int x = firstLine.indexOf("x");
				String numString = firstLine.substring(x+1);
				int num = Integer.parseInt(numString.replaceAll("\\s*", ""));
				
				// create new task 
				for(int i=0;i<num;i++) {
					Task newTaskItem = new Task(name);
					taskVt.add(newTaskItem);
				}
				
				
				// one recipe for a .rcp file
				Recipe recipe;
				// one step vector for a .rcp file
				Vector<Step> stepVt = new Vector<Step>();
				// one whole material for a .rcp file
				int woodNum = 0;
				int plasticNum = 0;
				int metalNum = 0;
				
				String line;
				while((line = br.readLine())!=null) {

					// one duration for a single line
					int duration = 0;
					// one location for a single line
					String location="";
					// one tool vector for a single line
					Vector<Tool> toolVt = new Vector<Tool>();
					
					int left = line.indexOf("[");
					int right= line.indexOf("]");
					
					line = line.substring(left+1, right);
					
					String[] words = line.split(" ");
					
					// here we parse the material part
					if(!(words[0].equalsIgnoreCase("Use"))) {
						words = line.split(":");
						String materialName = words[0].replaceAll("\\s*", "");
						int materialNum = Integer.parseInt(words[1].replaceAll("\\s*", ""));
						if(materialName.equalsIgnoreCase("Wood"))
							woodNum = materialNum;
						else if(materialName.equalsIgnoreCase("Plastic"))
							plasticNum = materialNum;
						else  if(materialName.equalsIgnoreCase("Metal"))
							metalNum = materialNum;
						else;
						// for material line, after we initialize, read next line immediately
						continue;
					}
					
					// here we parse the step part
					else {
						String[] word = line.split(" ");
						int length = word.length;
						for(int i=0;i<length;i++) {
							
							// "for" followed by time
							if(word[i].equalsIgnoreCase("for")) {
								String temp = word[i+1];
								while(temp.length()==0) {
									i++;
									temp = word[i+1];
								}
								int endIndex = word[i+1].indexOf("s");
								duration = Integer.parseInt(temp.substring(0, endIndex));
							}
							
							// "at" followed by location ("Use" may also followed by location)
							else if(word[i].equalsIgnoreCase("at")) {
								String temp = word[i+1];
								while(temp.length()==0) {
									i++;
									temp = word[i+1];
								}
								if(temp.equalsIgnoreCase("Anvil"))
									location = "Anvil";
								else if(temp.equalsIgnoreCase("Workbench"))
									location = "Workbench";
								else if(temp.equalsIgnoreCase("Painting") || temp.equalsIgnoreCase("PaintingStation"))
									location = "PaintingStation";
								else if (temp.equalsIgnoreCase("Saw"))
									location = "TableSaw";
								else if(temp.equalsIgnoreCase("Furnace"))
									location = "Furnace";
								else if(temp.equalsIgnoreCase("Press"))
									location = "Press";
								else;
							}	
							
							// "Use" followed either by location or by a tool
							else if(word[i].equalsIgnoreCase("Use")) {
								// first consider "Use" followed by a location
								String temp = word[i+1];
								while(temp.length()==0) {
									i++;
									temp = word[i+1];
								}
								if(temp.equalsIgnoreCase("Anvil"))
									location = "Anvil";
								else if(temp.equalsIgnoreCase("Workbench"))
									location = "Workbench";
								else if(temp.equalsIgnoreCase("Painting") || temp.equalsIgnoreCase("PaintingStation"))
									location = "PaintingStation";
								else if (temp.equalsIgnoreCase("Saw"))
									location = "TableSaw";
								else if(temp.equalsIgnoreCase("Furnace"))
									location = "Furnace";
								else if(temp.equalsIgnoreCase("Press"))
									location = "Press";
								
								// then consider "Use" followed by a tool
								else {
									// first parse the number of tools
									int toolNum = Integer.parseInt(temp.substring(0,1));
									String toolName = word[i+2];
									Tool newTool = new Tool(toolName, toolNum);
									toolVt.add(newTool);
								}
							}
							
							// "and" followed by other tools
							else if(word[i].equalsIgnoreCase("and")) {
								String temp = word[i+1];
								while(temp.length()==0) {
									i++;
									temp = word[i+1];
								}
								int toolNum = Integer.parseInt(temp.substring(0,1));
								String toolName = word[i+2];
								Tool newTool = new Tool(toolName, toolNum);
								toolVt.add(newTool);
							}
							else;
						}
					}
					
					// create a step instance based on this line's information
					Step step = new Step(toolVt, duration, location);
					// add step instance into stepVt
					stepVt.add(step);
				}
				
				Material material = new Material (name,woodNum, metalNum, plasticNum);
				// Here we have all the steps, so create a whole recipe
				recipe = new Recipe(name,material, stepVt);
				recipeVt.add(recipe);
				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void parseFactory() {
		workerVt = new Vector<Worker>();
		File factory = factoryFile[0];
		try {
			BufferedReader br = new BufferedReader(new FileReader(factory));
			String line;
			while((line = br.readLine()) !=null) {
				int left = line.indexOf("[");
				int right = line.indexOf("]");
				line = line.substring(left+1, right);
				// delete all potential whitespaces
				line = line.replaceAll("\\s*", "");
				String[] split = line.split(":");
				String name = split[0];
				int num = Integer.parseInt(split[1]);
				if (name.equalsIgnoreCase("Workers")|| name.equalsIgnoreCase("Worker")) {
					workerLeft = workerNum = num;
				
					for (int i = 0; i < workerNum; i++) {
						Worker worker = new Worker(this, taskVt, recipeVt, /*taskLabelVt,*/ i);
						workerVt.add(worker);
					}
				}
				else if (name.equalsIgnoreCase("Hammers") || name.equalsIgnoreCase("Hammer"))
					hammerLeft = hammerNum = num;
				else if (name.equalsIgnoreCase("Screwdrivers") || name.equalsIgnoreCase("Screwdriver"))
					screwdriverLeft = screwdriverNum = num;
				else if (name.equalsIgnoreCase("Pliers") || name.equalsIgnoreCase("Plier"))
					pliersLeft = pliersNum = num;
				else if (name.equalsIgnoreCase("Scissors") || name.equalsIgnoreCase("Scissor"))
					scissorLeft = scissorNum = num;
				else if (name.equalsIgnoreCase("Paintbrushes") || name.equalsIgnoreCase("Paintbrush"))
					paintbrushLeft = paintbrushNum = num;
				else;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createTaskBoard() {
		
		Font font = new Font("Monoco", Font.PLAIN, 16);
		
		rightPanel.removeAll();
		rightPanel.updateUI();

		rightPanel = new JPanel(new BorderLayout());
		taskBoardLabel = new JLabel("Task Board");
		taskBoardLabel.setFont(new Font("Monoco", Font.PLAIN, 16));
		taskBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightPanel.add(taskBoardLabel, BorderLayout.NORTH);
		
		taskBoardPanel = new JPanel();
		taskBoardPanel.setLayout(new BoxLayout(taskBoardPanel, BoxLayout.Y_AXIS));
		
		for(Task task: taskVt) {
			String name = task.getName();
			JLabel lbl = new JLabel(name+"..."+task.status);
			lbl.setFont(font);
			taskBoardPanel.add(lbl);
		}
		
		taskBoardPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
		taskBoardPanel.setMinimumSize(new Dimension(200,600));
		
		taskBoardScrollPane = new JScrollPane(taskBoardPanel);
		rightPanel.add(taskBoardScrollPane);
		
		wholePanel.add(rightPanel, BorderLayout.EAST);

	}
	
	public void createMenuBar() {
		menuBar = new JMenuBar();
		openItem = new JMenuItem("Open Folder");
		menuBar.add(openItem);
		setJMenuBar(menuBar);
		
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taskVt.clear();
				workerVt.clear();
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int value = fc.showOpenDialog(Factory.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					
					startTime =  System.currentTimeMillis();
					
					woodNum = plasticNum = metalNum = 1000;
					
					// get selected directory
					dir = fc.getSelectedFile();
					// only get .rcp file
					rcpFiles = dir.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".rcp");
						}
					});
					
					parseRcp();
					
					wholePanel.removeAll();
					wholePanel.updateUI();
					
					createTaskBoard();
					
					// get .factory file
					factoryFile = dir.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".factory");
						}
					});

					parseFactory();

					factoryPanel = new FactoryPanel();
					wholePanel.add(factoryPanel);

				}
			}
		});
	}
	
	public void initializeWorkSpace() {
		// initialize anvil
		Anvil anvil1 = new Anvil(150,150);
		anvilVt.add(anvil1);
		Anvil anvil2 = new Anvil(240,150);
		anvilVt.add(anvil2);
		
		// initialize workbench
		Workbench workbench1 = new Workbench(330,150);
		Workbench workbench2 = new Workbench(420,150);
		Workbench workbench3 = new Workbench(510,150);
		workbenchVt.add(workbench1);
		workbenchVt.add(workbench2);
		workbenchVt.add(workbench3);
		
		// initialize furnaces
		Furnace furnace1 = new Furnace(150,300);
		Furnace furnace2 = new Furnace(240,300);
		furnaceVt.add(furnace1);
		furnaceVt.add(furnace2);
		
		// initialize table saw
		TableSaw tablesaw1 = new TableSaw(330,300);
		TableSaw tablesaw2 = new TableSaw(420,300);
		TableSaw tablesaw3 = new TableSaw(510,300);
		tablesawVt.add(tablesaw1);
		tablesawVt.add(tablesaw2);
		tablesawVt.add(tablesaw3);
		
		// initailize painting station
		PaintingStation station1 = new PaintingStation(150,450);
		PaintingStation station2 = new PaintingStation(240,450);
		PaintingStation station3 = new PaintingStation(330,450);
		PaintingStation station4 = new PaintingStation(420,450);
		paintingstationVt.add(station1);
		paintingstationVt.add(station2);
		paintingstationVt.add(station3);
		paintingstationVt.add(station4);
		
		
		// initialize press
		Press press1 = new Press(510,450);
		pressVt.add(press1);
		
	}
	
	public void initialize() {
		
		woodNum = 1000;
		metalNum = 1000;
		plasticNum = 1000;
	
		initializeWorkSpace();
		
		createMenuBar();
	
		wholePanel = new JPanel(new BorderLayout());
		createTaskBoard();
		add(wholePanel);
	}
	
	public Factory() {
		super("Factory");
		setSize(800,600);
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initialize();
		
		this.setResizable(false);
		setVisible(true);
	}
	
//	public void run() {
//		while(true)
//			repaint();
//	}
	
	public static void main(String[] args) {
		Factory my = new Factory();
	}
	
	class FactoryPanel extends JPanel  {
		
		private int locX = 1, locY = 1;
		private int change = 5;
		
		public FactoryPanel() {
			this.setLayout(new BorderLayout());
		}
		
		public void drawMaterialContainer(Graphics g) {
			g = (Graphics)g.create();
			g.setFont(new Font("Monoco", Font.BOLD, 14 ));
			g.drawString("Wood", 150, 20);
			g.drawString("Metal", 300, 20);
			g.drawString("Plastic", 450, 20);
			g.drawImage(woodi, 150, 25, 50, 50, null);
			g.drawImage(metali, 300, 25, 50, 50, null);
			g.drawImage(plastici, 450, 25, 50, 50, null);
			g.setFont(new Font("Monoco", Font.BOLD, 12));
			g.drawString(""+woodNum, 160, 55);
			g.drawString(""+metalNum, 310, 55);
			g.drawString(""+plasticNum, 460, 55);
		}
		
		public void drawToolShed(Graphics g) {
			g = (Graphics) g.create();
			g.setFont(new Font("Monoco", Font.BOLD, 16));
			
			g.drawString("Screwdriver", 0, 130);
			g.drawImage(screwdriveri, 12,140,50,50,null);
			
			g.drawString("Hammer", 5, 210);
			g.drawImage(hammeri, 12,220,50,50,null);
			
			g.drawString("Paintbrush", 0, 290);
			g.drawImage(paintbrushi, 12,300,50,50,null);
			
			g.drawString("Pliers", 10, 370);
			g.drawImage(plieri, 12,380,50,50,null);
			
			g.drawString("Scissor", 7, 450);
			g.drawImage(scissori, 12,460,50,50,null);
			
			g.setFont(new Font("Monoco", Font.BOLD, 12));
			
			g.drawString(""+screwdriverLeft+"/"+screwdriverNum,21, 170 );
			g.drawString(""+hammerLeft+"/"+hammerNum, 21, 250);
			g.drawString(""+paintbrushLeft+"/"+paintbrushNum, 21, 330);
			g.drawString(""+pliersLeft+"/"+pliersNum, 21, 410);
			g.drawString(""+scissorLeft+"/"+scissorNum, 21, 490);
			
		}
		
		public void drawWorkSpace(Graphics g) {
			g = (Graphics) g.create();
			g.setFont(new Font("Monoco", Font.PLAIN, 18));
			
			g.setColor(Color.green.darker());
			
			// first line: stats + image
			String status = anvilVt.get(0).status;
			if(!status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED );
				g.drawString(status, 160,145);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(status, 150, 145);
			}
			
			status = anvilVt.get(1).status;
			if(!status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(status, 250,145);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(status, 240, 145);
			}
			
			// workbench[0]
			if(!workbenchVt.get(0).status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(workbenchVt.get(0).status, 340,145);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(workbenchVt.get(0).status, 330, 145);
			}
			
			// workbench[1]
			if(!workbenchVt.get(1).status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(workbenchVt.get(1).status, 430,145);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(workbenchVt.get(1).status, 420,145);
			}
			
			if(!workbenchVt.get(2).status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(workbenchVt.get(2).status, 520,145);
			}
			else {
			    g.setColor(Color.green.darker());
			    g.drawString(workbenchVt.get(2).status, 510,145);
			}
			
			g.drawImage(anvili, 150,150,50,50, null);
			g.drawImage(anvili, 240,150,50,50, null);
			g.drawImage(workbenchi, 330,150,50,50, null);
			g.drawImage(workbenchi, 420,150,50,50, null);
			g.drawImage(workbenchi, 510,150,50,50, null);
			
			// second line: status+ image
			
			if(!furnaceVt.get(0).status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(furnaceVt.get(0).status, 160, 295);
			}
			else {
		    	g.setColor(Color.green.darker());
      			g.drawString(furnaceVt.get(0).status, 150, 295);
			}
			
			if(!furnaceVt.get(1).status.equalsIgnoreCase("Open")) {
				g.setColor( Color.RED);
				g.drawString(furnaceVt.get(1).status, 250, 295);
			}
			else {
			    g.setColor(Color.green.darker());
			    g.drawString(furnaceVt.get(1).status, 240, 295);
			}
			
	        if(!tablesawVt.get(0).status.equalsIgnoreCase("Open")) {
	        	g.setColor(Color.RED);
		        g.drawString(tablesawVt.get(0).status, 340,295);
	        }
	        else {
		        g.setColor(Color.green.darker());
		        g.drawString(tablesawVt.get(0).status, 330,295);
	        }
	        
	        if(!tablesawVt.get(1).status.equalsIgnoreCase("Open")) {
	        	g.setColor(Color.RED);
		        g.drawString(tablesawVt.get(1).status, 430,295);
	        }
	        else {
		        g.setColor(Color.green.darker());
		        g.drawString(tablesawVt.get(1).status, 420,295);
	        }
	        
	        if(!tablesawVt.get(2).status.equalsIgnoreCase("Open")) {
	        	g.setColor(Color.RED);
		        g.drawString(tablesawVt.get(2).status, 520,295);
	        }
	        else {
	        	g.setColor(Color.GREEN.darker());
	        	g.drawString(tablesawVt.get(2).status, 510,295);
	        }


			g.drawImage(furnacei, 150,300,50,50, null);
			g.drawImage(furnacei, 240,300,50,50, null);
			g.drawImage(tablesawi, 330,300,50,50, null);
			g.drawImage(tablesawi, 420,300,50,50, null);
			g.drawImage(tablesawi, 510,300,50,50, null);
			
			// third line: status+ image
			if(!paintingstationVt.get(0).status.equalsIgnoreCase("Open")) {
				g.setColor(Color.RED);
				g.drawString(paintingstationVt.get(0).status,160, 440);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(paintingstationVt.get(0).status,150, 440);
			}
			
			if(!paintingstationVt.get(1).status.equalsIgnoreCase("Open")) {
				g.setColor(Color.RED);
				g.drawString(paintingstationVt.get(1).status,250, 440);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(paintingstationVt.get(1).status,240, 440);
			}
			
			if(!paintingstationVt.get(2).status.equalsIgnoreCase("Open")) {
				g.setColor(Color.RED);
				g.drawString(paintingstationVt.get(2).status,340, 440);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(paintingstationVt.get(2).status,330, 440);
			}
			
			if(!paintingstationVt.get(3).status.equalsIgnoreCase("Open")) {
				g.setColor(Color.RED);
				g.drawString(paintingstationVt.get(3).status,430, 440);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(paintingstationVt.get(3).status,420, 440);
			}
			
			if(!pressVt.get(0).status.equalsIgnoreCase("Open")) {
				g.setColor(Color.RED);
				g.drawString(pressVt.get(0).status,520, 440);
			}
			else {
				g.setColor(Color.green.darker());
				g.drawString(pressVt.get(0).status,510, 440);
			}
			
			g.drawImage(paintstationi, 150, 450, 50,50,null);
			g.drawImage(paintstationi, 240, 450, 50,50,null);
			g.drawImage(paintstationi, 330, 450, 50,50,null);
			g.drawImage(paintstationi, 420, 450, 50,50,null);
			g.drawImage(pressi, 510, 450, 50,50, null);
			
			g.setColor(Color.BLACK);
			g.drawString("Anvils", 190, 220);
			g.drawString("Work Benches", 380, 220);
			g.drawString("Furnaces", 180, 370);
			g.drawString("Table saws", 380, 370);
			g.drawString("Painting Stations", 250, 525);
			g.drawString("Press", 510, 525);

		}
		
		public void drawWorker(Graphics g) {

			try {
				g = (Graphics) g.create();
				for (Worker worker : workerVt) {
					if (worker.visible == true)
						g.drawImage(worki, worker.x, worker.y, 50, 50, null);
				}
			} catch (ConcurrentModificationException e) {

			}
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawMaterialContainer(g);
			drawToolShed(g);
			drawWorkSpace(g);
			drawWorker(g);
		}
	}
}