
package chessGame;

public class MoveSearcher {
    ChessGame chessGame;
    int level;
    public MoveSearcher(ChessGame chessGame){
        this.chessGame = chessGame;
    }
    public Position alphaBeta(int player, Position position, int alpha, int beta, int depth){ //alpha beta search for finding the best move, takes into account the level of difficulty.
        if(depth == 0) return position;
        Position optimalPosition = null;
        MoveGenerator moveGenerator = new MoveGenerator(position,player);
        moveGenerator.generateMoves();
        Position[] positions = moveGenerator.getPositions();
        if(positions.length == 0) return position;    
        
        Evaluator evaluator = new Evaluator();  
        for(Position _position:positions){
            if(optimalPosition == null) optimalPosition = _position; // the first time running through set the first position to be optimal, optimalPosition is declared and initialized as null earlier
            if(player == GameData.HUMAN){
                Position opponentPosition = alphaBeta(GameData.COMPUTER,_position,alpha,beta,depth-1);                
                int score = evaluator.evaluate(opponentPosition);
                if(score>alpha){
                    optimalPosition = _position;
                    alpha = score;
                }
            }else{
                Position opponentPosition = alphaBeta(GameData.HUMAN,_position,alpha,beta,depth-1);                
                if(new Game(opponentPosition).isChecked(GameData.HUMAN)){
                    return _position;
                }
                int score = evaluator.evaluate(opponentPosition);
                if(score<=alpha && level > 4) return _position;
                if(score<beta){
                    optimalPosition = _position;
                    beta = score;
                }              
            }
        }
        return optimalPosition;
    }
}
