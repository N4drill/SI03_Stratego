public class GameBoard {
    int boardSize;
    int [][] gameBoard;


    public GameBoard(int boardSize){
        this.boardSize = boardSize;
        gameBoard = new int [boardSize][boardSize];
    }

    public void printGameBoard() {
        for (int y = 0; y<boardSize; y++){
            for(int x= 0 ; x<boardSize; x++){
                if(gameBoard[x][y] == 1) { System.out.print("[X]"); }
                else if(gameBoard[x][y] == 2) { System.out.print("[O]"); }
                else System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    public void makeMove(Player player, int x, int y){
        if(!checkMove(x,y)) System.out.println("Ruch ("+x+","+y+")niedozwolony");
        else {
            gameBoard[x][y] = player.getPlayerNumber();
            player.addScore(checkScore(x,y));
        }
    }

    private int checkScore(int x, int y) {
        int points = 0;
        if(checkCol(x,y) == boardSize) points+=boardSize;
        if(checkRow(x,y) == boardSize) points+=boardSize;
        return points;
    }

    private boolean checkMove(int x, int y) {
        if(!checkOutOfBoard(x, y)) return false;
        else if(!checkPlaceEmpty(x, y)) return false;
        else return true;
    }

    private boolean checkPlaceEmpty(int x, int y) {
        if (gameBoard[x][y] == 1 || gameBoard[x][y]==2) return false;
        else return true;
    }

    private boolean checkOutOfBoard(int x, int y) {
        if(x<0 || x>=boardSize) return false;
        else if(y<0 || y>=boardSize) return false;
        else return true;
    }




    private int checkRow(int x_pos, int y_pos){
        int notEmpty = 0;
        for(int x = 0 ; x < boardSize ; x++){
            if(!checkPlaceEmpty(x, y_pos)) notEmpty++;
        }
        return notEmpty;
    }

    private int checkCol(int x_pos, int y_pos){
        int notEmpty = 0;
        for(int y = 0 ; y < boardSize ; y++){
            if(!checkPlaceEmpty(x_pos, y)) notEmpty++;
        }
        return notEmpty;
    }

}
