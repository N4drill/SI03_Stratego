public class GameBoard {
    int boardSize;
    int [][] gameBoard;


    public GameBoard(int boardSize){
        this.boardSize = boardSize;
        gameBoard = new int [boardSize][boardSize];
        initBoard();
    }

    private void initBoard(){
        for(int i= 0 ; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                gameBoard[i][j] = 0;
            }
        }
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
        points+=countDiagonals(x,y);
        points+=filledCorners(x,y);
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

    private int countDiagonals(int x_pos, int y_pos){
        int sum = 0;
        int downLeft=filledDiagonalDownLeft(x_pos,y_pos);
        int downRight=filledDiagonalDownRight(x_pos,y_pos);
        int upLeft=filledDiagonalUpLeft(x_pos,y_pos);
        int upRight=filledDiagonalUpRight(x_pos, y_pos);
        if(x_pos == 0 && y_pos == 0){
            if(downRight>0) sum+=downRight+1;
        } else
        if(x_pos == 0 && y_pos == boardSize-1){
            if(upRight>0) sum+=upRight+1;
        } else
        if(x_pos == boardSize -1 && y_pos == 0){
            if(downLeft>0) sum+=downLeft+1;
        } else
        if(x_pos == boardSize -1 && y_pos == boardSize -1 ){
            if(upLeft>0) sum+=upLeft + 1;
        }
        else // gdy nie jestes na krawedziach
        {
            if(upLeft>0 && downRight >0 ) sum+=upLeft + downRight + 1;
            if(upRight>0 && downLeft >0 ) sum+=upRight + downLeft +1 ;
        }
        return sum;
    }


    private int filledDiagonalUpLeft(int x_pos, int y_pos){
        int sum = 0;
        boolean isFilled = true;
        for(int y= y_pos-1; y>=0 && isFilled; y--){
            for(int x = x_pos-1 ; x>=0 && isFilled; x--){
                boolean wzor = x+y == x_pos+y_pos+((sum+1)*(-2));
                boolean wejscie = ((x==0 && y==0) || (y!=0 && x!=0)) ;
                //if( (x+y == x_pos+y_pos+((sum+1)*(-2)))  && ((x==0 && y==0) || (y!=0 && x!=0) || (y==0 && x==2) || (x==0 && y==2))) {
                if( (wzor)  && (wejscie) ) {
                    if (gameBoard[x][y] == 0) isFilled = false;
                    else sum++;
                }
            }
        }
        return isFilled ? sum : 0;
    }

    private int filledDiagonalUpRight(int x_pos, int y_pos){
        int sum = 0;
        boolean isFilled = true;
        for(int y= y_pos -1 ; y>=0 && isFilled; y--){
            for(int x = x_pos +1 ;  x< boardSize && isFilled; x++){
                if(x+y == x_pos+y_pos) {
                    if (gameBoard[x][y] == 0) isFilled = false;
                    else sum++;
                }
            }
        }
        return isFilled ? sum : 0;
    }

    private int filledDiagonalDownLeft(int x_pos, int y_pos){
        int sum=0;
        boolean isFilled = true;
        for(int y= y_pos +1 ; y<boardSize && isFilled; y++){
            for(int x = x_pos-1 ; x>=0 && isFilled; x--){
                if(x+y==x_pos+y_pos) {
                    if (gameBoard[x][y] == 0) isFilled = false;
                    else sum++;
                }
            }
        }
        return isFilled ? sum : 0;
    }

    private int filledDiagonalDownRight(int x_pos, int y_pos){
        int sum = 0 ;
        boolean isFilled = true;
        for(int y= y_pos +1 ; y<boardSize && isFilled; y++){
            for(int x = x_pos +1  ; x<boardSize && isFilled; x++){
                if((x+y) == (x_pos+y_pos+((sum+1)*2))  && ((x==0 && y==0) || (y!=0 && x!=0))) {
                    if (gameBoard[x][y] == 0) isFilled = false;
                    else sum++;
                }
            }
        }
        return isFilled ? sum : 0;
    }

    private int filledCorners(int x_pos, int y_pos){
        int sum = 0;
        if(x_pos == boardSize-2 && y_pos == 0){ //(n-1,0)
            if(gameBoard[boardSize-1][1] != 0 ) sum+= 2;
        }
        if(x_pos== boardSize -1 && y_pos == 1 ){//(n-1,1)
            if(gameBoard[boardSize-2][0] != 0) sum+= 2;
        }
        if(x_pos == 0 && y_pos == boardSize-2){//(0,n-1)
            if(gameBoard[1][boardSize-1] != 0 ) sum+= 2;
        }
        if(x_pos == 1 && y_pos == boardSize-1){//(n-1,n-1)
            if(gameBoard[0][boardSize-2] !=0) sum+= 2;
        }
        return sum;
    }
}
