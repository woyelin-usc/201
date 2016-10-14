package woyelin_CSCI201_Assignment5b;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OrderForm extends JFrame implements Runnable {

	Socket s;
	URL imageUrl;
	Image image;

	JPanel generalPanel, orderPanel, acceptPanel, denyPanel;
	DonePanel donePanel;
	
	
	JLabel acceptLabel, denyLabel;
	JButton back;
	JPanel backBtnPanel;

	JLabel orderDoneLbl;
	JButton doneBtn;
	JPanel doneBtnPanel;

	JPanel upperPanel, itemCostPanel, materialPanel;

	JPanel itemPanel, costPanel;

	JLabel itemLbl, costLbl;

	JLabel materialLbl, woodLbl, metalLbl, plasticLbl;
	JTextField woodField, metalField, plasticField;
	JPanel woodPanel, metalPanel, plasticPanel;

	JTextField itemField, costField;

	JPanel bottomPanel;

	JButton plusBtn, minusBtn;
	JPanel btnPanel, rcpPanel;

	Vector<InstructionPanel> rcpPanelVt;
	JLabel instructionLbl;

	JButton sendBtn;

	JPanel waitingPanel;
	JLabel waitingLabel;

	String item, cost, wood, plastic, metal, toolName, toolNum, workspace,
			duration;

	Recipe recipe;

	ObjectOutputStream os;

	public void configureItemCost() {
		itemCostPanel = new JPanel();
		itemCostPanel.setLayout(new BoxLayout(itemCostPanel, BoxLayout.Y_AXIS));

		itemLbl = new JLabel("Item:");
		costLbl = new JLabel("Cost: ");
		itemField = new JTextField(5);
		costField = new JTextField(5);

		itemPanel = new JPanel();
		itemPanel.add(itemLbl);
		itemPanel.add(itemField);
		costPanel = new JPanel();
		costPanel.add(costLbl);
		costPanel.add(costField);

		itemCostPanel.add(itemPanel);
		itemCostPanel.add(costPanel);

	}

	public void configureMaterialPanel() {
		materialPanel = new JPanel();
		materialPanel.setLayout(new BoxLayout(materialPanel, BoxLayout.Y_AXIS));

		materialLbl = new JLabel("Materials");

		woodLbl = new JLabel("Wood:");
		metalLbl = new JLabel("Metal:");
		plasticLbl = new JLabel("Plastic:");
		woodField = new JTextField(3);
		metalField = new JTextField(3);
		plasticField = new JTextField(3);

		woodPanel = new JPanel();
		woodPanel.add(woodLbl);
		woodPanel.add(woodField);
		metalPanel = new JPanel();
		metalPanel.add(metalLbl);
		metalPanel.add(metalField);
		plasticPanel = new JPanel();
		plasticPanel.add(plasticLbl);
		plasticPanel.add(plasticField);

		materialPanel.add(materialLbl);
		materialPanel.add(woodPanel);
		materialPanel.add(metalPanel);
		materialPanel.add(plasticPanel);

	}

	public void configureRcpPanel() {

		rcpPanel = new JPanel();
		rcpPanel.setLayout(new BoxLayout(rcpPanel, BoxLayout.Y_AXIS));

		instructionLbl = new JLabel("Instructions:");
		rcpPanel.add(instructionLbl);

		rcpPanelVt = new Vector<InstructionPanel>();
		InstructionPanel firstPanel = new InstructionPanel();
		rcpPanelVt.add(firstPanel);
		rcpPanel.add(firstPanel);
	}

	public boolean valid() {

		// If there is a blank Item
		String test = itemField.getText().replaceAll("\\s*", "");
		if (test.length() == 0) {
			JOptionPane.showMessageDialog(null, "invalid input!");
			return false;
		}
		String item = itemField.getText();

		// If there is a blank Cost / cost must be double
		test = costField.getText().replaceAll("\\s*", "");
		if (test.length() == 0) {
			JOptionPane.showMessageDialog(null, "invalid input!");
			return false;
		}
		int cost;
		try {
			cost = Integer.parseInt(costField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "invalid input!");
			return false;
		}

		// No Materials check
		String testWood = woodField.getText().replaceAll("\\s*", "");
		String testPlastic = plasticField.getText().replaceAll("\\s*", "");
		String testMetal = metalField.getText().replaceAll("\\s*", "");
		if (testWood.length() == 0 && testPlastic.length() == 0
				&& testMetal.length() == 0) {
			JOptionPane.showMessageDialog(null, "invalid input!");
			return false;
		}

		// number of materials are not an integer
		int woodNum = 0, plasticNum = 0, metalNum = 0;
		try {
			if (woodField.getText().length() != 0)
				woodNum = Integer.parseInt(woodField.getText());
			if (plasticField.getText().length() != 0)
				plasticNum = Integer.parseInt(plasticField.getText());
			if (metalField.getText().length() != 0)
				metalNum = Integer.parseInt(metalField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "invalid input!");
			return false;
		}

		Material material = new Material( woodNum, metalNum, plasticNum);

		Vector<Step> stepVt = new Vector<Step>();

		// There is no amount of time / number of seconds is not a number
		for (int i = 0; i < rcpPanelVt.size(); i++) {
			InstructionPanel panel = rcpPanelVt.get(i);
			test = panel.durationField.getText().replaceAll("\\s*", "");
			if (test.length() == 0) {
				JOptionPane.showMessageDialog(null, "invalid input!");
				return false;
			}
			int duration;
			try {
				duration = Integer.parseInt(panel.durationField.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "invalid input!");
				return false;
			}
			String workspace = panel.workspaceBox.getSelectedItem().toString();

			Vector<Tool> toolVt = new Vector<Tool>();

			// The Use is mismatched: there is an amount but no tool or vice
			// versa
			String testToolNum = panel.toolField1.getText().replaceAll("\\s*",
					"");
			String testToolName = panel.toolBox1.getSelectedItem().toString()
					.replaceAll("\\s*", "");
			if (((testToolNum.length() == 0 && testToolName.length() != 0))
					|| ((testToolNum.length() != 0 && testToolName.length() == 0))) {
				JOptionPane.showMessageDialog(null, "invalid input!");
				return false;
			}

			// If there is information about this tool, add this tool into
			// vector as part of recipe
			if ((!testToolNum.equalsIgnoreCase(""))
					&& (!testToolName.equalsIgnoreCase(""))) {
				try {
					int toolNum = Integer.parseInt(panel.toolField1.getText()
							.trim());
					String toolName = panel.toolBox1.getSelectedItem()
							.toString();
					toolVt.add(new Tool(toolName, toolNum));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "invalid input!");
					return false;
				}
			}

			testToolNum = panel.toolField2.getText().replaceAll("\\s*", "");
			testToolName = panel.toolBox2.getSelectedItem().toString()
					.replaceAll("\\s*", "");
			if (((testToolNum.length() == 0 && testToolName.length() != 0))
					|| ((testToolNum.length() != 0 && testToolName.length() == 0))) {
				JOptionPane.showMessageDialog(null, "invalid input!");
				return false;
			}

			// If there is information about this tool, add this tool into
			// vector as part of recipe
			if ((!testToolNum.equalsIgnoreCase(""))
					&& (!testToolName.equalsIgnoreCase(""))) {
				try {
					int toolNum = Integer.parseInt(panel.toolField2.getText()
							.trim());
					String toolName = panel.toolBox2.getSelectedItem()
							.toString();
					toolVt.add(new Tool(toolName, toolNum));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "invalid input!");
					return false;
				}
			}
			// At the end of every loop, create a step and add it to the steps
			// vector
			stepVt.add(new Step(toolVt, duration, workspace));
		}

		// When we get here, all information are valid, so create a new recipe
		// with above information
		this.recipe = new Recipe(item, material, stepVt, cost);
		
		return true;

	}

	public void waitForResponse() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String line = br.readLine();
			if (line.equalsIgnoreCase("accept")) {
				((CardLayout) generalPanel.getLayout()).show(generalPanel,
						"accept");
			} else if (line.equalsIgnoreCase("deny")) {
				((CardLayout) generalPanel.getLayout()).show(generalPanel,
						"deny");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInformation() {
		try {
			os.writeInt(0);
			os.flush();
			os.writeObject(this.recipe);
			os.flush();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public OrderForm() {
		super("Order Form");
		setSize(800, 600);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		generalPanel = new JPanel();
		generalPanel.setLayout(new CardLayout());

		orderPanel = new JPanel(new BorderLayout());

		/******** Upper half of the order GUI ********/
		upperPanel = new JPanel();

		configureItemCost();
		upperPanel.add(itemCostPanel);

		configureMaterialPanel();
		upperPanel.add(materialPanel);

		orderPanel.add(upperPanel, BorderLayout.NORTH);

		generalPanel.add(orderPanel, "order");

		/******** Bottom half of the order GUI ********/
		bottomPanel = new JPanel(new BorderLayout());

		btnPanel = new JPanel();
		plusBtn = new JButton("+");
		minusBtn = new JButton("-");
		btnPanel.add(plusBtn);
		btnPanel.add(minusBtn);

		bottomPanel.add(btnPanel, BorderLayout.EAST);

		configureRcpPanel();
		bottomPanel.add(rcpPanel);

		plusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InstructionPanel newPanel = new InstructionPanel();
				rcpPanelVt.add(newPanel);
				rcpPanel.add(newPanel);
				rcpPanel.updateUI();
			}
		});

		minusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InstructionPanel lastPanel = rcpPanelVt.get(rcpPanelVt.size() - 1);
				rcpPanelVt.remove(lastPanel);
				rcpPanel.remove(lastPanel);
				rcpPanel.updateUI();
			}
		});

		// Configure waiting panel
		waitingPanel = new JPanel();
		waitingLabel = new JLabel("Waiting for responses: ");
		waitingLabel.setFont(new Font("Monoco", Font.BOLD, 25));
		waitingPanel.add(waitingLabel);
		generalPanel.add(waitingPanel, "waiting");

		// Configure accept panel
		acceptPanel = new JPanel();
		acceptLabel = new JLabel("Request Accepted!");
		acceptLabel.setFont(new Font("Monoco", Font.BOLD, 25));
		acceptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		acceptLabel.setVerticalAlignment(SwingConstants.CENTER);
		acceptPanel.add(acceptLabel);
		generalPanel.add(acceptPanel, "accept");

		// Configure deny panel
		denyPanel = new JPanel(new BorderLayout());
		denyLabel = new JLabel("Request Deny!");
		denyLabel.setFont(new Font("Monoco", Font.BOLD, 25));
		denyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		denyLabel.setVerticalAlignment(SwingConstants.CENTER);
		denyPanel.add(denyLabel);
		back = new JButton("back");
		backBtnPanel = new JPanel();
		backBtnPanel.add(back);
		denyPanel.add(backBtnPanel, BorderLayout.SOUTH);
		generalPanel.add(denyPanel, "deny");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout) generalPanel.getLayout()).show(generalPanel,
						"order");
			}
		});

		// Configure order-finish panel
		donePanel = new DonePanel();
		orderDoneLbl = new JLabel("Order Complete");
		orderDoneLbl.setHorizontalAlignment(SwingConstants.CENTER);
		orderDoneLbl.setFont(new Font("Monoco", Font.BOLD, 20));
		donePanel.add(orderDoneLbl, BorderLayout.NORTH);
		doneBtn = new JButton("Done");
		doneBtn.setFont(new Font("Monoco", Font.BOLD, 16));
		doneBtnPanel = new JPanel();
		doneBtnPanel.add(doneBtn);
		doneBtn.setHorizontalAlignment(SwingConstants.CENTER);
		donePanel.add(doneBtnPanel, BorderLayout.SOUTH);
		generalPanel.add(donePanel, "done");

		doneBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Every time click the done button, do disconnect client from
				// the server
				try {

					os.writeInt(1);
					os.flush();
					
					System.exit(0);

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		sendBtn = new JButton("Send Request");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// When I return true, I already check instantiate a full recipe
				if (valid()) {
					((CardLayout) generalPanel.getLayout()).show(generalPanel,
							"waiting");
					// Here should send out the .rcp file
					sendInformation();
				}
			}
		});

		JPanel sendPanel = new JPanel();
		sendPanel.add(sendBtn);

		bottomPanel.add(sendPanel, BorderLayout.SOUTH);

		orderPanel.add(bottomPanel);

		JScrollPane scroll = new JScrollPane(generalPanel);
		add(scroll);
		setVisible(true);

		// connect to the server
		try {
			s = new Socket("localhost", 6789);

			os = new ObjectOutputStream(s.getOutputStream());

			Thread t = new Thread(this);
			t.start();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// This is where the client hear back from the factory server whether they
	// accept or deny the order
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				String line = br.readLine();
				
				if(line ==null )
					return;
				
				if (line.equalsIgnoreCase("Accept"))
					((CardLayout) (generalPanel.getLayout())).show(generalPanel, "accept");
				else if (line.equalsIgnoreCase("Deny"))
					((CardLayout) (generalPanel.getLayout())).show(generalPanel, "deny");
				else if (line.equalsIgnoreCase("done"));
//					((CardLayout) (generalPanel.getLayout())).show(generalPanel, "done");
				else if(line.equalsIgnoreCase("url")){
					imageUrl = new URL(br.readLine());
					
					URLConnection connection = imageUrl.openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/4.0");
					
					
//					BufferedImage c = ImageIO.read(connection.getInputStream());
//					ImageIcon ii = new ImageIcon(c);
//					this.donePanel.add(new JLabel(ii));
					
					
					image = ImageIO.read(connection.getInputStream());
					this.donePanel.repaint();
					((CardLayout) (generalPanel.getLayout())).show(generalPanel, "done");	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}
	}

	public static void main(String[] args) {
		new OrderForm();
	}
	
	class DonePanel extends JPanel {
		
		public DonePanel() {
			this.setLayout(new BorderLayout());
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(image!=null) 
				g.drawImage(image, 150, 30, 512, 512, null);
		}	
	}
}
