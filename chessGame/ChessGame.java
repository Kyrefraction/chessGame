package chessGame;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class ChessGame extends JFrame implements MouseListener{
    Position position;        
    ChessBoardPane chessboardPane;
    JPanel centralPane = new JPanel(new BorderLayout());
    HistoryBoardPane historyPane;
    JPanel easternPane;
    JLabel newGame,quit,learn,history,help,first,previous,next,last,profile;
    Resource resource = new Resource();
    Map<Integer,Image> images = new HashMap<Integer,Image>();
    List<Position> previousPositions = new ArrayList<Position>();
    Map<Integer,Icon> icon_images = new HashMap<Integer,Icon>();
    Move move = new Move();
    Move potentialMove = new Move();
    static String userName = "";
    static int suggestionLock = 0;
    static int suggestionPiece = 0;
    static int suggestionMove = 0;
    static int level;
    static int xp;
    static int levelThreshold;
    boolean isPieceSelected;
    static int lastClickLocation = 1;
    static int levelSelected;
    int stateOfGame;
    EvaluatorForFeedback EFF = new EvaluatorForFeedback();
    MoveSearcher mainMoveSearcher;
    MoveSearcher helperMoveSearcher;
    Game game;    
    PreferencesPane levelAndColourSelect;
    boolean castling;
    PromotionPane promotingAPiecePane;
    int history_count;
    public static int turnTimer = 999;
    public static String gamePhase = "Early Game";
    boolean isPlayingWhite;
    Color bg_color = Color.decode("#fcf6ea");
    
    public ChessGame(){
        super("Provenance Chess "+GameData.VERSION);                                  
        setContentPane(centralPane);                
        position = new Position();
        promotingAPiecePane = new PromotionPane(this);
        
        loadMenuIcons();
        loadBoardImages();
        
        chessboardPane = new ChessBoardPane();                                             
        
        centralPane.add(createMenuPane(),BorderLayout.WEST);
        centralPane.add(chessboardPane,BorderLayout.CENTER);  
        centralPane.setBackground(bg_color);      
        createEastPane();
        
        pack();
        Dimension size = getSize();
        size.height = 523;
        setSize(size);
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                quit();
            }
        });
    }               
    public JPanel createMenuPane(){
        newGame = new JLabel(icon_images.get(GameData.NEW_BUTTON));
        learn = new JLabel(icon_images.get(GameData.ABOUT_BUTTON));
        history = new JLabel(icon_images.get(GameData.HISTORY_BUTTON));
        help = new JLabel(icon_images.get(GameData.HELP_BUTTON));
        quit = new JLabel(icon_images.get(GameData.QUIT_BUTTON));
        profile = new JLabel(icon_images.get(GameData.PROFILE_BUTTON));  

        profile.addMouseListener(this);
        newGame.addMouseListener(this);
        learn.addMouseListener(this);
        history.addMouseListener(this);
        help.addMouseListener(this);
        quit.addMouseListener(this);
        profile.addMouseListener(this);
        
        JPanel pane = new JPanel(new GridLayout(6,1));
		//buttonHelp.addActionListener(new buttonPressHelp());
		//buttonHelp = new JButton("Help");
        //pane2.add(buttonHelp);
        pane.add(profile);
        pane.add(newGame);        
        pane.add(history);
        pane.add(learn);
        pane.add(help);
        pane.add(quit);             
        pane.setBackground(bg_color);
        JPanel menu_pane = new JPanel(new BorderLayout());
        menu_pane.setBackground(bg_color);
        menu_pane.add(pane,BorderLayout.SOUTH);
        menu_pane.setBorder(BorderFactory.createEmptyBorder(0,20,20,0));
        return menu_pane;
    }
    public void createEastPane(){           
        easternPane = new JPanel(new BorderLayout());
        historyPane = new HistoryBoardPane();                
        
        JPanel pane = new JPanel(new GridLayout(1,4));        
        first = new JLabel(icon_images.get(GameData.FIRST_BUTTON));
        previous = new JLabel(icon_images.get(GameData.PREV_BUTTON));
        next = new JLabel(icon_images.get(GameData.NEXT_BUTTON));
        last = new JLabel(icon_images.get(GameData.LAST_BUTTON));
        
        pane.add(first);
        pane.add(previous);
        pane.add(next);
        pane.add(last);
        
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BoxLayout(pane2,BoxLayout.Y_AXIS));
        pane2.add(historyPane);
        pane2.add(pane);
        
        easternPane.add(pane2,BorderLayout.SOUTH);
        easternPane.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        easternPane.setBackground(bg_color);        
        easternPane.setVisible(false);
        centralPane.add(easternPane,BorderLayout.EAST);
        
        pane.setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
        pane.setBackground(bg_color);
        
        first.addMouseListener(this);
        previous.addMouseListener(this);
        next.addMouseListener(this);
        last.addMouseListener(this);
    }    
    public void newGame(){
    	turnTimer = 0;
    	suggestionLock = 0;
    	EvaluationPane ep = new EvaluationPane();
    	EvaluationPane.setEnterName("<html>Pawn Evaluation: Default<br><br>Bishop Evaluation: Default<br><br>Knight Evaluation: Default<br><br><br>Overall Evaluation: Default</html>");
		ep.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    	ep.setBackground(bg_color);
    	ep.setSize(300,175);
    	ep.setVisible(true);
        levelSelected = levelAndColourSelect.levelSlider.getValue();
    	System.out.println(levelSelected);
    	if(levelSelected < 3) {
	       	HelpPane f = new HelpPane();
	    	f.setHelpTitleLabel("New Game");
	    	f.setHelpLabel("<html>Welcome to Provenance Chess!<br><br>You have started a game versus the Provenance AI,<br>but don't worry you are not alone, <font color = 'green'>press 'help' if<br> you are stuck to get advice.<br>You can also double click friendly pieces to see how they move.<br> or alternatively click the 'learn' button to scroll through all pieces</font><br>To win, Checkmate the opposing King<br>This means you have to attack their king in such a way <br>that he cannot avoid capture.<br><br>Good Luck!</html>");
			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		    Color bg_color = Color.decode("#fcf6ea");
			f.setBackground(bg_color);
			f.setSize(400, 300);  
	    }
        if(!easternPane.isVisible()){
            easternPane.setVisible(true);
            pack();
            setLocationRelativeTo(null);
        }        
        isPlayingWhite = levelAndColourSelect.whiteButton.isSelected();
        castling = false;
        move.sourceLocation = -1;
        move.destination = -1;
        position = new Position();
        position.initialize(isPlayingWhite);
        game = new Game(position);               
        loadPieceImages();
        promotingAPiecePane.setIcons(isPlayingWhite);
        chessboardPane.repaint();
        if(isPlayingWhite) stateOfGame = GameData.HUMAN_MOVE;
        else stateOfGame = GameData.COMPUTER_MOVE;
        previousPositions.clear();
        history_count = 0;
        newHistoryPosition();
        mainMoveSearcher.level = levelAndColourSelect.levelSlider.getValue();
        turnTimer = 0;
        play();
    }
    //static class buttonPressHelp implements ActionListener {
	//	public void actionPerformed(ActionEvent e) {
	//	}
	//} 
    
 
    public void play(){
        Thread t = new Thread(){ // run the move in a thread
        	
            public void run(){
                while(true){           
                    switch(stateOfGame){
                        case GameData.HUMAN_MOVE:  
                        	/*
                        	if(suggestionLock == 0) {
	                            move = mainMoveSearcher.alphaBeta(GameData.HUMAN, position, 
	                                    Integer.MIN_VALUE, Integer.MAX_VALUE,4).lastMove;   
	                            System.out.println(move.source_location);
	                            suggestionPiece = move.source_location;
	                            System.out.println(move.destination);
	                            int piece = position.humanPieces[position.board[suggestionPiece]].value; // get the value of the piece selected
	                            System.out.println(piece);
	                            suggestionMove = move.destination;
	                            suggestionLock = 1;
                        	}// PRIMITIVE
                        	*/
                            break;
                        case GameData.COMPUTER_MOVE:             
                            if(gameEnded(GameData.COMPUTER)){
                            	levelThreshold = calculateLevelThreshold(level);
                            	xp = xp + 300;
                            	if(xp >= levelThreshold) {
                            		level = level + 1;
                            		xp = 0;
                                	JOptionPane.showMessageDialog(null,"Congratulations! You have made it to level " + level,"Level Up",JOptionPane.PLAIN_MESSAGE); 
                            	}
                            	System.out.println(xp);
                                stateOfGame = GameData.GAME_ENDED;
                                break;
                            }
                            move = mainMoveSearcher.alphaBeta(GameData.COMPUTER, position, 
                                    Integer.MIN_VALUE, Integer.MAX_VALUE, levelAndColourSelect.levelSlider.getValue()).lastMove;    
                            stateOfGame = GameData.PREPARE_ANIMATION;                            
                            break;
                        case GameData.PREPARE_ANIMATION:
                            prepareAnimation();
                            break;
                        case GameData.ANIMATING:
                            animate();
                            break;                        
                        case GameData.GAME_ENDED: return;
                    }
                    try{                        
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    public boolean gameEnded(int player){
        int result = game.getResult(player);
        boolean end_game = false;
        String color ="";
        if(player == GameData.COMPUTER){
            color = (isPlayingWhite)?"White":"Black";
        } else color = (isPlayingWhite)?"Black":"White";
        if(result == GameData.CHECKMATE){
            showEndGameResult(color+" wins by CHECKMATE");
            end_game = true;
        }else if(result == GameData.DRAW){
            showEndGameResult("DRAW");
            end_game = true;
        }
        if(game.isChecked(GameData.HUMAN) && result != GameData.CHECKMATE && result != GameData.DRAW) {
        	JOptionPane.showMessageDialog(null,"You have been placed in CHECK.","Check",JOptionPane.PLAIN_MESSAGE); 
        } else if (game.isChecked(GameData.COMPUTER) && result != GameData.CHECKMATE && result != GameData.DRAW) {
        	JOptionPane.showMessageDialog(null,"Opponent placed in CHECK.","Check",JOptionPane.PLAIN_MESSAGE); 
        }
        return end_game;
    }
    public void showEndGameResult(String message){
        int option = JOptionPane.showOptionDialog(null,
                message,"Game Over",0,JOptionPane.PLAIN_MESSAGE,
                null,new Object[]{"Play again","Cancel"},"Play again");
        if(option == 0){
            levelAndColourSelect.setVisible(true);
        }
    }
    public void showNewGameWarning(){
        JOptionPane.showMessageDialog(null,
                "Start a new game after I made my move.\n",
                "Message",JOptionPane.PLAIN_MESSAGE);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if(source == quit){
            quit();
        } else if(source == newGame){
            if(stateOfGame == GameData.COMPUTER_MOVE){
                showNewGameWarning();
                return;
            }
            if(levelAndColourSelect == null) {
                levelAndColourSelect = new PreferencesPane(this);
                mainMoveSearcher = new MoveSearcher(this);
            }
            levelAndColourSelect.setVisible(true);
        } else if(source == learn){
			LearnPane.currentID = 1;
			LearnPane f = new LearnPane(); 
			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			f.setBackground(bg_color);
			f.setSize(600, 400);        		
			f.setVisible(true);        
        } else if(source == history){
            easternPane.setVisible(!easternPane.isVisible());
            pack();
            setLocationRelativeTo(null);
        } else if(source == help) {
        	checkGamePhase();
        	if(levelSelected < 3) {
        		if(gamePhase == "Menu") {
            		HelpPane f = new HelpPane();
                	f.setHelpTitleLabel("Hi!");
                	f.setHelpLabel(Advice.newGameMessage);
					f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        		    Color bg_color = Color.decode("#fcf6ea");
        			f.setBackground(bg_color);
        			f.setSize(400, 200);        		
        			f.setVisible(true);	
            	} else if(gamePhase == "Early Game") {
                	HelpPane f = new HelpPane();
                	f.setHelpTitleLabel("Early Game");
                	f.setHelpLabel(Advice.easyEarlyGameMessage);
        			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        		    Color bg_color = Color.decode("#fcf6ea");
        			f.setBackground(bg_color);
        			f.setSize(450, 450);        		
        			f.setVisible(true);	
            	} else if(gamePhase == "MidGame") {
            		HelpPane f = new HelpPane();
                	f.setHelpTitleLabel("Mid-Game");
                	f.setHelpLabel(Advice.easyMidGameMessage);
        			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        		    Color bg_color = Color.decode("#fcf6ea");
        			f.setBackground(bg_color);
        			f.setSize(450, 450);        		
        			f.setVisible(true);
            	} else if(gamePhase == "EndGame") {
            		HelpPane f = new HelpPane();
                	f.setHelpTitleLabel("End-Game");
                	f.setHelpLabel(Advice.endGameMessage);
        			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        		    Color bg_color = Color.decode("#fcf6ea");
        			f.setBackground(bg_color);
        			f.setSize(450, 300);        		
        			f.setVisible(true);
            	}
        	} else {
	        	if(gamePhase == "Menu") {
            		HelpPane f = new HelpPane();
                	f.setHelpTitleLabel("Hi!");
                	f.setHelpLabel(Advice.newGameMessage);
					f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        		    Color bg_color = Color.decode("#fcf6ea");
        			f.setBackground(bg_color);
        			f.setSize(400, 200);        		
        			f.setVisible(true);	
        		} else if(gamePhase == "Early Game") {
	            	HelpPane f = new HelpPane();
	            	f.setHelpTitleLabel("Early Game");
	            	f.setHelpLabel(Advice.earlyGameMessage);
	            	f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    		    Color bg_color = Color.decode("#fcf6ea");
	    			f.setBackground(bg_color);
	    			f.setSize(450, 400);        		
	    			f.setVisible(true);	
	        	} else if(gamePhase == "MidGame") {
	        		HelpPane f = new HelpPane();
	            	f.setHelpTitleLabel("Mid-Game");
	            	f.setHelpLabel(Advice.midGameMessage);
	    			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    		    Color bg_color = Color.decode("#fcf6ea");
	    			f.setBackground(bg_color);
	    			f.setSize(450, 400);        		
	    			f.setVisible(true);
	        	} else if(gamePhase == "EndGame") {
	        		HelpPane f = new HelpPane();
	            	f.setHelpTitleLabel("End-Game");
                	f.setHelpLabel(Advice.endGameMessage);
	    			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    		    Color bg_color = Color.decode("#fcf6ea");
	    			f.setBackground(bg_color);
	    			f.setSize(450, 300);        		
	    			f.setVisible(true);
	        	}
        	}
        } else if(source == first){
            history_count = 0;
            historyPane.repaint();            
        } else if(source == previous){
            if(history_count>0){
                history_count--;
                historyPane.repaint();
            }
        } else if(source == next){
            if(history_count<previousPositions.size()-1){
                history_count++;
                historyPane.repaint();
            }
        } else if(source == last){
            history_count = previousPositions.size()-1;
            historyPane.repaint();
        } else if(source == profile) {
        	ProfilePane f = new ProfilePane();
        	if(level<5) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Beginner</html>");
        	} else if(level < 10) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Bronze</html>");
        	} else if(level < 15) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Silver</html>");
        	} else if(level < 20) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Gold</html>");
        	} else if(level < 25) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Platinum</html>");
        	} else if(level < 30) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Diamond</html>");
        	} else if(level < 35) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Master</html>");
        	} else if(level < 50) {
            	f.setLevel("<html>" + userName + "<br>Level: " + level+"<br>Rank: Grandmaster</html>");
        	}
        	levelThreshold = calculateLevelThreshold(level);
        	f.setXP("Experience: " + xp + "/" + levelThreshold);
			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		    Color bg_color = Color.decode("#fcf6ea");
			f.setBackground(bg_color);
			f.setSize(250, 250);        		
			f.setVisible(true);
        }
    }    
	@Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if(source == newGame){
            newGame.setIcon(icon_images.get(GameData.NEW_BUTTON2));
        } else if(source == profile) {
        	profile.setIcon(icon_images.get(GameData.PROFILE_BUTTON2));
        } else if(source == learn){
            learn.setIcon(icon_images.get(GameData.ABOUT_BUTTON2));
        } else if(source == history){
            history.setIcon(icon_images.get(GameData.HISTORY_BUTTON2));
        } else if(source == quit){
            quit.setIcon(icon_images.get(GameData.QUIT_BUTTON2));
        } else if(source == help) {
        	help.setIcon(icon_images.get(GameData.HELP_BUTTON2));
        } else if(source == first){
            first.setIcon(icon_images.get(GameData.FIRST_BUTTON2));
        } else if(source == previous){
            previous.setIcon(icon_images.get(GameData.PREV_BUTTON2));
        } else if(source == next){
            next.setIcon(icon_images.get(GameData.NEXT_BUTTON2));
        } else if(source == last){
            last.setIcon(icon_images.get(GameData.LAST_BUTTON2));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if(source == newGame){
            newGame.setIcon(icon_images.get(GameData.NEW_BUTTON));
        } else if(source == profile) {
        	profile.setIcon(icon_images.get(GameData.PROFILE_BUTTON));
        } else if(source == learn){
            learn.setIcon(icon_images.get(GameData.ABOUT_BUTTON));
        } else if(source == history){
            history.setIcon(icon_images.get(GameData.HISTORY_BUTTON));
        } else if(source == help) {
        	help.setIcon(icon_images.get(GameData.HELP_BUTTON));
    	} else if(source == quit){
            quit.setIcon(icon_images.get(GameData.QUIT_BUTTON));
        } else if(source == first){
            first.setIcon(icon_images.get(GameData.FIRST_BUTTON));
        } else if(source == previous){
            previous.setIcon(icon_images.get(GameData.PREV_BUTTON));
        } else if(source == next){
            next.setIcon(icon_images.get(GameData.NEXT_BUTTON));
        } else if(source == last){
            last.setIcon(icon_images.get(GameData.LAST_BUTTON));
        }
    }
    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {}
    public class ChessBoardPane extends JPanel implements MouseListener{     
        Image animating_image;
        int movingX,movingY,desX,desY,deltaX,deltaY;
        public ChessBoardPane(){
            setPreferredSize(new Dimension(450,495));
            setBackground(bg_color);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
        	//21 22 23 24 25 26 27 28
        	//31 32 33 34 35 36 37 38
        	//41 42 43 44 45 46 47 48
        	//51 52 53 54 55 56 57 58
        	//61 62 63 64 65 66 67 68
        	//71 72 73 74 75 76 77 78
        	//81 82 83 84 85 86 87 88 
        	//91 92 93 94 95 96 97 98
            if(position.board == null) return;
            super.paintComponent(g);  
            
            g.drawImage(images.get(GameData.MAINTITLE),20,26,this);
            g.drawImage(images.get(GameData.BOARD_IMAGE),20,65,this);       
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] == GameData.ILLEGAL) continue;                                                                
                int x = i%10;
                int y = (i-x)/10;
                
                if (isPieceSelected && i == move.sourceLocation) {                
                    g.drawImage(images.get(GameData.GLOW), x * 45, y * 45,this); // show the select border around the current piece
                    int piece = position.humanPieces[position.board[i]].value; // get the value of the piece selected
                    // DEBUGGING System.out.println(piece);
                    int xPos = x * 45;
                    int yPos = y * 45;
                    final int squareSize = 45;
                    if(piece == 100) { // if the piece is a pawn
                    	if(validMoveCheck(lastClickLocation-10)) { // up one square
                            g.drawImage(images.get(GameData.GLOW), xPos, yPos - squareSize,this); // show where the pawn can move
                    	}
                    	if(validMoveCheck(lastClickLocation-20)) { // up two squares if not moved
                            g.drawImage(images.get(GameData.GLOW), xPos, yPos - squareSize - squareSize,this); // show where the pawn can move
                    	} 
                    	if(validMoveCheck(lastClickLocation-9)) { // up and to the right
                            g.drawImage(images.get(GameData.GLOW), xPos + squareSize, yPos - squareSize,this); // show where the pawn can move
                    	}
                    	if(validMoveCheck(lastClickLocation-11)) { // up and to the left
                            g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos - squareSize,this); // show where the pawn can move
                    	}
                    } else if (piece == 320) {
                    	if(validMoveCheck(lastClickLocation-21)) { // x1
                        	g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos - ( 2 * squareSize), this);
                    	}
                    	if(validMoveCheck(lastClickLocation-19)) { // x2
                        	g.drawImage(images.get(GameData.GLOW), xPos + squareSize, yPos - ( 2 * squareSize), this);
                    	}
                    	if(validMoveCheck(lastClickLocation-12)) { // x3
                        	g.drawImage(images.get(GameData.GLOW), xPos - (2 * squareSize), yPos - squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-8)) { // x4
                        	g.drawImage(images.get(GameData.GLOW), xPos + (2 * squareSize), yPos - squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+8)) { // x5
                        	g.drawImage(images.get(GameData.GLOW), xPos - (2 * squareSize), yPos + squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+12)) { // x6
                        	g.drawImage(images.get(GameData.GLOW), xPos + (2 * squareSize), yPos + squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+19)) { // x7 {
                        	g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos + (2 * squareSize), this);
                    	}
                    	if(validMoveCheck(lastClickLocation+21)) { // x8
                        	g.drawImage(images.get(GameData.GLOW), xPos + squareSize, yPos + (2 * squareSize), this);
                    	}
                    	System.out.println("Knight"); // if the piece is a knight, 21, 19, 12, 8
                    	//    x1    x2
                    	// x3          x4
                    	//       Y
                    	// x5          x6
                    	//    x7    x8
                    	// 8 potential places to move to

                    } else if (piece == 325) { // if the piece is a bishop, -11 x1, -9 x2, +9 x3, + 11 x4
                    	for(int j = 1; j <= 8; j = j + 1) { // x1
                    		int source = move.sourceLocation;
                       	 	int topLeft = source-(j*11);
                       	 	if(topLeft < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[topLeft];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(topLeft) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x2
                    		int source = move.sourceLocation;
                       	 	int topRight = source-(j*9);
                       	 	if(topRight < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[topRight];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(topRight) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x3
                    		int source = move.sourceLocation;
                       	 	int bottomLeft = source+(j*9);
                       	 	if(bottomLeft > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[bottomLeft];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(bottomLeft) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos + (j * squareSize), this);
	                            }
	                    	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { //x4
                    		int source = move.sourceLocation;
                       	 	int bottomRight = source+(j*11);
                       	 	if(bottomRight > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[bottomRight];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(bottomRight) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos + (j * squareSize), this);
	                            }
                       	 	}
                    	}
                   	 	
                    	System.out.println("Bishop");
                    	
                    	// x           x
                    	//   x       x
                    	//     x1  X2
                    	//       y
                    	//     x3  x4 
                    	//   x       x
                    	// x           x
                        // varying places to move to
                    	// 7 max in any diagonal
                    } else if (piece == 500) { // if the piece is a rook,
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x2 LEFT
                    		int source = move.sourceLocation;
                       	 	int left = source-(j*1);
                       	 	if(left < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos, this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x1 RIGHT
                    		int source = move.sourceLocation;
                       	 	int left = source+(j*1);
                       	 	if(left > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos, this);
	                            }
                       	 	}
                    	}
                    	for(int j = 1; j <= 8; j = j + 1) { // x3 UP
                    		int source = move.sourceLocation;
                       	 	int left = source-(j*10);
                       	 	if(left < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos, yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	for(int j = 1; j <= 8; j = j + 1) { // x4 DOWN
                    		int source = move.sourceLocation;
                       	 	int left = source+(j*10);
                       	 	if(left > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos, yPos + (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	//        x
                    	//        x
                    	//        x3
                    	// x x x2 y x1 x x
                    	//        x4
                    	//        x
                    	//        x
                    	
                    } else if (piece == 900) { // if the piece is a queen
                      	// x     x     x
                    	//   x   x   x
                    	//     x x x
                    	// x x x y x x x
                    	//     x x x 
                    	//   x   x   x
                    	// x     x     x
                    	for(int j = 1; j <= 8; j = j + 1) { // x1
                    		int source = move.sourceLocation;
                       	 	int topLeft = source-(j*11);
                       	 	if(topLeft < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[topLeft];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(topLeft) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x2
                    		int source = move.sourceLocation;
                       	 	int topRight = source-(j*9);
                       	 	if(topRight < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[topRight];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(topRight) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x3
                    		int source = move.sourceLocation;
                       	 	int bottomLeft = source+(j*9);
                       	 	if(bottomLeft > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[bottomLeft];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(bottomLeft) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos + (j * squareSize), this);
	                            }
	                    	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { //x4
                    		int source = move.sourceLocation;
                       	 	int bottomRight = source+(j*11);
                       	 	if(bottomRight > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[bottomRight];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(bottomRight) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos + (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x2 LEFT
                    		int source = move.sourceLocation;
                       	 	int left = source-(j*1);
                       	 	if(left < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos - (j * squareSize), yPos, this);
	                            }
                       	 	}
                    	}
                    	
                    	for(int j = 1; j <= 8; j = j + 1) { // x1 RIGHT
                    		int source = move.sourceLocation;
                       	 	int left = source+(j*1);
                       	 	if(left > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos + (j * squareSize), yPos, this);
	                            }
                       	 	}
                    	}
                    	for(int j = 1; j <= 8; j = j + 1) { // x3 UP
                    		int source = move.sourceLocation;
                       	 	int left = source-(j*10);
                       	 	if(left < 21) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos, yPos - (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	for(int j = 1; j <= 8; j = j + 1) { // x4 DOWN
                    		int source = move.sourceLocation;
                       	 	int left = source+(j*10);
                       	 	if(left > 98) {
                       	 		
                       	 	} else {
	                            int destinationSquareTopLeft = position.board[left];
	                            if(destinationSquareTopLeft == GameData.EMPTY && validMoveCheck(left) == true) {
	                            	g.drawImage(images.get(GameData.GLOW), xPos, yPos + (j * squareSize), this);
	                            }
                       	 	}
                    	}
                    	
                    } else if (piece == 1000000) { // if King
                    	if(validMoveCheck(lastClickLocation+1)) { //x1
                        	g.drawImage(images.get(GameData.GLOW), xPos+squareSize, yPos, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+2)) { //x1
                        	g.drawImage(images.get(GameData.GLOW), xPos+squareSize+squareSize, yPos, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-2)) { //x1
                        	g.drawImage(images.get(GameData.GLOW), xPos-squareSize-squareSize, yPos, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-9)) { //x2
                        	g.drawImage(images.get(GameData.GLOW), xPos+squareSize, yPos - squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-10)) { //x3
                        	g.drawImage(images.get(GameData.GLOW), xPos, yPos - squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-11)) { //x4
                        	g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos - squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation-1)) { //x5
                        	g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+9)) { //x6
                        	g.drawImage(images.get(GameData.GLOW), xPos - squareSize, yPos+squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+10)) { //x7
                        	g.drawImage(images.get(GameData.GLOW), xPos, yPos+squareSize, this);
                    	}
                    	if(validMoveCheck(lastClickLocation+11)) { //x8
                        	g.drawImage(images.get(GameData.GLOW), xPos + squareSize, yPos + squareSize, this);
                    	}
                      	//           
                    	//          
                    	//     x4x3x2
                    	//     x5y x1
                    	//     x6x7x8 
                    	//          
                    	//            
                    	
                    }
                } else if(!isPieceSelected && move.destination == i && (position.board[i]==GameData.EMPTY || position.board[i]<0)) {
                    g.drawImage(images.get(GameData.GLOW2), x * 45, y * 45, this);                                        
                }
                
                if (position.board[i] == GameData.EMPTY) continue;
                
                if(stateOfGame == GameData.ANIMATING && i==move.sourceLocation) continue;
                if (position.board[i] > 0) {          // HERE
                    int piece = position.humanPieces[position.board[i]].value;
                    g.drawImage(images.get(piece),x*45,y*45,this);
                }else{
                    int piece = position.computerPieces[-position.board[i]].value;
                    g.drawImage(images.get(-piece),x*45,y*45,this);
                }               
            }  
            if(stateOfGame == GameData.ANIMATING){
                g.drawImage(animating_image,movingX,movingY,this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(stateOfGame != GameData.HUMAN_MOVE) return; // if it's not the player's turn just do nothing
            int location = boardValue(e.getY())*10+boardValue(e.getX()); // get the location of where they clicked
            lastClickLocation = location;
            System.out.println("LOCATION: " + location);
            if(position.board[location] == GameData.ILLEGAL) return; // if it's outside of the gameboard then do nothing
            if((!isPieceSelected || position.board[location]>0) && position.board[location] != GameData.EMPTY){ // if it's a valid click and not selected a piece already
                if(position.board[location]>0){ 
                    isPieceSelected = true; // set the piece selected flag to true
                    move.sourceLocation = location;
                    if(e.getClickCount() % 2 == 0 && !e.isConsumed()) {
                    	e.consume();
                    	int piece_value = position.humanPieces[position.board[move.sourceLocation]].value;
                    	if(piece_value == 100) {
                			LearnPane.currentID = 1;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);       
                			f.setVisible(true);    
                    	} else if(piece_value == 320) {
                			LearnPane.currentID = 2;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);        
                			f.setVisible(true);    
                    	} else if(piece_value == 325) {
                    		LearnPane.currentID = 3;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);        
                			f.setVisible(true);    
                    	} else if(piece_value == 500) {
                    		LearnPane.currentID = 4;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);        
                			f.setVisible(true);    
                    	} else if(piece_value == 900) {
                    		LearnPane.currentID = 5;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);        
                			f.setVisible(true);    
                    	} else if(piece_value > 1000) {
                    		LearnPane.currentID = 6;
                    		LearnPane f = new LearnPane(); 
                			f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                			f.setBackground(bg_color);
                			f.setSize(600, 400);        
                			f.setVisible(true);    
                    	}
                    }
                }
            }else if(isPieceSelected && validMove(location)){
            	if(turnTimer >= 1) {
            		turnTimer = turnTimer + 2;
            	} else {
                    turnTimer = turnTimer + 1;
            	}
                System.out.println("TURN: " + turnTimer); // advance the turn timer
                isPieceSelected = false;
                move.destination = location;     
            	EFF.evaluate(position);
                stateOfGame = GameData.PREPARE_ANIMATION;
            }
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }
    public class HistoryBoardPane extends JPanel{
        public HistoryBoardPane(){
            setBackground(bg_color);
            setPreferredSize(new Dimension(300,330));            
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(images.get(GameData.HISTORY_TITLE),20,15,this);
            g.drawImage(images.get(GameData.BOARD_IMAGE2),14,44,this);
            if(previousPositions.size()<=0) return;
            Position _position = previousPositions.get(history_count);
            for(int i=0; i<_position.board.length -11; i++){
                if(_position.board[i] == GameData.EMPTY) continue;
                if(_position.board[i] == GameData.ILLEGAL) continue;
                int x = i%10;
                int y = (i-x)/10;
                if (_position.board[i] > 0) {          
                    int piece = _position.humanPieces[_position.board[i]].value;
                    g.drawImage(images.get(piece+10),x*30,y*30,this);
                }else{
                    int piece = _position.computerPieces[-_position.board[i]].value;
                    g.drawImage(images.get(-piece+10),x*30,y*30,this);
                }
            }
        }
    }
    public boolean validMoveCheck(int destination) {
    	 int source = move.sourceLocation;
         int destination_square = position.board[destination];
         if(destination_square == GameData.ILLEGAL) return false;
         if(!game.safeMove(GameData.HUMAN,source,destination)) return false;
         boolean valid = false;
         int piece_value = position.humanPieces[position.board[source]].value;      // get what piece they have selected                   
         switch(piece_value){
             case Piece.PAWN:
                 if(destination == source-10 && destination_square == GameData.EMPTY) valid = true;
                 if(destination == source-20 && position.board[source-10] == GameData.EMPTY &&
                         destination_square == GameData.EMPTY && source>80) valid = true;
                 if(destination == source-9 && destination_square<0) valid = true;
                 if(destination == source-11 && destination_square<0) valid = true;
                 break;
             case Piece.KNIGHT:
             case Piece.KING:
                 if(piece_value == Piece.KING) valid = checkCastling(destination);
                 int[] destinations = null;
                 if(piece_value == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                     source-12,source+12,source-8,source+8};
                 else destinations = new int[]{source+1,source-1,source+10,source-10,
                     source+11,source-11,source+9,source-9};
                 for(int i=0; i<destinations.length; i++){
                     if(destinations[i] == destination){
                         if(destination_square == GameData.EMPTY || destination_square<0){
                             valid = true;
                             break;
                         }
                     }
                 }                
                 break;
             case Piece.BISHOP:
             case Piece.ROOK:
             case Piece.QUEEN:
                 int[] deltas = null;
                 if(piece_value == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                 if(piece_value == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                 if(piece_value == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                 for (int i = 0; i < deltas.length; i++) {
                     int des = source + deltas[i]; 
                     valid = true;
                     while (destination != des) { 
                         destination_square = position.board[des];  
                         if(destination_square != GameData.EMPTY){
                             valid = false;
                             break;
                         }                        
                         des += deltas[i];
                     }
                     if(valid) break;
                 }
                 break;
         }
         return valid;
    }
    public boolean validMove(int destination){
        int source = move.sourceLocation;
        int destination_square = position.board[destination];
        if(destination_square == GameData.ILLEGAL) return false;
        if(!game.safeMove(GameData.HUMAN,source,destination)) return false;
        boolean valid = false;
        int piece_value = position.humanPieces[position.board[source]].value;      // get what piece they have selected                   
        switch(piece_value){
            case Piece.PAWN:
                if(destination == source-10 && destination_square == GameData.EMPTY) valid = true;
                if(destination == source-20 && position.board[source-10] == GameData.EMPTY &&
                        destination_square == GameData.EMPTY && source>80) valid = true;
                if(destination == source-9 && destination_square<0) valid = true;
                if(destination == source-11 && destination_square<0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                if(piece_value == Piece.KING) valid = checkCastling(destination);
                int[] destinations = null;
                if(piece_value == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(destination_square == GameData.EMPTY || destination_square<0){
                            valid = true;
                            break;
                        }
                    }
                }                
                break;
            case Piece.BISHOP:
            case Piece.ROOK:
            case Piece.QUEEN:
                int[] deltas = null;
                if(piece_value == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                if(piece_value == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                if(piece_value == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                for (int i = 0; i < deltas.length; i++) {
                    int des = source + deltas[i]; 
                    valid = true;
                    while (destination != des) { 
                        destination_square = position.board[des];  
                        if(destination_square != GameData.EMPTY){
                            valid = false;
                            break;
                        }                        
                        des += deltas[i];
                    }
                    if(valid) break;
                }
                break;
        }
        return valid;
    }
    public void checkGamePhase() {
    	if(turnTimer == 999) {
    		gamePhase = "Menu";
    	} else if(turnTimer < 7) {
    		gamePhase = "Early Game";
    	} else if(turnTimer >= 7 && turnTimer < 40 || castling == true) {
    		gamePhase = "MidGame";
    	} else if(turnTimer >= 40) {
    		gamePhase = "EndGame";
    	}    	
    }
    public boolean checkCastling(int destination){        
        Piece king = position.humanPieces[8];
        Piece right_rook = position.humanPieces[6];
        Piece left_rook = position.humanPieces[5];
        
        if(king.hasMoved) return false;              
        int source = move.sourceLocation;
        
        if(right_rook == null && left_rook == null) return false;
        if(right_rook != null && right_rook.hasMoved && 
                left_rook != null && left_rook.hasMoved) return false;
            
        if(isPlayingWhite){            
            if(source != 95) return false;            
            if(destination != 97 && destination != 93) return false;
            if(destination == 97){
                if(position.board[96] != GameData.EMPTY) return false;
                if(position.board[97] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,96)) return false;
                if(!game.safeMove(GameData.HUMAN,source,97)) return false;
            }else if(destination == 93){
                if(position.board[94] != GameData.EMPTY) return false;
                if(position.board[93] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,94)) return false;
                if(!game.safeMove(GameData.HUMAN,source,93)) return false;
            }
        }else{
            if(source != 94) return false;            
            if(destination != 92 && destination != 96) return false;
            if(destination == 92){
                if(position.board[93] != GameData.EMPTY) return false;
                if(position.board[92] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,93)) return false;
                if(!game.safeMove(GameData.HUMAN,source,92)) return false;
            }else if(destination == 96){
                if(position.board[95] != GameData.EMPTY) return false;
                if(position.board[96] != GameData.EMPTY) return false;
                if(!game.safeMove(GameData.HUMAN,source,95)) return false;
                if(!game.safeMove(GameData.HUMAN,source,96)) return false;
            }
        }        
        return castling=true;
    }
    public int boardValue(int value){
        return value/45;
    }
    public void prepareAnimation(){
        int imageToAnimateKey = 0;
        if(position.board[move.sourceLocation]>0){
        	int a = move.sourceLocation;
        	int b = position.board[a];
            imageToAnimateKey = position.humanPieces[b].value;
        } else {
            imageToAnimateKey = -position.computerPieces[-position.board[move.sourceLocation]].value;
        }        
        chessboardPane.animating_image = images.get(imageToAnimateKey);
        int x = move.sourceLocation%10;        
        int y = (move.sourceLocation-x)/10;
        chessboardPane.desX = move.destination%10;
        chessboardPane.desY = (move.destination-chessboardPane.desX)/10;
        int dX = chessboardPane.desX-x;
        int dY = chessboardPane.desY-y;           
        chessboardPane.movingX = x*45;
        chessboardPane.movingY = y*45;
        if(Math.abs(dX)>Math.abs(dY)){
            if(dY == 0){
                chessboardPane.deltaX = (dX>0)?1:-1;
                chessboardPane.deltaY = 0;
            }else{
                chessboardPane.deltaX = (dX>0)?Math.abs(dX/dY):-(Math.abs(dX/dY));
                chessboardPane.deltaY = (dY>0)?1:-1;
            }
        }else{
            if(dX == 0){
                chessboardPane.deltaY = (dY>0)?1:-1;
                chessboardPane.deltaX = 0;
            }else{
                chessboardPane.deltaX = (dX>0)?1:-1;
                chessboardPane.deltaY = (dY>0)?Math.abs(dY/dX):-(Math.abs(dY/dX));
            }
        }          
        stateOfGame = GameData.ANIMATING;
    }
    public void animate(){
        if (chessboardPane.movingX == chessboardPane.desX * 45 && chessboardPane.movingY == chessboardPane.desY * 45) {                                           
            chessboardPane.repaint();            
            int source_square = position.board[move.sourceLocation];            
            if(source_square>0){                
                stateOfGame = GameData.COMPUTER_MOVE;                                               
            }else {
                if(move.destination > 90 && move.destination<98 
                        && position.computerPieces[-source_square].value == Piece.PAWN)
                    promoteComputerPawn();
                stateOfGame = GameData.HUMAN_MOVE;
            }                        
            position.update(move);       
            if(source_square>0){
                if(castling){   
                    prepareCastlingAnimation();
                      stateOfGame = GameData.PREPARE_ANIMATION;
                }else if(move.destination > 20 && move.destination < 29 && 
                        position.humanPieces[source_square].value == Piece.PAWN){
                    promoteHumanPawn();                    
                }
            }else{
                if (gameEnded(GameData.HUMAN)) {
                    stateOfGame = GameData.GAME_ENDED;
                    return;
                }
            }
            if(!castling && stateOfGame != GameData.PROMOTING) 
                newHistoryPosition();
            if(castling) castling = false;     
        }
        chessboardPane.movingX += chessboardPane.deltaX;
        chessboardPane.movingY += chessboardPane.deltaY;
        chessboardPane.repaint();
    }
    public void promoteHumanPawn(){        
        promotingAPiecePane.location = move.destination;
        promotingAPiecePane.index = position.board[move.destination];
        promotingAPiecePane.setVisible(true);
        stateOfGame = GameData.PROMOTING;
    }
    public void promoteComputerPawn(){
        int piece_index = position.board[move.sourceLocation];
        position.computerPieces[-piece_index] = new Piece(Piece.QUEEN,move.destination);
    }
    public void prepareCastlingAnimation(){
        if(move.destination == 97 || move.destination == 96){
            move.sourceLocation = 98;
            move.destination -= 1;
        }else if(move.destination == 92 || move.destination == 93){
            move.sourceLocation = 91;
            move.destination += 1;
        }
    }
    public void newHistoryPosition(){        
        previousPositions.add(new Position(position));
        history_count = previousPositions.size()-1;
        historyPane.repaint();
    }
    public void loadPieceImages(){
        char[] resource_keys = {'p','n','b','r','q','k'};
        int[] images_keys = {Piece.PAWN,Piece.KNIGHT,Piece.BISHOP,Piece.ROOK,Piece.QUEEN,Piece.KING};
        try{
            for(int i=0; i<resource_keys.length; i++){             
                images.put(images_keys[i],ImageIO.read(resource.getResource((isPlayingWhite?"w":"b")+resource_keys[i])));
                images.put(-images_keys[i],ImageIO.read(resource.getResource((isPlayingWhite?"b":"w")+resource_keys[i])));   
                images.put(images_keys[i]+10,ImageIO.read(resource.getResource((isPlayingWhite?"w":"b")+resource_keys[i]+'2')));
                images.put(-images_keys[i]+10,ImageIO.read(resource.getResource((isPlayingWhite?"b":"w")+resource_keys[i]+'2'))); 
            }               
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }
    public void loadBoardImages(){
        try{ 
            images.put(GameData.BOARD_IMAGE,ImageIO.read(resource.getResource("chessboard")));
            images.put(GameData.BOARD_IMAGE2,ImageIO.read(resource.getResource("history_board")));
            images.put(GameData.GLOW,ImageIO.read(resource.getResource("glow")));
            images.put(GameData.GLOW2,ImageIO.read(resource.getResource("glow2")));            
            images.put(GameData.HISTORY_TITLE,ImageIO.read(resource.getResource("history_title")));
            images.put(GameData.MAINTITLE,ImageIO.read(resource.getResource("provenance")));
        }catch(IOException ex){
            ex.printStackTrace();
        }        
    }
    public void loadMenuIcons(){
        icon_images.put(GameData.NEW_BUTTON,new ImageIcon(resource.getResource("new_game")));
        icon_images.put(GameData.NEW_BUTTON2,new ImageIcon(resource.getResource("new_game_hover")));
        icon_images.put(GameData.QUIT_BUTTON,new ImageIcon(resource.getResource("quit")));
        icon_images.put(GameData.QUIT_BUTTON2,new ImageIcon(resource.getResource("quit_hover")));
        icon_images.put(GameData.HISTORY_BUTTON,new ImageIcon(resource.getResource("history")));
        icon_images.put(GameData.HISTORY_BUTTON2,new ImageIcon(resource.getResource("history_hover")));
        icon_images.put(GameData.HELP_BUTTON,new ImageIcon(resource.getResource("help")));
        icon_images.put(GameData.HELP_BUTTON2,new ImageIcon(resource.getResource("help_hover")));
        icon_images.put(GameData.ABOUT_BUTTON,new ImageIcon(resource.getResource("about")));
        icon_images.put(GameData.ABOUT_BUTTON2,new ImageIcon(resource.getResource("about_hover")));
        icon_images.put(GameData.PROFILE_BUTTON, new ImageIcon(resource.getResource("profile")));
        icon_images.put(GameData.PROFILE_BUTTON2, new ImageIcon(resource.getResource("profile_hover")));
        icon_images.put(GameData.FIRST_BUTTON,new ImageIcon(resource.getResource("first")));
        icon_images.put(GameData.FIRST_BUTTON2,new ImageIcon(resource.getResource("first_hover")));
        icon_images.put(GameData.NEXT_BUTTON,new ImageIcon(resource.getResource("next")));
        icon_images.put(GameData.NEXT_BUTTON2,new ImageIcon(resource.getResource("next_hover")));
        icon_images.put(GameData.PREV_BUTTON,new ImageIcon(resource.getResource("previous")));
        icon_images.put(GameData.PREV_BUTTON2,new ImageIcon(resource.getResource("previous_hover")));
        icon_images.put(GameData.LAST_BUTTON,new ImageIcon(resource.getResource("last")));
        icon_images.put(GameData.LAST_BUTTON2,new ImageIcon(resource.getResource("last_hover")));
    }
    public void quit(){
    	try {
        	saveProgress();
            int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
                    "Provenance Chess 1.0", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(option == JOptionPane.YES_OPTION)
            System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    	} finally  {
            int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
                    "Provenance Chess 1.0", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(option == JOptionPane.YES_OPTION)
            System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    	}
    }
    public void saveProgress() {
    	final String separator = ",";
    	String input = userName + separator + level + separator + xp;
    	FileWriter fileWriter = null;
    	try {
    		fileWriter = new FileWriter("src/userInfo/user.csv");
    		fileWriter.append(input);
        	System.out.println("csv file saved");
    	} catch (Exception e) {
    		System.out.println("error writing to csv");
    		e.printStackTrace();
    	} finally {
    		try {
    			fileWriter.flush();
    			fileWriter.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    } 
    public static int convertStringToInt(String input) {
    	int integerToReturn = Integer.parseInt(input);
    	return integerToReturn;
    }
    public static int calculateLevelThreshold(int level) {
    	final int scalar = 950;
    	double levelCalculation = Math.pow(0.8, (double) level);
    	int threshold = (int) (1000 - ((levelCalculation)*(double)scalar));
    	return threshold;
    }

    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
    	//PiecesDAO em1 = new PiecesDAO();
    	//PiecesDataClass example1 = new PiecesDataClass("1", "Pawn", "1", "Vertical Forward only (Possibly two spaces on first move)", "N/A");
    	//em1.insertPiece(example1);
    	//System.out.println(calculateLevelThreshold(20));
    	String csvFile = "src/userInfo/user.csv";
    	BufferedReader br = null;
    	String line = "";
    	String separator = ",";
    	try {
    		br = new BufferedReader(new FileReader(csvFile));
    		while ((line = br.readLine()) != null) {
    			// use comma as the separator
    			String userInfo[] = line.split(separator);
    			System.out.println("name = " + userInfo[0] + " level = " + userInfo[1] + " XP = " + userInfo[2]);
    			userName = userName + userInfo[0];
    			int userLevel = convertStringToInt(userInfo[1]);
    			int userXP = convertStringToInt(userInfo[2]);
    			level = userLevel;
    			xp = userXP;
    		}
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if(br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                try{
                    boolean nimbusFound = false;
                        for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                            if(info.getName().equals("Nimbus")){
                                UIManager.setLookAndFeel(info.getClassName());
                                nimbusFound = true;
                                break;
                            }
                        }
                        if(!nimbusFound){
                            int option = JOptionPane.showConfirmDialog(null,
                                    "Nimbus Look And Feel not found\n"+
                                    "Do you want to proceed?",
                                    "Warning",JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                            if(option == JOptionPane.NO_OPTION){
                                System.exit(0);
                            }
                        }
                    ChessGame mcg = new ChessGame();
                   // mcg.pack();
                    mcg.setLocationRelativeTo(null);
                    mcg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mcg.setResizable(false);
                    mcg.setVisible(true); 
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, e.getStackTrace());
                    e.printStackTrace();
                }
            }
        });
    }
}
