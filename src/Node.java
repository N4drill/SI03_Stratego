import java.util.ArrayList;

public class Node {
    private Move move;
    private int potentialScore;
    private ArrayList<Node> otherMoves;

    Node(Move move){
        this.move=move;
    }

    public void setPotentialScore(GameBoard gameBoard, Move move){
        potentialScore = gameBoard.checkScore(move.getX(),move.getY());
    }

    public void setOtherMoves(GameBoard gameBoard){
        for(int y=0; y<gameBoard.boardSize; y++){
            for(int x=0; x<gameBoard.boardSize; x++){
                if(gameBoard.gameBoard[x][y]==0){
                    Move newMove = new Move(x,y);
                    Node node = new Node(newMove);
                    node.setPotentialScore(gameBoard, newMove);
                }
            }
        }
    }

    public void goMin() {

    }

    public void goMax(){

    }

}
