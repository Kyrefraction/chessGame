package chessGame;

/**
 *
 * @author 15072935 Vincenzo Scialpi-Sullivan
 */
public class Evaluator {   // evaluate a chess position, if it is more advantageous to the human or the computer
    public int evaluate(Position position){        
        int humanScore = 0;
        int computerScore = 0;
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
                        if(col>4){
                            humanScore -= ((col-4));
                        } else if(col<3){
                            humanScore -= ((3-col));
                        }
                        if(col>1 && col<6 && row>1) humanScore +=2;
                        if(row == 0) humanScore += Piece.QUEEN;
                        //human_score += GameData.HUMAN_PAWN_TABLE[piece.location];
                        break;
                    case Piece.KNIGHT: 
                        if(row == 7) humanScore -=1;
                        if(col == 0 || col == 7) humanScore -= 1;
                        if(col>1 && col<6 && row>1 && row<6) humanScore +=1;
                        //human_score += GameData.HUMAN_KNIGHT_TABLE[piece.location];                        
                        break;
                    case Piece.BISHOP: 
                        if(row == 7) humanScore-=1;
                        if(col == 0 || col == 7) humanScore -= 1;
                        if(col>0 && col<7 && row>0 && row<7) humanScore +=1;
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
        return humanScore - computerScore;
    }    
}
