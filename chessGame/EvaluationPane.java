package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EvaluationPane extends JFrame {
	static int pawnEval = 0;
	static int knightEval = 0;
	static int bishopEval = 0;
	static int overallEval = 0;
	private static JLabel labelEnterName;
    Color bg_colour = Color.decode("#fcf6ea");
	
	EvaluationPane() {
		super("Evaluation");
		try {
			drawInterface();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void setEnterName(String info) {
		System.out.println("TEXT SET" + info);
		labelEnterName.setText(info);
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

		labelEnterName = new JLabel();
		c.gridy = 1;
		panel.add(labelEnterName, c);


		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true); //set size and visibility to true
		
	}

} 
