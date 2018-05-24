package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LearnPane extends JFrame {

	static PiecesDataClass pawn = new PiecesDataClass("Pawn", "1", "<html>Vertical Movement up<br> the board, can move <br> two spaces on first <br> move, but otherwise <br> moves one space forward <br> Diagonal movement to <br> take pieces.</html>", "N/A", 1);
	static PiecesDataClass knight = new PiecesDataClass("Knight", "3", "<html>L shaped Movement<br> either two spaces<br> horizontally and one<br> space veritcal or<br> two spaces vertical<br> and one space<br>horizontal, can take<br> the piece it lands on</html>", "N",2);
	static PiecesDataClass bishop = new PiecesDataClass("Bishop", "3", "<html>Diagonal Movement for<br> any number of spaces <br> take any piece<br>that it lands on</html>", "B", 3);
	static PiecesDataClass rook = new PiecesDataClass("Rook", "5", "<html>Vertical and Horizontal<br> Movement for any number <br> of spaces</html>", "R", 4);
	static PiecesDataClass queen = new PiecesDataClass("Queen", "9", "<html>Movement in any direction<br> and by any amount</html>", "Q", 5);
	static PiecesDataClass king = new PiecesDataClass("King", "Indefinite", "<html>Movement in any direction <br> by one space</html>", "K", 6);
	public static int currentID; // the current ID of the Pieces shown
	private static PiecesDataClass currentPieceData; // declare the swing components
	private static JLabel textEnterName;
	private static JLabel textEnterValue;
	private static JLabel textEnterMovement;
	private static JLabel textEnterNotation;
	private JButton buttonNext;
	private JButton buttonBack;
	private JLabel labelEnterName;
	private JLabel labelEnterValue;
	private JLabel labelEnterMovement;
	private JLabel labelEnterNotation;
	private static JLabel labelImage;
    Color bg_colour = Color.decode("#fcf6ea");
	
	LearnPane() {
		super("Chess Pieces");
		//piecesData = PiecesDAO.selectAllPieces(); // make employees represent all employees in the database
		currentPieceChange();
		getSelectedPiece();
		try {
			updateInformation();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			drawInterface();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // draw the GUI
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

		JPanel panel2 = new JPanel(new GridBagLayout()); // add the panel that
															// holds the
															// picture, previous
															// and next buttons
		panel2.setBackground(bg_colour);

		c.gridx = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		this.add(panel2, c);

		labelEnterName = new JLabel();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		labelEnterName.setText("Name:");
		panel.add(labelEnterName, c);

		labelEnterValue = new JLabel();
		c.gridy = 2;
		labelEnterValue.setText("Value:");
		panel.add(labelEnterValue, c);

		labelEnterMovement = new JLabel();
		c.gridy = 3;
		labelEnterMovement.setText("Movement:");
		panel.add(labelEnterMovement, c);

		labelEnterNotation = new JLabel();
		c.gridy = 4;
		labelEnterNotation.setText("Notation: ");
		panel.add(labelEnterNotation, c);

		textEnterName = new JLabel();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(textEnterName, c);

		textEnterValue = new JLabel();
		c.gridy = 2;
		panel.add(textEnterValue, c);

		textEnterMovement = new JLabel();
		c.gridy = 3;
		panel.add(textEnterMovement, c);

		textEnterNotation = new JLabel();
		c.gridy = 4;
		panel.add(textEnterNotation, c);

		// buttons for scrolling through pieces

		buttonNext = new JButton("Next");

		buttonNext.addActionListener(new buttonPressNext());
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 1;
		c.gridy = 0;
		panel2.add(buttonNext, c);

		buttonBack = new JButton("Previous");

		buttonBack.addActionListener(new buttonPressBack());
		c.gridx = 0;
		c.gridy = 0;
		panel2.add(buttonBack, c);
		
		labelImage = new JLabel();
		c.insets = new Insets(10, 5, 0, 5);
		c.anchor = GridBagConstraints.CENTER;
		panel2.add(labelImage,c);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(950, 450);
		this.setVisible(true); //set size and visibility to true
		currentPieceChange();
		try {
			updateInformation();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // update the display to show the current employee's information
 catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateInformation()
			throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, MalformedURLException {
		{
			try {
				PiecesDataClass emp = getSelectedPiece();
				String imageLocation = "src/img/piece" + currentID + ".gif"; // set the image to the corresponding ID's photo
				ImageIcon curEmpImage = new ImageIcon(imageLocation); // make it an image icon
				//Image curImage = curEmpImage.getImage(); // transform it for resizing
				//Image newimg = curImage.getScaledInstance(65, 90,  java.awt.Image.SCALE_SMOOTH); // resize
				//ImageIcon finImage = new ImageIcon(newimg); // transform back to draw it
				labelImage.setIcon(curEmpImage); // draw it
				
				if (emp.getName() != null) { // if the information isn't null, display it
					textEnterName.setText(emp.getName()); // this is the same for all of the following
				} else {
					textEnterName.setText("");
				}
				if (emp.getValue() != null) {
					textEnterValue.setText(emp.getValue());
				} else {
					textEnterValue.setText("");
				}
				if (emp.getMovement() != null) {
					textEnterMovement.setText(emp.getMovement());
				} else {
					textEnterMovement.setText("");
				}
				if (emp.getNotation() != null) {
					textEnterNotation.setText(emp.getNotation());
				} else {
					textEnterNotation.setText("");
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}

	static class buttonPressNext implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(currentID);
			if(currentID >= 6) {
				System.out.println("no more pieces to iterate to");
			} else {
				currentID = currentID + 1; // move along one on the ID
				currentPieceChange();
				try {
					updateInformation();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			}
		}
	}

	static class buttonPressBack implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(currentID);
			if (currentID < 2) { // if they get to the start and try to go back, wrap around to the end
				currentPieceChange();
				try {
					updateInformation();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else { // else just move back the ID one and update the information accordingly
				currentID = currentID - 1;
				currentPieceChange();
				try {
					updateInformation();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	static void currentPieceChange() {
		switch(currentID) {
		case 1: currentPieceData = pawn;
		break;
		case 2: currentPieceData = knight;
		break;
		case 3: currentPieceData = bishop;
		break;
		case 4: currentPieceData = rook;
		break;
		case 5: currentPieceData = queen;
		break;
		case 6: currentPieceData = king;
		break;
		}
	}

	static PiecesDataClass getSelectedPiece() { // return the employee that is selected at the moment
		return currentPieceData;
	}

} 