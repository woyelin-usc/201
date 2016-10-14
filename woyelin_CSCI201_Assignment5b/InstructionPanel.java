package woyelin_CSCI201_Assignment5b;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InstructionPanel extends JPanel {
	
	JLabel useLbl;
	JLabel atLbl ;
	JLabel forLbl;
	JLabel sLbl;
	
	JPanel toolPanel, toolPanel1, toolPanel2;
	JTextField toolField1, toolField2, durationField;
	JComboBox toolBox1, toolBox2, workspaceBox;
	
	Object[] tools = {"","Screwdrivers","Hammers","Paintbrushes","Pliers","Scissors"};
	Object[] workspaces = {"Anvil","Workbench","Furnace","Saw","Painting Station","Press"};
	Object[] materials = {"Wood", "Metal", "Plastic"};

	public void configureMaterialPanel() {
		toolPanel = new JPanel();
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
		
		toolPanel1 = new JPanel();
		toolField1 = new JTextField(2);
		toolBox1 = new JComboBox(tools);
		toolPanel1.add(toolField1);
		toolPanel1.add(toolBox1);
		
		toolPanel2 = new JPanel();
		toolField2 = new JTextField(2);
		toolBox2 = new JComboBox(tools);
		toolPanel2.add(toolField2);
		toolPanel2.add(toolBox2);
		
		toolPanel.add(toolPanel1);
		toolPanel.add(toolPanel2);
	}
	
	public InstructionPanel() {
		
		useLbl = new JLabel("Use");
		atLbl = new JLabel("At");
		forLbl = new JLabel("For");
		sLbl = new JLabel("S");
		
		this.add(useLbl);
		configureMaterialPanel();
		this.add(toolPanel);
		
		this.add(atLbl);
		
		workspaceBox = new JComboBox(workspaces);
		this.add(workspaceBox);
		
		this.add(forLbl);
		
		durationField = new JTextField(2);
		this.add(durationField);
		this.add(sLbl);

	}

}
