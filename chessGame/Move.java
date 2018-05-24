package chessGame;

/**
 *
 * @author Vincenzo Scialpi 15072935
 */
public class Move {
    int sourceLocation; // current location
    int destination; // location to move to
    public Move() { // no variable constructor
        sourceLocation = -1;
        destination = -1;
    }
    public Move(int source_location, int destination) { // constructor with variables passed 
        this.sourceLocation = source_location;
        this.destination = destination;
    }
}
