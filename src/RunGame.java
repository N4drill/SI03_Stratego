import java.util.Scanner;

public class RunGame {

    private static final int BOARD_SIZE = 4;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[]args){

        GameBoard gameBoard = new GameBoard(BOARD_SIZE);
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        int moves = 1;
       while(1==1) {
           showGameInfo(player1,player2,moves);
           if(moves%2 == 0) {
               Move nextMove = getMove();
               gameBoard.makeMove(player1, nextMove.getX(), nextMove.getY());
           }
           else{
               Move nextMove = getMove();
               gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
           }
           gameBoard.printGameBoard();
           moves++;
       }


    }

    private static void showGameInfo(Player p1, Player p2, int move){
        System.out.println("---------------------------------------------");
        System.out.println("Player" + p1.getPlayerNumber() + " score: "+ p1.getScore());
        System.out.println("Player" + p2.getPlayerNumber() + " score: "+ p2.getScore());
        System.out.println("-----------------Move "+move+" -----------------");
    }

    private static Move getMove(){
        System.out.print("Podaj x: ");
        int x= scanner.nextInt();
        System.out.print("Podaj y: ");
        int y= scanner.nextInt();
        return new Move(x,y);
    }

}
