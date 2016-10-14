package woyelin_CSCI201_Assignment5b;

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

import woyelin_CSCI201_Assignment5b.Tool;
import woyelin_CSCI201_Assignment5b.Factory.FactoryPanel;

public class Worker extends Thread {

	static Lock toolLock = new ReentrantLock();
	static Condition lackScrewdriver = toolLock.newCondition();
	static Condition lackHammer = toolLock.newCondition();
	static Condition lackPaintbrush = toolLock.newCondition();
	static Condition lackPliers = toolLock.newCondition();
	static Condition lackScissor = toolLock.newCondition();
	
	static Lock taskLock = new ReentrantLock();
	static Condition waitForTask = taskLock.newCondition();
	
	static Lock materialLock = new ReentrantLock();
	static Condition lackWood = materialLock.newCondition();
	static Condition lackMetal = materialLock.newCondition();
	static Condition lackPlastic = materialLock.newCondition();
	
	
	int index;
	
	int x, y, change;
//	boolean visible = true;
	Task currentTask;
//	private Vector<Recipe> recipeVt;
	 
//	Recipe currentRecipe;
	WorkSpace currentWorkSpace;

	boolean fire = false;

	private Factory factory;
	
	public void moveX(int dest) {
		int change = 1;
		if(this.x>dest) 
			change = -1;
		while(this.x!=dest) {
			try {
				this.x += change;
				this.sleep(3);
				factory.factoryPanel.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				
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
				this.sleep(3);
				factory.factoryPanel.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Worker(Factory factory, /*Vector<Recipe> recipeVt,*/ int index) {
		this.index = index;
		this.x = 100-index*50;
		this.y = 80;
//		this.recipeVt = recipeVt;
		change = 1;
		this.factory = factory;
//		this.visible = true;
		this.start();
	}
	
	public void updateTaskBoard() {
		Font font = new Font("Monoco", Font.PLAIN, 16);
		factory.taskBoardPanel.removeAll();
		factory.taskBoardPanel.updateUI();
		for(int i=0;i<factory.taskVt.size();i++) {
			if(!factory.taskVt.get(i).status.equalsIgnoreCase("")) {
				JLabel lbl = new JLabel(factory.taskVt.get(i).recipe.name+"..."+factory.taskVt.get(i).status);
				lbl.setFont(font);
				factory.taskBoardPanel.add(lbl);
				factory.taskBoardPanel.updateUI();
			}
		}
	}
	
	public boolean noNewTask() {
		for(Task task: factory.taskVt) {
			if(task.status.equalsIgnoreCase("Not Built"))
				return false;
		}
		return true;
	}
	
	public boolean isComplete() {
		for(Task task: factory.taskVt) {
			if(!task.status.equalsIgnoreCase("Complete"))
				return false;
		}
		return true;
	}
	
	public synchronized void grabTask() {

		// grab a new task
		int i = 0;
		for (; i < factory.taskVt.size(); i++) {
			if (factory.taskVt.get(i).status.equalsIgnoreCase("Not Built")) {
				currentTask = factory.taskVt.get(i);
				currentTask.status = "In Progress";
				updateTaskBoard();
				return;
			}
		}

	}
	
	// move out of plastic for grabbing next material
	public void moveDownForNextMaterial() {
		moveY(80);
	}
	
	// move the worker to plastic place and reduce the amount of plastic
	public void grabPlastic(int plasticNum) {
		
		// first move y to 80
		moveY(80);
		
		// then move x to 530
		moveX(530);

		// now x = 530, y = 80, move y up to 25
		moveX(530);
		moveY(25);
		
		// finally move x left to 500
		moveX(500);
		
		
		while(factory.plasticNum - plasticNum < 0) {
			try {
				this.materialLock.lock();
				this.lackPlastic.await();
				this.materialLock.unlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		factory.plasticNum -= plasticNum;
		factory.factoryPanel.repaint();
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
		
		while(factory.metalNum - metalNum < 0) {

			try {
				this.materialLock.lock();
				this.lackMetal.await();
				this.materialLock.unlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		factory.metalNum -= metalNum;
		factory.factoryPanel.repaint();
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
		
		while(factory.woodNum - woodNum < 0) {
			try {
				this.materialLock.lock();
				this.lackWood.await();
				this.materialLock.unlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		factory.woodNum -= woodNum;
		factory.factoryPanel.repaint();
		moveDownForNextMaterial();
	}
	
	public void grabMaterial() {
		
		Material material = currentTask.recipe.material;
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
		} else if (location.equalsIgnoreCase("TableSaw") || location.equalsIgnoreCase("saw")) {
			expectX = 330;
			expectY = 220;
		} else if (location.equalsIgnoreCase("PaintingStation") || location.equalsIgnoreCase("Painting Station")) {
			expectX = 150;
			expectY = 360;
		} else if (location.equalsIgnoreCase("press")) {
			expectX = 510;
			expectY = 360;
		} else;
		
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
		else if(location.equalsIgnoreCase("TableSaw") || location.equalsIgnoreCase("Saw")) {
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
					factory.factoryPanel.repaint();
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

		else if(location.equalsIgnoreCase("PaintingStation") || location.equalsIgnoreCase("Painting Station") || location.equalsIgnoreCase("Painting")) {
			
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
					factory.factoryPanel.repaint();
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
					factory.factoryPanel.repaint();
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
				factory.factoryPanel.repaint();
			}
			else if (name.equalsIgnoreCase("hammer")|| name.equalsIgnoreCase("hammers")) {
				lackHammer.signalAll();
				factory.hammerLeft += tool.num;
				factory.factoryPanel.repaint();
			}
			else if (name.equalsIgnoreCase("paintbrush")|| name.equalsIgnoreCase("paintbrushes")) {
				lackPaintbrush.signalAll();
				factory.paintbrushLeft += tool.num;
				factory.factoryPanel.repaint();
			} 
			else if (name.equalsIgnoreCase("pliers")|| name.equalsIgnoreCase("plier")) {
				lackPliers.signalAll();
				factory.pliersLeft += tool.num;
				factory.factoryPanel.repaint();
			}
			else if (name.equalsIgnoreCase("scissor")|| name.equalsIgnoreCase("scissors")) {
				lackScissor.signalAll();
				factory.scissorLeft += tool.num;
				factory.factoryPanel.repaint();
			}
			else;

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
		moveX(550);
		try {
		// mark current status complete
		currentTask.status = "Complete";
		updateTaskBoard();
		this.factory.taskVt.remove(currentTask);
		updateTaskBoard();
		
		factory.money += currentTask.recipe.cost;
		factory.factoryPanel.repaint();
		factory.moneyInOrderLabel.setText("Money: $"+factory.money);
		factory.moneyInOrderLabel.setFont(new Font("Monoco", Font.BOLD, 16));
		
		
		factory.sendCompleteInfo(currentTask);
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
			moveX(550);
		
			while(noNewTask()) {
				try {
					taskLock.lock();
					waitForTask.await();
					taskLock.unlock();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			// If the worker being waken up is fired, this worker wake up other workers
			if (this.fire == true) {
				factory.workerVt.remove(this);
				factory.updateFile();
				factory.factoryPanel.repaint();
				taskLock.lock();
				waitForTask.signal();
				taskLock.unlock();
				return;
			}
			
			// doing a new task
			grabTask();
			
			grabMaterial();
			
			Vector<Step> stepVt = currentTask.recipe.stepVt;
			for (int i = 0; i < stepVt.size(); i++) {
				Step step = stepVt.get(i);
				work(step);
			}
			
			returnToTaskBoard();
			
			this.currentTask = null;
			
			// If this worker got fired, remove it from the vector and stop this
			// thread;
			if (this.fire == true) {
				factory.workerVt.remove(this);
				factory.updateFile();
				factory.factoryPanel.repaint();
				return;
			}
		}
	}
}
	

