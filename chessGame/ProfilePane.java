package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProfilePane extends JFrame {

	private static JLabel profileXP;
	private static JLabel profileLevel;
	private static JLabel labelImage;
    Color bg_colour = Color.decode("#fcf6ea");
	
	ProfilePane() {
		super("Profile");
		try {
			drawInterface();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // draw the GUI
	}
	public void setLevel(String info) {
		profileXP.setText(info);
	}
	public void setXP(String title) {
		profileLevel.setText(title);
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

		profileXP = new JLabel();
		c.gridy = 1;
		panel.add(profileXP, c);
		
    	if(ChessGame.level<10) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank1.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level < 15) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank2.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level < 20) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank3.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level < 25) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank4.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level < 30) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank5.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level < 35) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank6.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
    		labelImage.setIcon(finImage);
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	} else if(ChessGame.level >= 35) {
    		labelImage = new JLabel();
    		String imageLocation = "src/img/rank7.png"; // set the image to the corresponding ID's photo
    		ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
    		Image curImage = curEmpImage.getImage(); // transform it for resizing
    		Image newimg = curImage.getScaledInstance(65, 65,  java.awt.Image.SCALE_SMOOTH); // resize
    		ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
			labelImage.setIcon(finImage); // draw it
    		c.gridy = 2;
    		panel.add(labelImage,c);
    	}
		profileLevel = new JLabel();
		c.gridy = 3;
		panel.add(profileLevel, c);
		


		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(950, 550);
		this.setVisible(true); //set size and visibility to true
		}
} 