package woyelin_CSCI201_Assignment5b;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class OrderPanel extends JPanel {
	
	JLabel nameCostLbl, woodLbl, metalLbl, plasticLbl, durationLbl;
	JButton accept, decline;
	JPanel woodPanel, metalPanel, plasticPanel, woodImagePanel, metalImagePanel, plasticImagePanel;
	JLabel woodImageLbl, metalImageLbl, plasticImageLbl, durationImageLbl;
	
	ImageIcon woodii = new ImageIcon("./images/wood.png");
	ImageIcon metalii = new ImageIcon("./images/metal.png");
	ImageIcon plasticii = new ImageIcon("./images/plastic.png");
	
	Recipe recipe;
	
	public OrderPanel(Recipe recipe) {
		super();
		this.recipe = recipe;
		nameCostLbl = new JLabel(recipe.name+"-"+" $"+recipe.cost);
		int duration = 0;
		for(Step step: this.recipe.stepVt) {
			duration += step.duration;
		}
		durationLbl = new JLabel(""+duration+"s");
		accept = new JButton("Accept");
		decline = new JButton("Decline");
		
		woodPanel = new JPanel(new BorderLayout());
		woodLbl = new JLabel("Wood");
		woodLbl.setHorizontalAlignment(SwingConstants.CENTER);
		woodPanel.add(woodLbl, BorderLayout.NORTH);
		woodImageLbl = new JLabel(""+recipe.material.woodNum, woodii,SwingConstants.CENTER);
		woodImageLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		woodImageLbl.setVerticalTextPosition(SwingConstants.CENTER);
		woodPanel.add(woodImageLbl);
		
		metalPanel = new JPanel(new BorderLayout());
		metalLbl = new JLabel("Metal");
		metalLbl.setHorizontalAlignment(SwingConstants.CENTER);
		metalPanel.add(metalLbl, BorderLayout.NORTH);
		metalImageLbl = new JLabel(""+recipe.material.metalNum, metalii,SwingConstants.CENTER);
		metalImageLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		metalImageLbl.setVerticalTextPosition(SwingConstants.CENTER);
		metalPanel.add(metalImageLbl);
		
		plasticPanel = new JPanel(new BorderLayout());
		plasticLbl = new JLabel("Plastic");
		plasticLbl.setHorizontalAlignment(SwingConstants.CENTER);
		plasticPanel.add(plasticLbl, BorderLayout.NORTH);
		plasticImageLbl = new JLabel(""+recipe.material.plasticNum, plasticii, SwingConstants.CENTER);
		plasticImageLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		plasticImageLbl.setVerticalTextPosition(SwingConstants.CENTER);
		plasticPanel.add(plasticImageLbl);
		
		this.add(nameCostLbl);
		this.add(woodPanel);
		this.add(metalPanel);
		this.add(plasticPanel);
		this.add(durationLbl);
		this.add(accept);
		this.add(decline);
		
	}
}
