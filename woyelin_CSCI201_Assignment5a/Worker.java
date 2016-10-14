package woyelin_CSCI201_Assignment5a;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import woyelin_CSCI201_Assignment5a.Factory.FactoryPanel;

public class Worker extends Thread {

	static Lock toolLock = new ReentrantLock();
	static Condition lackScrewdriver = toolLock.newCondition();
	static Condition lackHammer = toolLock.newCondition();
	static Condition lackPaintbrush = toolLock.newCondition();
	static Condition lackPliers = toolLock.newCondition();
	static Condition lackScissor = toolLock.newCondition();
	
	int index;
	
	int x, y, change;
	boolean visible = true;
	private Vector<Task> taskVt;
	Task currentTask;
	private Vector<Recipe> recipeVt;
//	Vector<JLabel> taskLabelVt;
	 
	WorkSpace currentWorkSpace;

	Recipe currentRecipe;

	private Factory factory;
	
	public void moveX(int dest) {
		int change = 1;
		if(this.x>dest) 
			change = -1;
		while(this.x!=dest) {
			try {
				this.x += change;
				this.factory.repaint();
				this.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void moveY(int dest) {
		int change = 1;
		if(this.y>dest)
			change = -1;
		while(this.y!=dest) {
			try {
				this.y+=change;
				this.factory.factoryPanel.repaint();
				this.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Worker(Factory factory, Vector<Task> taskVt,Vector<Recipe> recipeVt, /*Vector<JLabel> taskLabelVt, */int index) {
		this.index = index;
		this.x = 100-index*50;
		this.y = 80;
		this.taskVt = taskVt;
		this.recipeVt = recipeVt;
		change = 1;
		this.factory = factory;
//		this.taskLabelVt = taskLabelVt;
		this.start();
	}
	
	public void updateTaskBoard() {
		Font font = new Font("Monoco", Font.PLAIN, 16);
		
		factory.taskBoardPanel.removeAll();
		for(int i=0;i<taskVt.size();i++) {
			if(!taskVt.get(i).status.equalsIgnoreCase("")) {
				JLabel lbl = new JLabel(taskVt.get(i).getName()+"..."+taskVt.get(i).status);
				lbl.setFont(font);
				factory.taskBoardPanel.add(lbl);
			}
		}
		factory.taskBoardPanel.updateUI();
	}
	
	public boolean noNewTask() {
		for(Task task: taskVt) {
			if(task.status.equalsIgnoreCase("Not Built"))
				return false;
		}
		return true;
	}
	
	public boolean isComplete() {
		for(Task task: taskVt) {
			if(!task.status.equalsIgnoreCase("Complete"))
				return false;
		}
		return true;
	}

	
	public synchronized void grabTask() {

		// grab a new task
		int i = 0;
		for (; i < taskVt.size(); i++) {
			if (taskVt.get(i).status.equalsIgnoreCase("Not Built")) {
				currentTask = taskVt.get(i);
				currentTask.status = "In Progress";
				updateTaskBoard();
				return;
			}
		}
		
		// Move worker out of taskboard
		moveX(550);
	}
	
	public Recipe getRecipe() {
		int count = 0;
		try {
			String taskName = currentTask.getName();
			count = 0;
			for (int i = 0; i < recipeVt.size(); i++) {
				if (taskName.equalsIgnoreCase(recipeVt.get(i).name)) {
					count = i;
					break;
				}
			}
		} catch (NullPointerException e) {
		}
		return recipeVt.get(count);
	}
	
	// move out of plastic for grabbing next material
	public void moveDownForNextMaterial() {
		moveY(80);
	}
	
	// move the worker to plastic place and reduce the amount of plastic
	public void grabPlastic(int plasticNum) {

		// now x = 530 
		moveX(530);
		// move y up to 25
		moveY(25);
		
		// finally move x left to 500
		moveX(500);
		
		factory.plasticNum -= plasticNum;
		factory.repaint();
		
		// move down to (500,80);
		moveDownForNextMaterial();
		
	}

	public void grabMetal(int metalNum) {
		// No matter whether you use plastic or not before, first move y down to 80, 
		moveY(80);
		
		// if x<380, move right; else,  move left
		moveX(380);
		
		// now x = 380, y = 80; move y up to 25, x left to 350
		moveY(25);
		moveX(350);
		
		factory.metalNum -= metalNum;
		factory.repaint();
		
		moveDownForNextMaterial();
	}
	
	public void grabWood(int woodNum) {
		
		// No matter what material you use before, first move y down to 80, 
		moveY(80);
		
		// move x to 230
		moveX(230);
		
		// finally move next to wood
		moveY(25);
		
		// move x left to touch wood label
		moveX(200);
		
		factory.woodNum -= woodNum;
		factory.repaint();
		
		moveDownForNextMaterial();
	}
	
	public void grabMaterial() {
		currentRecipe = getRecipe();
		Material material = currentRecipe.material;
		if(material.plasticNum !=0)
			grabPlastic(material.plasticNum);
		if(material.metalNum !=0) 
			grabMetal(material.metalNum);
		if(material.woodNum !=0)
			grabWood(material.woodNum);	
		
		// always make sure that after grabbing material, move x to 100
		moveX(100);
	}
	
	public void grabTool(Vector<Tool> toolVt) {
		
		// in order to use condition, must use lock first
		toolLock.lock();
		
		int screwdriver = 0, hammer = 0, paintbrush = 0, plier = 0, scissor = 0;
		
		for(Tool tool:toolVt) {
			String name = tool.name;
			if (name.equalsIgnoreCase("screwdriver")|| name.equalsIgnoreCase("screwdrivers"))
				screwdriver = tool.num;
			else if (name.equalsIgnoreCase("hammer")|| name.equalsIgnoreCase("hammers"))
				hammer = tool.num;
			else if (name.equalsIgnoreCase("paintbrush") || name.equalsIgnoreCase("paintbrushes")) 
				paintbrush = tool.num;
			else if (name.equalsIgnoreCase("pliers") || name.equalsIgnoreCase("plier"))
				plier = tool.num;
			else if (name.equalsIgnoreCase("scissor") || name.equalsIgnoreCase("scissors"))
				scissor = tool.num;
			else;
		}
		
		if (y == 80) 
			moveX(100);
		
		if(screwdriver!=0) {
			moveY(140);
			// if there are not enough tools currently, wait
			while (factory.screwdriverLeft - screwdriver <0) {
				try {
					lackScrewdriver.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if there are enough tools currently, grab tool by touching toolLabel and decrease toolNum
			moveX(62);
			factory.screwdriverLeft -= screwdriver;
			factory.repaint();
			moveX(100);
		}
		
		if(hammer!=0) {
			moveY(220);
			// if there are not enough tools currently, wait
			while (factory.hammerLeft - hammer <0) {
				try {
					lackHammer.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if there are enough tools currently, grab tool by touching toolLabel and decrease toolNum
			moveX(62);
			factory.hammerLeft -= hammer;
			factory.repaint();
			moveX(100);
		}
		
		
		if(paintbrush!=0) {
			moveY(300);
			// if there are not enough tools currently, wait
			while (factory.paintbrushLeft - paintbrush <0) {
				try {
					lackPaintbrush.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if there are enough tools currently, grab tool by touching toolLabel and decrease toolNum
			moveX(62);
			factory.paintbrushLeft -= paintbrush;
			factory.repaint();
			moveX(100);
		}
		
		if(plier!=0) {
			moveY(380);
			// if there are not enough tools currently, wait
			while (factory.pliersLeft - plier <0) {
				try {
					lackPliers.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if there are enough tools currently, grab tool by touching toolLabel and decrease toolNum
			moveX(62);
			factory.pliersLeft -= plier;
			factory.repaint();
			moveX(100);
		}
		
		if(scissor!=0) {
			moveY(460);
			// if there are not enough tools currently, wait
			while (factory.scissorLeft - scissor <0) {
				try {
					lackScissor.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if there are enough tools currently, grab tool by touching toolLabel and decrease toolNum
			moveX(62);
			factory.scissorLeft -= scissor;
			factory.repaint();
			moveX(100);
		}

		toolLock.unlock();
	}
	
	public void moveToWorkSpace(String location) {
		int expectX = 0, expectY = 0;
		
		if (location.equalsIgnoreCase("anvil")) {		
			expectX = 150;
			expectY = 80;
		} else if (location.equalsIgnoreCase("workbench")) {
			expectX = 330;
			expectY = 80;
		} else if (location.equalsIgnoreCase("furnace")) {
			expectX = 150;
			expectY = 220;
		} else if (location.equalsIgnoreCase("TableSaw")) {
			expectX = 330;
			expectY = 220;
		} else if (location.equalsIgnoreCase("PaintingStation")) {
			expectX = 150;
			expectY = 360;
		} else if (location.equalsIgnoreCase("press")) {
			expectX = 510;
			expectY = 360;
		} else;
		
		int change = 1;
		moveY(expectY);
		
		moveX(expectX);
	}
	
	public void produce(String location, int duration) {
		
		int expectX = 0, expectY = 0;
		
		// This is where to modify when multiple workers
		// because should iterate through corresponding workspace vector instead choosing the [0]
		if(location.equalsIgnoreCase("Anvil")) {
			int i =0;
			while (true) {
				try {
					Anvil.semaphore.acquire();
					for (; i < factory.anvilVt.size(); i++) {
						if (factory.anvilVt.get(i).inUse == false) {
							factory.anvilVt.get(i).inUse = true;
							expectX = 150 + 90 * i;
							expectY = 150;
							
							// move x, y to its expected location
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			
			currentWorkSpace = factory.anvilVt.get(i);
			
			try {
				for(int j=duration;j>0;j--) {
					currentWorkSpace.status = ((Integer)j).toString()+"s";
					factory.factoryPanel.repaint();
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				currentWorkSpace.status = "Open";
				currentWorkSpace.inUse = false;
				Anvil.semaphore.release();
				moveY(80);
				moveX(100);
			}
		}
		
		else if(location.equalsIgnoreCase("Workbench")) {
			
			int i=0;
			while(true) {
				try {
					Workbench.semaphore.acquire();
					for(;i<factory.workbenchVt.size();i++) {
						if(factory.workbenchVt.get(i).inUse == false) {
							factory.workbenchVt.get(i).inUse = true;
							expectX = 330+90*i;
							expectY = 150;
							
							// move worker to expectX expectY
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
					
					if(i != factory.workbenchVt.size()) 
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentWorkSpace = factory.workbenchVt.get(i);
			
			try {
				for(int j=duration;j>0;j--) {
					currentWorkSpace.status = ((Integer)j).toString()+"s";
					factory.factoryPanel.repaint();
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				currentWorkSpace.status = "Open";
				currentWorkSpace.inUse = false;
				Workbench.semaphore.release();
				
				// after work, move worker above the current workspace
				moveY(80);
				moveX(100);
			}
		}
		else if(location.equalsIgnoreCase("Furnace")) {
			int i=0;
			while(true) {
				try {
					Furnace.semaphore.acquire();
					for(;i<factory.furnaceVt.size();i++) {
						if(factory.furnaceVt.get(i).inUse == false ) {
							factory.furnaceVt.get(i).inUse = true;
							expectX = 150+i*90;
							expectY = 300;
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
					
					if(i!=factory.furnaceVt.size())
						break;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			currentWorkSpace = factory.furnaceVt.get(i);
			
			for(int k = duration;k>0;k--) {
				try {
					currentWorkSpace.status = ((Integer)k).toString()+"s";
					factory.factoryPanel.repaint();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}  
			
			currentWorkSpace.inUse = false;
			currentWorkSpace.status = "Open";
			Furnace.semaphore.release();
			
			// move worker out of work space
			moveY(220);
			moveX(100);
		}
		else if(location.equalsIgnoreCase("TableSaw")) {
			int i=0;
			while(true) {
				try {
					TableSaw.semaphore.acquire();
					// At here, there must be at least a table saw available
					for(;i<factory.tablesawVt.size();i++) {
						if(factory.tablesawVt.get(i).inUse==false) {
							factory.tablesawVt.get(i).inUse = true;
							expectX = 330+90*i;
							expectY = 300;
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
					break;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
			currentWorkSpace = factory.tablesawVt.get(i);
			
			for(int k=duration;k>0;k--) {
				try {
					currentWorkSpace.status = ((Integer)k).toString()+"s";
					factory.repaint();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentWorkSpace.status = "Open";
			currentWorkSpace.inUse = false;
			TableSaw.semaphore.release();
			
			moveY(220);
			moveX(100);
			
		}

		else if(location.equalsIgnoreCase("PaintingStation")) {
			
			int i=0;
			while(true) {
				try {
					PaintingStation.semaphore.acquire();
					for(;i<factory.paintingstationVt.size();i++) {
						if(factory.paintingstationVt.get(i).inUse == false) {
							factory.paintingstationVt.get(i).inUse = true;
							expectX = 150+90*i;
							expectY = 450;
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
					break;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentWorkSpace = factory.paintingstationVt.get(i);
			for(int k=duration;k>0;k--) {
				try {
					currentWorkSpace.status = ((Integer)k).toString()+"s";
					factory.repaint();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentWorkSpace.inUse = false;
			currentWorkSpace.status = "Open";
			PaintingStation.semaphore.release();
			
			moveY(360);
			moveX(100);
			
		}
		else if(location.equalsIgnoreCase("Press")) {
			
			int i=0;
			while(true) {
				try {
					Press.semaphore.acquire();
					for(;i<factory.pressVt.size();i++) {
						if(factory.pressVt.get(i).inUse == false) {
							factory.pressVt.get(i).inUse = true;
							expectX = 510;
							expectY = 450;
							
							moveX(expectX);
							moveY(expectY);
							break;
						}
					}
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			currentWorkSpace = factory.pressVt.get(i);
			for(int k=duration;k>0;k--) {
				try {
					currentWorkSpace.status = ((Integer)k).toString()+"s";
					factory.repaint();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentWorkSpace.status = "Open";
			currentWorkSpace.inUse = false;
			Press.semaphore.release();
			
			moveY(360);
			moveX(100);
		}
		else;
	}
	
	public void returnTool(Vector<Tool> toolVt) {
		
		// now we are in the aisle, begin return the tools
		for(Tool tool: toolVt) {
			String name = tool.name;
			int expectY = 0;
			if (name.equalsIgnoreCase("screwdriver")|| name.equalsIgnoreCase("screwdrivers"))
				expectY = 140;
			else if (name.equalsIgnoreCase("hammer")|| name.equalsIgnoreCase("hammers"))
				expectY = 220;
			else if (name.equalsIgnoreCase("paintbrush")|| name.equalsIgnoreCase("paintbrushes"))
				expectY = 300;
			else if (name.equalsIgnoreCase("pliers") || name.equalsIgnoreCase("plier"))
				expectY = 380;
			else if (name.equalsIgnoreCase("scissor")|| name.equalsIgnoreCase("scissors"))
				expectY = 460;
			else;
			
			// move y to the tool
			int change = 1;
			moveY(expectY);
			
			// move x to the tool
			moveX(62);
			
			toolLock.lock();
			
			// Now we are next to the tool we want, so return the tool
			if (name.equalsIgnoreCase("screwdriver")|| name.equalsIgnoreCase("screwdrivers")) {
				lackScrewdriver.signalAll();
				factory.screwdriverLeft += tool.num;
			}
			else if (name.equalsIgnoreCase("hammer")|| name.equalsIgnoreCase("hammers")) {
				lackHammer.signalAll();
				factory.hammerLeft += tool.num;
			}
			else if (name.equalsIgnoreCase("paintbrush")|| name.equalsIgnoreCase("paintbrushes")) {
				lackPaintbrush.signalAll();
				factory.paintbrushLeft += tool.num;
			} 
			else if (name.equalsIgnoreCase("pliers")|| name.equalsIgnoreCase("plier")) {
				lackPliers.signalAll();
				factory.pliersLeft += tool.num;
			}
			else if (name.equalsIgnoreCase("scissor")|| name.equalsIgnoreCase("scissors")) {
				lackScissor.signalAll();
				factory.scissorLeft += tool.num;
			}
			else;

			
			factory.repaint();
			toolLock.unlock();
			
			// move x out of tool to 100 for next return tool
			moveX(100);	
		}
	}
	
	public void craft(Step step) {
		String location = step.location;
		int duration = step.duration;
		moveToWorkSpace(location);
		produce(location,duration);
	}
	
	public void returnToTaskBoard() {
		
		moveY(80);
		moveX(600);
		try {
		// mark current status complete
		currentTask.status = "Complete";
		currentTask = null;
		updateTaskBoard();
		} catch(NullPointerException e) {}
	}
	
	public void work(Step step) {
		if(!step.toolVt.isEmpty()) {
			grabTool(step.toolVt);
		}
		craft(step);
		if(!step.toolVt.isEmpty())
			returnTool(step.toolVt);
	}	
	
	public void run() {
		while (true) {
			
			// first move y to 80
			moveY(80);
			// Then move right to the task board
			moveX(600);
			
			if(noNewTask()) {
				this.visible = false;
				factory.workerVt.remove(this);
				factory.repaint();
				break;
			}
			
			grabTask();
			grabMaterial();
			Vector<Step> stepVt = currentRecipe.stepVt;
			for (int i = 0; i < stepVt.size(); i++) {
				Step step = stepVt.get(i);
				work(step);
			}
			returnToTaskBoard();
		}
		
		if(isComplete()) {
			
			this.visible = false;
			factory.workerVt.remove(this);
			factory.repaint();
			
			factory.endTime = System.currentTimeMillis();
			
			long time = factory.endTime - factory.startTime;
			
			Object[] option = {"New directory", "Exit"};
			
			
			// Why do I have to put JFileChooser before JOptionPane?????
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			
			int val = JOptionPane.showOptionDialog(this.factory, "Use "+time/1000+"s","",JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, option,option[0]);

			if(val == 1)
				System.exit(0);

			else if(val == 0) {
				
				int value = fc.showOpenDialog(factory);

				if (value == JFileChooser.APPROVE_OPTION) {
					
					factory.woodNum = 1000;
					factory.metalNum = 1000;
					factory.plasticNum = 1000;
					
					/***first get all informations about taskboard, tool, material, workspace and worker ***/
					// parse new .rcp files
					factory.dir = fc.getSelectedFile();
					// only get .rcp file
					factory.rcpFiles = factory.dir.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".rcp");
						}
					});
					
					factory.parseRcp();
					updateTaskBoard();
					
					
					// parse new .factory file
					factory.factoryFile = factory.dir.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.toLowerCase().endsWith(".factory");
						}
					});
					
					factory.parseFactory();
					factory.startTime =  System.currentTimeMillis();
				}
				
				else
					factory.workerVt.clear();

			}
		}
		
	}
}
	

