package chessGame;

/**
 *
 * @author 15072935 Vincenzo Scialpi-Sullivan
 */
public class EvaluatorForFeedback {   // evaluate a chess position, if it is more advantageous to the human or the computer
    public int evaluate(Position position){        
        int humanScore = 0;
        int computerScore = 0;
        EvaluationPane.pawnEval = 0;
        EvaluationPane.bishopEval = 0;
        EvaluationPane.knightEval = 0;
        EvaluationPane.overallEval = 0; //reset the display
        for(int i=1; i<position.humanPieces.length; i++){
            if(position.humanPieces[i] != null){
                Piece piece = position.humanPieces[i];
                int value = piece.value;
                humanScore += value;
                int location = piece.location;
                int col = (location % 10)-1;
                int row = ((location-col)/10)-2;
                switch(value){
                    case Piece.PAWN: 
                        if(row<4) humanScore+=(8-row);
                        EvaluationPane.pawnEval += 8-row;
                        if(col>4){
                            humanScore -= ((col-4));
                            EvaluationPane.pawnEval += col-4;
                        } else if(col<3){
                        	EvaluationPane.pawnEval -= 3-col;
                            humanScore -= ((3-col));
                        }
                        if(col>1 && col<6 && row>1){ humanScore +=2; EvaluationPane.pawnEval += 2;}
                        if(row == 0) { humanScore += Piece.QUEEN; EvaluationPane.pawnEval += Piece.QUEEN; }
                        //human_score += GameData.HUMAN_PAWN_TABLE[piece.location];
                        break;
                    case Piece.KNIGHT: 
                        if(row == 7) { humanScore -=1; EvaluationPane.knightEval -= 1; }
                        if(col == 0 || col == 7) { humanScore -= 1; EvaluationPane.knightEval -= 1; }
                        if(col>1 && col<6 && row>1 && row<6) { humanScore +=1; EvaluationPane.knightEval += 1; }
                        //human_score += GameData.HUMAN_KNIGHT_TABLE[piece.location];                        
                        break;
                    case Piece.BISHOP: 
                        if(row == 7) { humanScore-=1; EvaluationPane.bishopEval -=1; }
                        if(col == 0 || col == 7){ humanScore -= 1; EvaluationPane.bishopEval -=1;}
                        if(col>0 && col<7 && row>0 && row<7) { humanScore +=1; EvaluationPane.bishopEval +=1; }
                        //human_score += GameData.HUMAN_BISHOP_TABLE[piece.location];
                        break;
                }
            }
            if(position.computerPieces[i] != null){
                Piece piece = position.computerPieces[i];
                int value = piece.value;
                computerScore += value;
                int location = piece.location;
                int col = (location % 10)-1;
                int row = ((location-col)/10)-2;
                switch(value){
                    case Piece.PAWN: 
                        if(row>3)computerScore+=row;
                        if(col>4){
                            computerScore -= ((col-4));
                        }else if(col<3){
                            computerScore -= ((3-col));
                        }
                        if(col>1 && col<6 && row>1) computerScore +=2;
                        if(row == 7) computerScore += Piece.QUEEN;
                        //computer_score += GameData.COMPUTER_PAWN_TABLE[piece.location];
                        break;
                    case Piece.KNIGHT: 
                        if(row == 0) computerScore-=1;
                        if(col == 0 || col == 7) computerScore -= 1;
                        if(col>1 && col<6 && row>1 && row<6) computerScore +=1;
                        //computer_score += GameData.COMPUTER_KNIGHT_TABLE[piece.location];
                        break;
                    case Piece.BISHOP: 
                        if(row == 0) computerScore-=1;
                        if(col == 0 || col == 7) computerScore -= 1;
                        if(col>0 && col<7 && row>0 && row<7) computerScore +=1;
                        //computer_score += GameData.COMPUTER_BISHOP_TABLE[piece.location];
                        break;
                }
            }
        }
        String info = "";
        EvaluationPane.overallEval = humanScore - computerScore;
        scaleDisplayValues();
        if(EvaluationPane.pawnEval < 0) {
        	info = "<html><font color = 'red'>Pawn Evaluation: " + EvaluationPane.pawnEval;
        } else {
        	info = "<html><font color = 'green'>Pawn Evaluation: " + EvaluationPane.pawnEval;
        }
        
        if(EvaluationPane.bishopEval < 0) {
        	info = info + "<br><br><font color = 'red'>Bishop Evaluation: " + EvaluationPane.bishopEval;
        } else {
        	info = info + "<br><br><font color = 'green'>Bishop Evaluation: " + EvaluationPane.bishopEval;
        }
        
        if(EvaluationPane.knightEval < 0) {
        	info = info + "<br><br><font color = 'red'>Knight Evaluation: " + EvaluationPane.knightEval;
        } else {
        	info = info + "<br><br><font color = 'green'>Knight Evaluation: " + EvaluationPane.knightEval;
        }
        
        if(EvaluationPane.overallEval < 0) {
        	info = info + "<br><br><font color = 'red'>Overall Evaluation: " + EvaluationPane.overallEval + "</html>";
        } else {
        	info = info + "<br><br><font color = 'green'>Overall Evaluation: " + EvaluationPane.overallEval + "</html>";	
        }
        EvaluationPane.setEnterName(info);
        return humanScore - computerScore;
     
    }    
    public void scaleDisplayValues() {
    	if(EvaluationPane.pawnEval > 10) {
    		EvaluationPane.pawnEval = EvaluationPane.pawnEval / 2;
    	}
    	if(EvaluationPane.overallEval > 100 || EvaluationPane.overallEval < 0) {
    		EvaluationPane.overallEval = EvaluationPane.overallEval /20;
    	}
    	if(EvaluationPane.overallEval > 10) {
    		EvaluationPane.overallEval = EvaluationPane.overallEval /2;
    	}
    }
}
