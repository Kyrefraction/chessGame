package chessGame;

public class Position {
    Move lastMove;
    int[] board = new int[120];
    Piece[] humanPieces = new Piece[17];
    Piece[] computerPieces = new Piece[17];
    
    public Position(){
        for(int i=0; i<board.length; i++){
            board[i] = GameData.EMPTY;
        }
    }
    public Position(Position position){
        this(position,null);
    }
    public Position(Position position, Move last_move){
        System.arraycopy(position.board, 0, this.board, 0, board.length);
        for(int i=1; i<humanPieces.length; i++){
            if(position.humanPieces[i] != null){
                this.humanPieces[i] = position.humanPieces[i].clone();
            }
            if(position.computerPieces[i] != null){
                this.computerPieces[i] = position.computerPieces[i].clone();
            }
        }
        if(last_move != null) update(last_move);
    }    
    public void initialize(boolean humanWhite){         
        humanPieces[1] = new Piece(Piece.KNIGHT,92);
        humanPieces[2] = new Piece(Piece.KNIGHT,97);
        humanPieces[3] = new Piece(Piece.BISHOP,93);
        humanPieces[4] = new Piece(Piece.BISHOP,96);
        humanPieces[5] = new Piece(Piece.ROOK,91);
        humanPieces[6] = new Piece(Piece.ROOK,98);
        humanPieces[7] = new Piece(Piece.QUEEN,humanWhite?94:95);
        humanPieces[8] = new Piece(Piece.KING,humanWhite?95:94);
        
        computerPieces[1] = new Piece(Piece.KNIGHT,22);
        computerPieces[2] = new Piece(Piece.KNIGHT,27);
        computerPieces[3] = new Piece(Piece.BISHOP,23);
        computerPieces[4] = new Piece(Piece.BISHOP,26);
        computerPieces[5] = new Piece(Piece.ROOK,21);
        computerPieces[6] = new Piece(Piece.ROOK,28);
        computerPieces[7] = new Piece(Piece.QUEEN,humanWhite?24:25);
        computerPieces[8] = new Piece(Piece.KING,humanWhite?25:24); 
        
        int j = 81;
        for(int i=9; i<humanPieces.length; i++){
            humanPieces[i] = new Piece(Piece.PAWN,j);
            computerPieces[i] = new Piece(Piece.PAWN,j-50);
            j++;
        }                      
        board = new int[]{
            GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.EMPTY,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,
            GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL,GameData.ILLEGAL
        };        
        for(int i=0; i<board.length; i++){                        
            for(int k=1; k<humanPieces.length; k++){
                if(i==humanPieces[k].location){
                    board[i] = k;
                }else if(i==computerPieces[k].location){
                    board[i] = -k;
                }
            }
        }
    }    
    public void update(Move move){
        this.lastMove = move;   
        int sourceIndex = board[move.sourceLocation];
        int destination_index = board[move.destination];  
        if(sourceIndex>0){
            humanPieces[sourceIndex].hasMoved = true;
            humanPieces[sourceIndex].location = move.destination;
            if(destination_index<0){                
                computerPieces[-destination_index] = null;
            }            
        }else{
            computerPieces[-sourceIndex].hasMoved = true;
            computerPieces[-sourceIndex].location = move.destination;
            if(destination_index>0 && destination_index != GameData.EMPTY){                
                humanPieces[destination_index] = null;
            }            
        }
        board[move.sourceLocation] = GameData.EMPTY;
        board[move.destination] = sourceIndex;
    }
}
