package chessGame;

/**
 *
 * @author Vincenzo Scialpi 15072935
 */
public class Game {          
    Position position;
    Piece humanKing; // declare the Computer's victory condition, the human King
    Piece computerKing; // declare the human's victory condition, the computer's King
    
    public Game(Position position){
        humanKing = position.humanPieces[8];
        computerKing = position.computerPieces[8];
        this.position = position;
    }
    public int getResult(int player){
        int state = -1;
        MoveGenerator mg = new MoveGenerator(position,player); // create instantiation of the move generator, pass the position and which player is undertaking their turn
        mg.generateMoves(); // call the generate moves function in the moveGenerator class
        Position[] positions = mg.getPositions(); // create an array of the positions and fill it with the generated moves
        if(positions.length == 0) { // <1> if there are no possible moves to be done...
            if(isChecked(player)) { // if there are possible moves and the player is CHECKED
                state = GameData.CHECKMATE; // the game ends in CHECKMATE
            }
            else state = GameData.DRAW; //...Neither side can move therefore the game ends in a DRAW <1/>
        }
        return state; // return the game state: DRAW/CHECKMATE/-1
    }    
    public boolean safeMove(int player, int source,int destination){
        Move _move = new Move(source,destination);
        Position _position = new Position(position,_move);  
        Game gs = new Game(_position);   
        return !gs.isChecked(player);
    }
    public boolean isChecked(int player){ // function to check if the player is in check
        boolean checked = false; // create the checked boolean which is instantiated as false
        Piece king = (player == GameData.HUMAN)?humanKing:computerKing;
        if(king == null) return false; // check the kingpiece isn't a null value, avoids errors in execution
        checked = checkedByPawn(king); // change the checked boolean to true if the king is in check by a pawn
        if(!checked) checked = checkedByKnight(king); //do for each of the pieces if not already place in check
        if(!checked) checked = checkedByBishop(king);
        if(!checked) checked = checkedByRook(king);
        if(!checked) checked = checkedByQueen(king);
        if(!checked) checked = desSquareAttackedByKing(king);       
        return checked; // return the final state true or false of whether the king is in check or not 
    }
    private boolean checkedByPawn(Piece king){ // function to determine if a king is placed in check by a pawn
        boolean checked = false;   
        int location = king.location; // get the location of the King Piece that has been passed into the function
        if(king == humanKing){ // if the king is the user's king
            int right_square = position.board[location-9]; // the right square is the position on the board of the user's king - 9, this moves across until it is on the row above and one to the right
            int left_square = position.board[location-11]; // the left square is the position on the board of the user's king - 11
            if(right_square == GameData.ILLEGAL || left_square == GameData.ILLEGAL) return false; // if the above squares are in illegal places, the king isn't in check
            if(right_square<0 && position.computerPieces[-right_square].value == Piece.PAWN) // if the right square to the king is occupied by the computer's pawn, the user is in check
                checked = true;
            if(left_square<0 && position.computerPieces[-left_square].value == Piece.PAWN) // likewise if the left square to the king is occupied by the computer's pawn
                checked = true;
        }else{ // for the Computer's King piece
            int right_square = position.board[location+11];  // right square is the position of the king +11
            int left_square = position.board[location+9]; // left square is the position of the king +9
            if(right_square != GameData.ILLEGAL){ // if the right square isn't in an illegal position
                if(right_square>0 && right_square != GameData.EMPTY && position.humanPieces[right_square].value == Piece.PAWN) // if rightsquare is occupied by a human pawn
                    checked = true; // the computer's king is in check
            }
            if(left_square != GameData.ILLEGAL){ // likewise if the left square is occupied by a human pawn
                if(left_square>0 && left_square != GameData.EMPTY && 
                        position.humanPieces[left_square].value == Piece.PAWN)
                    checked = true;
            }
        }
        return checked; // return the value true or false of whether the king passed in is in check or not by a pawn
        
        /**
         *  X	X	X
         *  X	X	X
         * 	X	X	X
         *  X KING  X
         * PAWN	X	PAWN
         *  X	X	X
         * 	X	X	X
         * this is a check scenario for the computer's king by the human's pawns, either pawn can be in the given position to result in a check
         */
    }
    private boolean checkedByKnight(Piece king) { // function to check whether the king passed into the function is in check by a knight
        boolean checked = false;
        int location = king.location; // get the location of the king piece that has been passed in
        int[] destinations = {location-21,location+21,location+19,location-19, 
            location-12,location+12,location-8,location+8}; // an array of the potential squares the knight could be in in relation to the king to place it in check
        for(int destination:destinations){ // for every destintation in the array of destinations
            int des_square = position.board[destination]; // the current destination square in question is put into a separate variable
            if(des_square == GameData.ILLEGAL) continue; // if that square is off the board there can't possibly be a knight there to just leave it
            if(king == humanKing) { // for the human side
                if(des_square<0 && position.computerPieces[-des_square].value == Piece.KNIGHT) { // if the current square in question is occupied by a computer Knight
                    checked = true; // then the user is in check
                    break;
                }
            } else { // for the computer's side 
                if(des_square>0 && des_square != GameData.EMPTY && position.humanPieces[des_square].value == Piece.KNIGHT) { // if the square is occupied by a human knight
                    checked = true; // the computer is in check
                    break;
                }
            }
        }
        return checked;  // return the value true or false of whether the king passed in is in check or not by a knight
    }
    private boolean desSquareAttackedByKing(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10,
            location+11,location-11,location+9,location-9};
        for(int destination:destinations){
            int des_square = position.board[destination];
            if(des_square == GameData.ILLEGAL) continue;
            if(king == humanKing){                
                if(des_square<0 && position.computerPieces[-des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != GameData.EMPTY && 
                        position.humanPieces[des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    private boolean checkedByBishop(Piece king){
        boolean checked = false;
        int[] deltas = {11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == humanKing){
                    if(des_square<0 && position.computerPieces[-des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computerKing){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.humanPieces[des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByRook(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == humanKing){
                    if(des_square<0 && position.computerPieces[-des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computerKing){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.humanPieces[des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByQueen(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == GameData.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == humanKing){
                    if(des_square<0 && position.computerPieces[-des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }else if(king == computerKing){
                    if(des_square>0 && des_square != GameData.EMPTY && 
                            position.humanPieces[des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != GameData.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }
}
