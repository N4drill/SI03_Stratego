import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
    int boardSize;
    int[][] gameBoard;
    Random random = new Random();

    public GameBoard(){};

    public GameBoard(int boardSize) {
        this.boardSize = boardSize;
        gameBoard = new int[boardSize][boardSize];
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = 0;
            }
        }
    }

    public void printGameBoard() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (gameBoard[x][y] == 1) {
                    System.out.print("[X]");
                } else if (gameBoard[x][y] == 2) {
                    System.out.print("[O]");
                } else System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    public boolean makeMove(Player player, int x, int y) {
        if (!checkMove(x, y)){
            System.out.println("!!!------Ruch (" + x + "," + y + ")niedozwolony------!!!");
            return false;
        }
        else {
            gameBoard[x][y] = player.getPlayerNumber();
            player.addScore(checkScore(x, y));
            return true;
        }
    }

    public int makeMove(int [][] board, int x, int y){
        board[x][y] = 1;
        return checkScore(x, y);
    }

    public int checkScore(int x, int y) {
        int points = 0;
        if (checkCol(x, y) == boardSize) points += boardSize;
        if (checkRow(x, y) == boardSize) points += boardSize;
        points += countDiagonals(x, y);
        return points;
    }

    private boolean checkMove(int x, int y) {
        if (!checkOutOfBoard(x, y)) return false;
        else if (!checkPlaceEmpty(x, y)) return false;
        else return true;
    }

    private boolean checkPlaceEmpty(int x, int y) {
        if (gameBoard[x][y] == 1 || gameBoard[x][y] == 2) return false;
        else return true;
    }

    private boolean checkOutOfBoard(int x, int y) {
        if (x < 0 || x >= boardSize) return false;
        else if (y < 0 || y >= boardSize) return false;
        else return true;
    }


    private int checkRow(int x_pos, int y_pos) {
        int notEmpty = 0;
        for (int x = 0; x < boardSize; x++) {
            if (!checkPlaceEmpty(x, y_pos)) notEmpty++;
        }
        return notEmpty;
    }

    private int checkCol(int x_pos, int y_pos) {
        int notEmpty = 0;
        for (int y = 0; y < boardSize; y++) {
            if (!checkPlaceEmpty(x_pos, y)) notEmpty++;
        }
        return notEmpty;

    }

    private int filledDaigonalUpLeft(int x_pos, int y_pos) {
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

    private int filledDaigonalUpRight(int x_pos, int y_pos) {
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

    private int filledDaigonalDownLeft(int x_pos, int y_pos) {
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

    private int filledDaigonalDownRight(int x_pos, int y_pos) {
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

    private int countDiagonals(int x_pos, int y_pos) {
        int points = 0;
        int upLeft = filledDaigonalUpLeft(x_pos, y_pos);
        int upRight = filledDaigonalUpRight(x_pos, y_pos);
        int downLeft = filledDaigonalDownLeft(x_pos, y_pos);
        int downRight = filledDaigonalDownRight(x_pos, y_pos);
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

    public Move randomEmptyPlace(){
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

    public Move getBestGreedyMove(){
        Move bestMove = new Move(0,0);
        int bestScore = 0;
        for(int y= 0; y<boardSize ; y++) {
            for(int x = 0; x<boardSize; x++ ){
                if(gameBoard[x][y]==0){
                    int placeScore = checkScore(x,y);
                    if(placeScore>bestScore){
                        bestMove.setX(x);
                        bestMove.setY(y);
                        bestScore = placeScore;
                    }
                }
            }
        }
        if(bestScore==0) {
            bestMove = randomEmptyPlace();
        }
        return bestMove;
    }

    public boolean isGameEnd()
    {
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize;j++){
                if(gameBoard[i][j]==0) return false;
            }
        }
        return true;
    }

    public boolean isGameEnd(int [][] board)
    {
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length;j++){
                if(board[i][j]==0) return false;
            }
        }
        return true;
    }

    public void undoMove( int [][] board, int x, int y){
        board[x][y] = 0;
    }




}