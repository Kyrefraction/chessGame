
package chessGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class PreferencesPane extends JFrame implements ActionListener{
     JSlider levelSlider;
     JRadioButton whiteButton;
     JRadioButton blackButton;
     JButton ok;
     JButton cancel;
     final static int WHITE = 0;
     final static int BLACK = 1;
     ChessGame chessGame;

    public PreferencesPane(ChessGame chessGame){
        super("Options");
        this.chessGame = chessGame;
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(createLevelPane(),BorderLayout.NORTH);
        mainPane.add(createColorPane(),BorderLayout.CENTER);
        mainPane.add(createButtonPane(),BorderLayout.SOUTH);
        mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setContentPane(mainPane);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ok.addActionListener(this);
        cancel.addActionListener(this);
    }
    public JPanel createLevelPane(){
        levelSlider = new JSlider(JSlider.HORIZONTAL,1,5,4);
        JPanel levelPane = new JPanel();        
        levelSlider.setMajorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        levelPane.add(levelSlider);
        levelPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createTitledBorder("Level")));
        return levelPane;
    }
    public JPanel createColorPane(){
        JPanel colorPane = new JPanel(new GridLayout(1,2));
        whiteButton = new JRadioButton("White",true);
        blackButton = new JRadioButton("Black");
        ButtonGroup group = new ButtonGroup();
        group.add(whiteButton);
        group.add(blackButton);
        colorPane.add(whiteButton);
        colorPane.add(blackButton);
        colorPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createTitledBorder("Color")));
        return colorPane;
    }
    public JPanel createButtonPane(){
        JPanel buttonPane = new JPanel(new BorderLayout());
        JPanel pane = new JPanel(new GridLayout(1,2,5,0));
        pane.add(ok = new JButton("OK"));
        pane.add(cancel = new JButton("Cancel"));
        buttonPane.add(pane,BorderLayout.EAST);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return buttonPane;
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == ok){
            chessGame.stateOfGame = GameData.GAME_ENDED;
            chessGame.newGame();           
        }
        setVisible(false);
    }
}
