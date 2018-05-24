package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HelpPane extends JFrame {

	private static JLabel labelEnterMovement;
	private static JLabel labelEnterNotation;
    Color bg_colour = Color.decode("#fcf6ea");
	
	HelpPane() {
		super("Help");
		try {
			drawInterface();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // draw the GUI
	}
	public void setHelpLabel(String info) {
		labelEnterNotation.setText(info);
	}
	public void setHelpTitleLabel(String title) {
		labelEnterMovement.setText(title);
	}

	public void drawInterface()
			throws ClassNotFoundException {
		

		this.setLayout(new GridBagLayout()); // declare layout type
		GridBagConstraints c = new GridBagConstraints();

		JPanel panel = new JPanel(new GridBagLayout()); // add the panel that
														// holds the information
														// on the left part of
														// the screen
		getContentPane().setBackground(bg_colour);
		panel.setBackground(bg_colour);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.insets = new Insets(5, 5, 0, 5);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		this.add(panel, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;

		labelEnterMovement = new JLabel();
		c.gridy = 1;
		panel.add(labelEnterMovement, c);

		labelEnterNotation = new JLabel();
		c.gridy = 2;
		panel.add(labelEnterNotation, c);

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(950, 450);
		this.setVisible(true); //set size and visibility to true
		}
} 