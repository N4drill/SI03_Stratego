import java.util.ArrayList;
import java.util.Random;

public class Player {
    Random random = new Random();
    private int playerNumber;
    private int score=0;
    private String name;

    public Player(int playerNumber, String name){
        this.playerNumber = playerNumber;
        this.name = name;
    }

    public Player(Player player){
        this.playerNumber = player.getPlayerNumber();
        this.score = player.getScore();
        this.name = player.getName();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score){
        this.score+=score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int evaluateScore( Player player, int points){
        if(this.getScore() > player.getScore()) return points;
        if(this.getScore() < player.getScore()) return -points;
        return 0;
    }

    public int evaluateScore ( Player player) {
        if(this.getScore() > player.getScore()) return 100;
        if(this.getScore() < player.getScore()) return -100;
        return 0;
    }

    public void reduceScore (int points){
        score = score - points;
    }

    public int makeMove(int [][] board, int x, int y){
        board[x][y] = 1;
        return checkScore(board, board.length, x, y);
    }

    public int checkScore(int [][] gameBoard, int boardSize, int x, int y) {
        int points = 0;
        if (checkCol(gameBoard, boardSize, x, y) == boardSize) points += boardSize;
        if (checkRow(gameBoard, boardSize, x, y) == boardSize) points += boardSize;
        points += countDiagonals(gameBoard, boardSize, x, y);
        return points;
    }

    private boolean checkPlaceEmpty(int [][] gameBoard, int x, int y) {
        if (gameBoard[x][y] == 1 || gameBoard[x][y] == 2) return false;
        else return true;
    }

    private boolean checkOutOfBoard(int boardSize, int x, int y) {
        if (x < 0 || x >= boardSize) return false;
        else if (y < 0 || y >= boardSize) return false;
        else return true;
    }


    private int checkRow(int [][] gameBoard , int boardSize, int x_pos, int y_pos) {
        int notEmpty = 0;
        for (int x = 0; x < boardSize; x++) {
            if (!checkPlaceEmpty(gameBoard, x, y_pos)) notEmpty++;
        }
        return notEmpty;
    }

    private int checkCol(int [][] gameBoard ,int boardSize, int x_pos, int y_pos) {
        int notEmpty = 0;
        for (int y = 0; y < boardSize; y++) {
            if (!checkPlaceEmpty(gameBoard, x_pos, y)) notEmpty++;
        }
        return notEmpty;

    }

    private int filledDaigonalUpLeft(int [][] gameBoard, int x_pos, int y_pos) {
        boolean foundEmpty = false;
        int points = 0;
        x_pos--;
        y_pos--;
        while (x_pos >= 0 && y_pos >= 0 && !foundEmpty) {
            if (gameBoard[x_pos][y_pos] != 0) {
                points++;
            } else foundEmpty = true;

            x_pos--;
            y_pos--;
        }
        return !foundEmpty ? points : 0;

    }

    private int filledDaigonalUpRight(int [][] gameBoard , int boardSize, int x_pos, int y_pos) {
        boolean foundEmpty = false;
        int points = 0;
        x_pos++;
        y_pos--;
        while (x_pos < boardSize && y_pos >= 0 && !foundEmpty) {
            if (gameBoard[x_pos][y_pos] != 0) {
                points++;
            } else foundEmpty = true;

            x_pos++;
            y_pos--;
        }
        return !foundEmpty ? points : 0;
    }

    private int filledDaigonalDownLeft(int [][] gameBoard , int boardSize, int x_pos, int y_pos) {
        boolean foundEmpty = false;
        int points = 0;
        x_pos--;
        y_pos++;
        while (x_pos >= 0 && y_pos < boardSize && !foundEmpty) {
            if (gameBoard[x_pos][y_pos] != 0) {
                points++;
            } else foundEmpty = true;

            x_pos--;
            y_pos++;
        }
        return !foundEmpty ? points : 0;
    }

    private int filledDaigonalDownRight(int [][] gameBoard , int boardSize, int x_pos, int y_pos) {
        boolean foundEmpty = false;
        int points = 0;
        x_pos++;
        y_pos++;
        while (x_pos < boardSize && y_pos < boardSize && !foundEmpty) {
            if (gameBoard[x_pos][y_pos] != 0) {
                points++;
            } else foundEmpty = true;

            x_pos++;
            y_pos++;
        }
        return !foundEmpty ? points : 0;
    }

    private int countDiagonals(int [][] gameBoard , int boardSize, int x_pos, int y_pos) {
        int points = 0;
        int upLeft = filledDaigonalUpLeft(gameBoard, x_pos, y_pos);
        int upRight = filledDaigonalUpRight(gameBoard, boardSize,  x_pos, y_pos);
        int downLeft = filledDaigonalDownLeft(gameBoard, boardSize,x_pos, y_pos);
        int downRight = filledDaigonalDownRight(gameBoard, boardSize,x_pos, y_pos);
        if (x_pos == 0 || x_pos == boardSize - 1 || y_pos == 0 || y_pos == boardSize - 1) {
            if(upLeft>0) upLeft++;
            if(upRight>0) upRight++;
            if(downLeft>0) downLeft++;
            if(downRight>0) downRight++;
            points+=upLeft+upRight+downLeft+downRight;
        } else {
            if (upLeft != 0 && downRight != 0) points += upLeft + downRight + 1;
            if (upRight != 0 && downLeft != 0) points += upRight + downLeft + 1;
        }
        return points;
    }

    public void undoMove( int [][] board, int x, int y){
        board[x][y] = 0;
    }

    public Move randomEmptyPlace(int [][] gameBoard, int boardSize){
        ArrayList<Move> freeMoves = new ArrayList<>();
        for(int i=0; i<boardSize ; i ++ ){
            for(int j= 0; j<boardSize; j++){
                if(gameBoard[i][j]==0){
                    freeMoves.add(new Move(i,j));
                }
            }
        }
        return freeMoves.get(random.nextInt(freeMoves.size()));
    }

    public Move getBestGreedyMove(int [][] gameBoard, int boardSize){
        Move bestMove = new Move(0,0);
        int bestScore = 0;
        for(int y= 0; y<boardSize ; y++) {
            for(int x = 0; x<boardSize; x++ ){
                if(gameBoard[x][y]==0){
                    int placeScore = checkScore(gameBoard, boardSize,x,y);
                    if(placeScore>bestScore){
                        bestMove.setX(x);
                        bestMove.setY(y);
                        bestScore = placeScore;
                    }
                }
            }
        }
        if(bestScore==0) {
            bestMove = randomEmptyPlace(gameBoard, boardSize);
        }
        return bestMove;
    }
}
