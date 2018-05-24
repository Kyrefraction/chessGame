
package chessGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Vincenzo Scialpi 15072935
 */
@SuppressWarnings("serial")
public class PromotionPane extends JDialog implements ActionListener { //This is the pane that appears once a pawn has reached the other end of the board 
    int index;
    int location;
    JPanel mainPane; // the pane for the chess game
    ChessGame chessGame;

    @SuppressWarnings("unused")
	public PromotionPane(ChessGame chessGame){
        setTitle("Promote Pawn");
        this.chessGame = chessGame;
        mainPane = new JPanel(new GridLayout(1,4,10,0));
        Resource resource = new Resource();

        int[] cmdActions = { Piece.QUEEN,Piece.ROOK,Piece.BISHOP,Piece.KNIGHT }; // potential pieces that a pawn can be promoted to once it reaches the end board            
        for(int i=0; i<cmdActions.length; i++){ // make a button for each of the potential pieces
            JButton button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(cmdActions[i]+"");
            mainPane.add(button);
        }
        setContentPane(mainPane);        
        setResizable(false);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                resumeGame(Piece.QUEEN);
            }
        });
    }
    public void setIcons(boolean white){
        Component[] components = mainPane.getComponents();
        Resource resource = new Resource();
        String[] resourceStrings = {"q","r","b","n"};
        for(int i=0; i<components.length; i++){
            JButton button = (JButton)components[i];
            button.setIcon(new ImageIcon(
                    resource.getResource((white?"w":"b")+resourceStrings[i])));
        }
        pack();
        setLocationRelativeTo(null);
    }
    public void actionPerformed(ActionEvent e){
        int promotion_piece = Integer.parseInt(e.getActionCommand());
        setVisible(false);
        resumeGame(promotion_piece);
    }
    public void resumeGame(int promotionPiece){  
        chessGame.position.humanPieces[index] = new Piece(promotionPiece,location);
        chessGame.newHistoryPosition();
        chessGame.chessboardPane.repaint();
        chessGame.stateOfGame = GameData.COMPUTER_MOVE;
    }
}
