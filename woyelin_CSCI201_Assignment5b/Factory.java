package woyelin_CSCI201_Assignment5b;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Factory extends JFrame {
	
	/******* These are for Order Panel *******/
	JLabel moneyInOrderLabel;
	JButton backBtnInOrderPanel;
	JPanel topOrderPanel, centerOrderPanel;
	Vector<OrderPanel> orderPanelVt = new Vector<OrderPanel>();
	JScrollPane orderScrollPane;
	
	Vector<OrderThread> orderThreadVt;
	
	long startTime = 0;
	long endTime = 0;
	
	int money;
	File factoryFileB;
	
	ImageIcon backii = new ImageIcon("./images/back.png");
	ImageIcon ordersii = new ImageIcon("./images/orders.png");
	ImageIcon storeii = new ImageIcon("./images/shop.png");
	Image backi = backii.getImage();
	Image ordersi = ordersii.getImage();
	Image shopi = storeii.getImage();
	
	JButton sdBuyBtn, sdSellBtn,  hmBuyBtn, hmSellBtn, pbBuyBtn, pbSellBtn, 
	plBuyBtn, plSellBtn, scBuyBtn, scSellBtn, woodBuyBtn, woodSellBtn, metalBuyBtn, metalSellBtn,
	plasticBuyBtn, plasticSellBtn, hireBtn, fireBtn;
	
	JButton storeBtn, backBtn, orderBtn;
	ImageIcon orderii = new ImageIcon("./images/orders.png");
	Image orderi = orderii.getImage();
	
	Font stringFont = new Font("Monoco", Font.BOLD, 16);
	
	private JMenuBar menuBar;
	private JMenuItem openItem;
	
	JPanel generalPanel, wholePanel;
	StorePanel storePanel;
	
	JPanel rightPanel = new JPanel(new BorderLayout());
	private JLabel taskBoardLabel;
	JPanel taskBoardPanel;
	private JScrollPane taskBoardScrollPane;

	JPanel orderWholePanel, orderPanel;
	
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
	private ImageIcon workerii = new ImageIcon("./images/worker.png");
	
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
	private Image workeri = workerii.getImage();

	int woodNum, metalNum, plasticNum ;
	int screwdriverNum, hammerNum, paintbrushNum,pliersNum, scissorNum;
	int screwdriverLeft, hammerLeft, paintbrushLeft, pliersLeft, scissorLeft; 

	File dir;
//	File[] rcpFiles, factoryFile;

	Vector<Worker> workerVt = new Vector<Worker>();
//	Vector<Recipe> recipeVt = new Vector<Recipe>();
	Vector<Task> taskVt = new Vector<Task>();
	
	Vector<Anvil> anvilVt = new Vector<Anvil>();
	Vector<Workbench> workbenchVt = new Vector<Workbench>();
	Vector<Furnace> furnaceVt = new Vector<Furnace>();
	Vector<TableSaw> tablesawVt = new Vector<TableSaw>();
	Vector<PaintingStation> paintingstationVt = new Vector<PaintingStation>();
	Vector<Press> pressVt = new Vector<Press>();
	
	public void updateFile() {
		try {
			FileWriter fw = new FileWriter(factoryFileB);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("[Money:"+money+"]");
			pw.println("[Workers:"+workerVt.size()+"]");
			pw.println("[Hammers:"+hammerNum+"]");
			pw.println("[Screwdrivers:"+screwdriverNum+"]");
			pw.println("[Pliers:"+pliersNum+"]");
			pw.println("[Scissors:"+scissorNum+"]");
			pw.println("[Paintbrushes:"+paintbrushNum+"]");
			pw.println("[Wood:"+woodNum+"]");
			pw.println("[Metal:"+metalNum+"]");
			pw.print("[Plastic:"+plasticNum+"]");
			pw.flush();
			pw.close();
			fw.close();
			
			
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
			String name = task.recipe.name;
			JLabel lbl = new JLabel(name+"..."+task.status);
			lbl.setFont(font);
			taskBoardPanel.add(lbl);
		}
		
		taskBoardPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
		
		taskBoardScrollPane = new JScrollPane(taskBoardPanel);
		taskBoardScrollPane.setPreferredSize(new Dimension(200,600));
		
		
		rightPanel.add(taskBoardScrollPane);

		wholePanel.add(rightPanel, BorderLayout.EAST);
		
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
	
	// create a new .factory file in src folder
	public void createFactoryFileB() {
		try {
			FileWriter fw = new FileWriter("./src/factory.factory");
			PrintWriter pw = new PrintWriter(fw);
			pw.println("[Money:100]");
			pw.println("[Workers:0]");
			pw.println("[Hammers:0]");
			pw.println("[Screwdrivers:0]");
			pw.println("[Pliers:0]");
			pw.println("[Scissors:0]");
			pw.println("[Paintbrushes:0]");
			pw.println("[Wood:0]");
			pw.println("[Metal:0]");
			pw.print("[Plastic:0]");
			pw.flush();
			pw.close();
			fw.close();
			factoryFileB = new File("./src/factory.factory");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This is to read in factory files for assignment5b
	public void grabFactoryFileB() {
		File dir = new File("./src");
		for(File file: dir.listFiles()) {
			if(file.getAbsolutePath().endsWith(".factory")) {
				factoryFileB = file;
				return;
			}
		}
		// if we don't have an existing .factory file, create a new one
		createFactoryFileB();
	}
	
	// Read in factory file to initialize the information about material, worker and tool
	public void readFactoryFileB() {
		grabFactoryFileB();
		try {
			BufferedReader br = new BufferedReader(new FileReader(factoryFileB));
			String line = "";
			while ((line = br.readLine()) != null) {
				int left = line.indexOf("[");
				int right = line.indexOf("]");
				line = line.substring(left+1, right);
				// remove all whitespace
				line = line.replaceAll("\\s*", "");
				String[] split = line.split(":");
				String name = split[0];
				int num = Integer.parseInt(split[1]);
				if(name.equalsIgnoreCase("Money")) 
					money = num;
				else if(name.equalsIgnoreCase("Workers") || name.equalsIgnoreCase("Worker")) {
					for(int i=0;i<num;i++) 
						workerVt.add(new Worker(this,  /*recipeVt,*/ i));
				}
				else if(name.equalsIgnoreCase("Hammers") || name.equalsIgnoreCase("Hammer"))
					hammerNum = hammerLeft = num;
				else if(name.equalsIgnoreCase("Screwdrivers") || name.equalsIgnoreCase("Screwdriver"))
					screwdriverNum = screwdriverLeft = num;
				else if(name.equalsIgnoreCase("Pliers") || name.equalsIgnoreCase("Plier"))
					pliersNum = pliersLeft = num;
				else if(name.equalsIgnoreCase("Scissors") || name.equalsIgnoreCase("Scissor"))
					scissorNum = scissorLeft = num;
				else if(name.equalsIgnoreCase("Paintbrushes") || name.equalsIgnoreCase("Paintbrush"))
					paintbrushNum = paintbrushLeft = num;
				else if(name.equalsIgnoreCase("Wood"))
					woodNum = num;
				else if(name.equalsIgnoreCase("metal"))
					metalNum = num;
				else if(name.equalsIgnoreCase("plastic"))
					plasticNum = num;
				else;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void configureFactoryPanel() {
		wholePanel = new JPanel(new BorderLayout());
		initializeWorkSpace();
		factoryPanel = new FactoryPanel();
		storeBtn = new JButton();
		storeBtn.setIcon(storeii);
		storeBtn.setBounds(90,40,50,50);
		orderBtn = new JButton();
		orderBtn.setIcon(orderii);
		orderBtn.setBounds(540,20,50,50);
		factoryPanel.add(storeBtn);
		factoryPanel.add(orderBtn);
		wholePanel.add(factoryPanel);
		createTaskBoard();
		
	}
	
	public void configureBackBtn() {
		backBtn = new JButton();
		backBtn.setIcon(backii);
		backBtn.setBounds(90,40,50,50);
		storePanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)generalPanel.getLayout()).show(generalPanel, "factory");
			}
		});
	}
	
	public void configureToolTradeBtn() {
		// screwdriver buy & sell buttons
		sdBuyBtn = new JButton("Buy");
		sdBuyBtn.setBounds(200, 130, 100, 30);
		sdBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (money - 10 > 0) {
					screwdriverNum++;
					screwdriverLeft++;
					money -= 10;
					storePanel.repaint();
					Worker.toolLock.lock();
					Worker.lackScrewdriver.signalAll();
					Worker.toolLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(sdBuyBtn);
		sdSellBtn = new JButton("Sell");
		sdSellBtn.setBounds(200, 170, 100, 30);
		sdSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (screwdriverLeft > 0) {
					money += 7;
					screwdriverLeft--;
					screwdriverNum--;
					storePanel.repaint();
					updateFile();
				}
				
			}
		});
		storePanel.add(sdSellBtn);
		// hammer buy & sell buttons
		hmBuyBtn = new JButton("Buy");
		hmBuyBtn.setBounds(200, 210, 100, 30);
		hmBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money - 12>=0) {
					hammerNum++;
					hammerLeft++;
					money -= 12;
					storePanel.repaint();
					Worker.toolLock.lock();
					Worker.lackHammer.signalAll();
					Worker.toolLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(hmBuyBtn);
		hmSellBtn = new JButton("Sell");
		hmSellBtn.setBounds(200, 250, 100, 30);
		hmSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hammerLeft > 0) {
					money += 9;
					hammerLeft--;
					hammerNum--;
					storePanel.repaint();
					updateFile();
				}
				
			}
		});
		storePanel.add(hmSellBtn);
		
		// paintbrush buy & sell buttons
		pbBuyBtn = new JButton("Buy");
		pbBuyBtn.setBounds(200, 290, 100, 30);
		pbBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money - 5>=0) {
					paintbrushNum++;
					paintbrushLeft++;
					money -= 5;
					storePanel.repaint();
					Worker.toolLock.lock();
					Worker.lackPaintbrush.signalAll();
					Worker.toolLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(pbBuyBtn);
		pbSellBtn = new JButton("Sell");
		pbSellBtn.setBounds(200, 330, 100, 30);
		pbSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (paintbrushLeft > 0) {
					money += 3;
					paintbrushLeft--;
					paintbrushNum--;
					storePanel.repaint();
					updateFile();
				}
				
			}
		});
		storePanel.add(pbSellBtn);
		
		// pliers buy & sell buttons
		plBuyBtn = new JButton("Buy");
		plBuyBtn.setBounds(200, 370, 100, 30);
		plBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money - 11>=0) {
					pliersNum++;
					pliersLeft++;
					money -= 11;
					storePanel.repaint();
					Worker.toolLock.lock();
					Worker.lackPliers.signalAll();
					Worker.toolLock.unlock();
					updateFile();
				}
			}
		});
		
		storePanel.add(plBuyBtn);
		plSellBtn = new JButton("Sell");
		plSellBtn.setBounds(200, 410, 100, 30);
		plSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pliersLeft > 0) {
					money += 9;
					pliersLeft--;
					pliersNum--;
					storePanel.repaint();
					updateFile();
				}
				
			}
		});
		storePanel.add(plSellBtn);
		
		
		// scissor buy & sell buttons
		scBuyBtn = new JButton("Buy");
		scBuyBtn.setBounds(200, 450, 100, 30);
		scBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money - 9>=0) {
					scissorNum++;
					scissorLeft++;
					money -= 9;
					storePanel.repaint();
					Worker.toolLock.lock();
					Worker.lackScissor.signalAll();
					Worker.toolLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(scBuyBtn);
		scSellBtn = new JButton("Sell");
		scSellBtn.setBounds(200, 490, 100, 30);
		scSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (scissorLeft > 0) {
					money += 7;
					scissorLeft--;
					scissorNum--;
					storePanel.repaint();
					updateFile();
				}
				
			}
		});
		storePanel.add(scSellBtn);
	}

	public void configureMaterialTradeBtn() {
		// wood panel
		woodBuyBtn = new JButton("Buy");
		woodBuyBtn.setBounds(550,130,100,30);
		woodBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money - 1 >=0) {
					woodNum++;
					money -= 1;
					storePanel.repaint();
					Worker.materialLock.lock();
					Worker.lackWood.signalAll();
					Worker.materialLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(woodBuyBtn);
		woodSellBtn = new JButton("Sell");
		woodSellBtn.setBounds(550,170,100,30);
		woodSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(woodNum>0) {
					woodNum--;
					money += 1;
					storePanel.repaint();
					updateFile();
				}
			}
		});
		storePanel.add(woodSellBtn);
		
		// metal panel
		metalBuyBtn = new JButton("Buy");
		metalBuyBtn.setBounds(550,210,100,30);
		metalBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money -3 >=0) {
					metalNum++;
					money -= 3;
					storePanel.repaint();
					Worker.materialLock.lock();
					Worker.lackMetal.signalAll();
					Worker.materialLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(metalBuyBtn);
		metalSellBtn = new JButton("Sell");
		metalSellBtn.setBounds(550,250,100,30);
		metalSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(metalNum>0) {
					metalNum--;
					money += 2;
					storePanel.repaint();
					updateFile();
				}
			}
		});
		storePanel.add(metalSellBtn);
		
		// plastic panel
		plasticBuyBtn = new JButton("Buy");
		plasticBuyBtn.setBounds(550,290,100,30);
		plasticBuyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money -2 >=0) {
					plasticNum++;
					money -= 2;
					storePanel.repaint();
					Worker.materialLock.lock();
					Worker.lackPlastic.signalAll();
					Worker.materialLock.unlock();
					updateFile();
				}
			}
		});
		storePanel.add(plasticBuyBtn);
		plasticSellBtn = new JButton("Sell");
		plasticSellBtn.setBounds(550,330,100,30);
		plasticSellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(plasticNum>0) {
					plasticNum--;
					money += 1;
					storePanel.repaint();
					updateFile();
				}
			}
		});
		storePanel.add(plasticSellBtn);
	}
	
	public void configureWorkerTradeBtn() {
		hireBtn = new JButton("Hire");
		hireBtn.setBounds(550,410,100,30);
		hireBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(money -15 >=0) {
					int index = workerVt.size();
					workerVt.add(new Worker(Factory.this, /*recipeVt,*/ index));
					Factory.this.repaint();
					money -= 15;
					storePanel.repaint();
					updateFile();
				}
			}
		});
		storePanel.add(hireBtn);
		fireBtn = new JButton("Fire");
		fireBtn.setBounds(550, 450,100,30);
		fireBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!workerVt.isEmpty()) {
					int i = 0;
					int size = workerVt.size();
					// First try to fire a worker that doesn't work right now
					for (; i < workerVt.size(); i++) {
						Worker worker = workerVt.get(i);
						if (worker.currentTask == null) {
							worker.fire = true;
							workerVt.remove(worker);
							money += 15;
							storePanel.repaint();
							updateFile();
							Factory.this.repaint();
							return;
						}
					}
					
					// If all workers are currently working, just fire the last non-fire worker in the vector
					if (i == size) {
						for(i = i-1;i>=0;i--) {
							Worker worker = workerVt.get(i);
							if(worker.fire == false) {
								worker.fire = true;
								break;
							}
						}
					}
				}
				Factory.this.repaint();
			}
		});
		storePanel.add(fireBtn);
	}
	
	// add all components (JButton) here
	// all graphics already have been drawn
	public void configureStorePanel() {
		storePanel = new StorePanel();
		storePanel.setLayout(null);
		configureBackBtn();
		configureToolTradeBtn();
		configureMaterialTradeBtn();
		configureWorkerTradeBtn();
	}
	
	public void configureOrderPanel() {
		orderWholePanel = new JPanel(new BorderLayout());
		topOrderPanel = new JPanel();
		moneyInOrderLabel = new JLabel("Money: $"+money);
		moneyInOrderLabel.setFont(new Font("Monoco", Font.BOLD, 16));
		backBtnInOrderPanel = new JButton();
		backBtnInOrderPanel.setIcon(backii);
		topOrderPanel.add(moneyInOrderLabel);
		topOrderPanel.add(backBtnInOrderPanel);
		orderWholePanel.add(topOrderPanel, BorderLayout.NORTH);
		backBtnInOrderPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)(generalPanel.getLayout())).show(generalPanel, "factory");
			}
		});
		
		centerOrderPanel = new JPanel();
		centerOrderPanel.setLayout(new BoxLayout(centerOrderPanel, BoxLayout.Y_AXIS));
		orderWholePanel.add(centerOrderPanel);
	}
		
	public void initialize() {
		
		readFactoryFileB();
		
		generalPanel = new JPanel(new CardLayout());
		
		// Configure and add factory panel into cardLayout
		configureFactoryPanel();
		generalPanel.add(wholePanel, "factory");
		
		// Configure and add store panel into cardLayout
		configureStorePanel();
		generalPanel.add(storePanel, "store");	
		
		// Configure and add Order panel into cardLayout
		configureOrderPanel();
		orderScrollPane = new JScrollPane(orderWholePanel);
		generalPanel.add(orderScrollPane, "order");

		// storeBtn is used to switch between factory panel and store panel
		storeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 ((CardLayout) (generalPanel.getLayout())).show(generalPanel, "store");
			}
		});
		
		// orderBtn is used to switch between factory panel and order panel
		orderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout) generalPanel.getLayout()).show(generalPanel,  "order");
			}
		});

		add(generalPanel);
	}
	
	public void sendCompleteInfo(Task task /*OrderThread orderThread*/) {
		for(OrderThread thread: orderThreadVt) {
			if(thread.equals(task.orderThread))
				thread.sendDoneInfoToThisClient(task.recipe.name);
		}
	}
	
	public Factory() {
		super("Factory");
		setSize(800,600);
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initialize();
		
		this.setResizable(false);
		setVisible(true);
		
		
		/*********** Configure Server ***********/
		try {
			ServerSocket ss = new ServerSocket(6789);
			orderThreadVt = new Vector<OrderThread>();
			while(true) {
				Socket s = ss.accept();
				OrderThread orderThread = new OrderThread(this,s);
				orderThreadVt.add(orderThread);
				orderThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new Factory();
	}
	
	
	/****** Customized Factory Panel ******/
	class FactoryPanel extends JPanel {
		
		private int locX = 1, locY = 1;
		private int change = 5;
		
		public FactoryPanel() {
			this.setLayout(null);
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
				for (Worker worker : workerVt) 
					g.drawImage(workeri, worker.x, worker.y, 50, 50, null);
			} catch (ConcurrentModificationException e) {
			}
		}
		
		public void drawMoney(Graphics g) {
			g.setFont(new Font("Monoco", Font.PLAIN, 20));
			g.drawString("Money",25, 40);
			g.drawString("$"+money, 25,60);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawMaterialContainer(g);
			drawToolShed(g);
			drawWorkSpace(g);
			drawWorker(g);
			drawMoney(g);
		}
	}
	
	
	/****** Customized StorePanel *******/
	class StorePanel extends JPanel {
		
		public void drawMoney(Graphics g) {
			g.setFont(new Font("Monoco", Font.PLAIN, 20));
			g.drawString("Money", 25, 40);
			g.drawString("$"+money, 25, 60);
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
			
			g.drawString(""+screwdriverNum,30, 170 );
			g.drawString(""+hammerNum, 30, 250);
			g.drawString(""+paintbrushNum, 30, 330);
			g.drawString(""+pliersNum, 30, 410);
			g.drawString(""+scissorNum, 30, 490);
			
		}
		
		public void drawToolShedPrice(Graphics g) {
			g.setFont(stringFont);
			g.drawString("$10",150, 150);
			g.drawString("$7", 150, 190);
			g.drawString("$12",150, 230);
			g.drawString("$9", 150, 270);
			g.drawString("$5", 150, 310);
			g.drawString("$3", 150, 350);
			g.drawString("$11",150, 390);
			g.drawString("$9", 150, 430);
			g.drawString("$9", 150, 470);
			g.drawString("$7", 150, 510);
		}
		
		// include material label and material name above label
		public void drawMaterial(Graphics g) {
			g.setFont(stringFont);
			// draw wood
			g.drawString("Wood", 350, 135);
			g.drawImage(woodi, 350, 140, 50, 50, null);
			g.drawString(""+woodNum, 370, 170);
			// draw metal
			g.drawString("Metal", 350, 215);
			g.drawImage(metali, 350, 220,50,50,null);
			g.drawString(""+metalNum,  370, 250);
			// draw plastic
			g.drawString("Plastic", 350,295);
			g.drawImage(plastici,350,300,50,50,null);
			g.drawString(""+plasticNum, 370, 330);
		}
		
		public void drawMaterialPrice(Graphics g) {
			g.setFont(stringFont);
			g.drawString("$1", 500, 150);
			g.drawString("$1", 500,190);
			g.drawString("$3", 500, 230);
			g.drawString("$2", 500,270);
			g.drawString("$2", 500,310);
			g.drawString("$1", 500,350);
		}
		
		public void drawWorker(Graphics g) {
			g.drawImage(workeri, 350,420,50,50,null);
			g.setFont(stringFont);
			g.drawString("$15", 500,430 );
			g.drawString("$15", 500,470);
			g.drawString(""+workerVt.size(), 365,490);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawMoney(g);
			drawToolShed(g);
			drawToolShedPrice(g);
			drawMaterial(g);
			drawMaterialPrice(g);
			drawWorker(g);
		}
	}
}

