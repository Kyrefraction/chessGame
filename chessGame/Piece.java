
package chessGame;

public class Piece {
    public final static int PAWN = 100; // numerical values to refer to the pieces of chess
    public final static int KNIGHT = 320;
    public final static int BISHOP = 325;
    public final static int ROOK = 500;
    public final static int QUEEN = 900;
    public final static int KING = 1000000;       
    int value;
    int location;
    boolean hasMoved;
    public Piece(int value,int location){
        this(value,location,false);
    }
    public Piece(int value,int location,boolean hasMoved){
        this.value = value;
        this.location = location;
        this.hasMoved = hasMoved;
    }
    @Override
    public Piece clone(){
        return new Piece(value,location,hasMoved);
    }
}
