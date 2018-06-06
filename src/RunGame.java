import java.util.Scanner;

public class RunGame {

    private static final int BOARD_SIZE = 10;
    private static final int MAX_DEPTH = 5;
    private static final boolean IS_MINMAX = false;
    private static final int GAME_MODE = 12;
    private static final double HEURISTIC_RANDOM_PERCENT = 0.5;
    private static final double HEURISTIC_GREEDY_PERCENT = 0.5;
    private static final int FIRST_STARTS = 1; // 1 - ZACZYNA PIERWSZY //0 - ZACZYNA DRUGI 
    //  1- PvR
    // 2- PvG
    // 3 -RvG
    // 4 AI vs AI
    // 5- Player vs AI
    // 6- Random vs AI
    // 7- Greedy Vs AI
    // 8- Greedy Vs AI Heuristic
    // 9 - Testing Results
    // others - PvP


    static Scanner scanner = new Scanner(System.in);
    static int moves = 0; // 0- zaczyna PLAYER 1   // 1- zaczyna PLAYER 2
    public static void main(String[]args) throws InterruptedException {


        GameBoard gameBoard = new GameBoard(BOARD_SIZE);
        Player player1 = new Player(1, "PLAYER");
        Player player2 = new Player(2 , "PLAYER");
        Player playerRandom2 = new Player(2, "RANDOM");
        Player playerRandom1 = new Player(1, "RANDOM");
        Player playerGreedy1 = new Player(1, "GREEDY");
        Player playerGreedy2 = new Player(2, "GREEDY");
        AI ai1 = new AI(1, "Arificial 1");
        AI ai2 = new AI(2, "Arificial 2");
        gameBoard.printGameBoard();

        switch(GAME_MODE){
            default:
                playerVSplayer(gameBoard,player1,player2);break;
            case 1:
                playerVSRandom(gameBoard,player1,playerRandom2);break;
            case 2:
                playerVSgreedy(gameBoard,player1, playerGreedy2);break;
            case 3:
                randomVSgreedy(gameBoard,playerRandom1,playerGreedy2);break;
            case 4:
                AIvsAI(gameBoard, ai1, ai2, MAX_DEPTH, IS_MINMAX);break;
            case 5:
                PlayerVSAI(gameBoard,ai1,player2, MAX_DEPTH, IS_MINMAX);break;
            case 6:
                randomVSAI(gameBoard,ai1,playerRandom2,MAX_DEPTH,IS_MINMAX);break;
            case 7:
                greedyVSAI(gameBoard,ai1,playerGreedy2,MAX_DEPTH,IS_MINMAX);
            case 8:
                greedyVSAIRandomHeuristic(gameBoard,ai1,playerGreedy2,MAX_DEPTH,IS_MINMAX, HEURISTIC_RANDOM_PERCENT);
            case 9:
                greedyVSAIGreedyHeuristic(gameBoard,ai1,playerGreedy2,MAX_DEPTH,IS_MINMAX, HEURISTIC_GREEDY_PERCENT);
            case 10:
                greedyVSAIDepthHeuristic(gameBoard,ai1,playerGreedy2,MAX_DEPTH,IS_MINMAX);
            case 11:
                greedyVSAIDepthHeuristicConstant(gameBoard,ai1,playerGreedy2,MAX_DEPTH,IS_MINMAX);
            case 12:
                testingResults(ai1,playerGreedy2);
        }
    }

    private static void testingResults(AI ai1, Player playerGreedy2) {
        // TEST 1
        int boardsize = 10;
        int depth = 4;
        double random = 0.1;
        double greedy = 0.5;
        boolean minmax = false;
        testingCase(ai1,playerGreedy2, boardsize, depth, random, greedy, minmax);
    }


    private static void testingCase(AI ai1, Player playerGreedy2, int BoardSize, int Depth, double random_percent, double greedy_percent, boolean isMinMax) {
        GameBoard gameBoardBasic = new GameBoard(BoardSize);
        System.out.println("@@@@@@@ BASIC START @@@@@@");
        long basicStart1 = System.currentTimeMillis();
        String basicWinner = greedyVSAI(gameBoardBasic,ai1,playerGreedy2,Depth,isMinMax);
        long basicEnd1 = System.currentTimeMillis();
        long basicTime1 = basicEnd1 - basicStart1;
        moves=0;

        GameBoard gameBoardRandom  = new GameBoard(BoardSize);
        System.out.println("@@@@@@@ RANDOM START @@@@@@");
        long randomStart1 = System.currentTimeMillis();
        String randomWinner = greedyVSAIRandomHeuristic(gameBoardRandom,ai1,playerGreedy2,Depth,isMinMax, random_percent);
        long randomEnd1 = System.currentTimeMillis();
        long randomTime1 = randomEnd1 - randomStart1;
        moves=0;

        GameBoard gameBoardGreedy = new GameBoard(BoardSize);
        System.out.println("@@@@@@@ GREEDY START @@@@@@");
        long greedyStart1 = System.currentTimeMillis();
        String greedyWinner = greedyVSAIGreedyHeuristic(gameBoardGreedy,ai1,playerGreedy2,Depth,isMinMax, greedy_percent);
        long greedyEnd1 = System.currentTimeMillis();
        long greedyTime1 = greedyEnd1 - greedyStart1;
        moves=0;

        GameBoard gameBoardDepth  = new GameBoard(BoardSize);
        System.out.println("@@@@@@@ DEPTH START @@@@@@");
        long depthStart1 = System.currentTimeMillis();
        String depthWinner = greedyVSAIDepthHeuristic(gameBoardDepth,ai1,playerGreedy2,Depth,isMinMax);
        long depthEnd1 = System.currentTimeMillis();
        long depthTime1 = depthEnd1 - depthStart1;
        moves=0;

        GameBoard gameBoardDepth2  = new GameBoard(BoardSize);
        System.out.println("@@@@@@@ DEPTH2 START @@@@@@");
        long depthStart2 = System.currentTimeMillis();
        String depthWinner2 = greedyVSAIDepthHeuristicConstant(gameBoardDepth2,ai1,playerGreedy2,Depth,isMinMax);
        long depthEnd2 = System.currentTimeMillis();
        long depthTime2 = depthEnd2 - depthStart2;
        moves=0;

        System.out.println("+++++++++++++++++++++RESULTS++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++Parameters++++++++++++++++++++++++");
        System.out.println("Board Size  :    " + BoardSize);
        System.out.println("Depth       :    " + Depth);
        System.out.println("Random %    :    " + random_percent);
        System.out.println("Greedy %    :    " + greedy_percent);
        System.out.println();
        System.out.println("Basic game time               : "+ basicTime1 + "   " + basicWinner);
        System.out.println("RandomHeuristic game time     : "+ randomTime1 + "   " + randomWinner);
        System.out.println("GreedyHeuristic game time     : "+ greedyTime1 + "   " + greedyWinner);
        System.out.println("DepthHeuristic  game time     : "+ depthTime1 + "   " + depthWinner);
        System.out.println("DepthHeuristic2 game time     : "+ depthTime2 + "   " + depthWinner2);

    }

    private static String greedyVSAIGreedyHeuristic(GameBoard gameBoard, AI ai1, Player playerGreedy, int maxDepth, boolean isMinmax, double percent){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerGreedy,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMoveHeuristicGreedy(gameBoard.gameBoard, ai1, maxDepth, isMinmax, moves, percent);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(playerGreedy, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        return showEndGameInfo(ai1,playerGreedy);
    }

    private static String greedyVSAIDepthHeuristic(GameBoard gameBoard, AI ai1, Player playerGreedy, int maxDepth, boolean isMinmax){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerGreedy,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMoveHeuristicDepthChange(gameBoard.gameBoard, ai1, maxDepth, isMinmax, moves);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(playerGreedy, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        return showEndGameInfo(ai1,playerGreedy);
    }

    private static String greedyVSAIDepthHeuristicConstant(GameBoard gameBoard, AI ai1, Player playerGreedy, int maxDepth, boolean isMinmax){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerGreedy,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMoveHeuristicDepthChange2(gameBoard.gameBoard, ai1, maxDepth, isMinmax, moves);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(playerGreedy, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        return showEndGameInfo(ai1,playerGreedy);
    }

    private static String greedyVSAIRandomHeuristic(GameBoard gameBoard, AI ai1, Player playerGreedy, int maxDepth, boolean isMinmax, double percent) {
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerGreedy,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMoveHeuristicRandom(gameBoard.gameBoard, ai1, maxDepth, isMinmax, moves, percent);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(playerGreedy, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        return showEndGameInfo(ai1,playerGreedy);
    }

    private static String PlayerVSAI(GameBoard gameBoard, AI ai1, Player player2, int maxDepth, boolean isMinmax) {
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,player2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMove(gameBoard.gameBoard, ai1, maxDepth, isMinmax);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = getMove();
                correctMove = gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        return showEndGameInfo(ai1,player2);
    }

    private static void AIvsAI(GameBoard gameBoard, AI ai1, AI ai2, int maxDepth, boolean minmax){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,ai2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMove(gameBoard.gameBoard, ai1, maxDepth, minmax);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = ai2.getOptimalMove(gameBoard.gameBoard, ai1, maxDepth, minmax);
                correctMove = gameBoard.makeMove(ai2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        showEndGameInfo(ai1,ai2);
    }

    private static void playerVSplayer(GameBoard gameBoard, Player player1, Player player2){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(player1,player2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = getMove();
                correctMove = gameBoard.makeMove(player1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = getMove();
                correctMove = gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        showEndGameInfo(player1,player2);
    }

    private static void randomVSAI(GameBoard gameBoard, AI ai1, Player playerRandom, int maxDepth, boolean minmax){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerRandom,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMove(gameBoard.gameBoard, ai1, maxDepth, minmax);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.randomEmptyPlace();
                correctMove = gameBoard.makeMove(playerRandom, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        showEndGameInfo(ai1,playerRandom);
    }

    private static String greedyVSAI(GameBoard gameBoard, AI ai1, Player playerGreedy, int maxDepth, boolean minmax){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(ai1,playerGreedy,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = ai1.getOptimalMove(gameBoard.gameBoard, ai1, maxDepth, minmax);
                correctMove = gameBoard.makeMove(ai1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(playerGreedy, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
       return showEndGameInfo(ai1,playerGreedy);
    }

    private static void playerVSRandom(GameBoard gameBoard, Player player1, Player player2){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(player1,player2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = getMove();
                correctMove = gameBoard.makeMove(player1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.randomEmptyPlace();
                correctMove = gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        showEndGameInfo(player1,player2);
    }

    private static void playerVSgreedy(GameBoard gameBoard, Player player1, Player player2){
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(player1,player2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = getMove();
                correctMove = gameBoard.makeMove(player1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
        }
        showEndGameInfo(player1,player2);
    }

    private static void randomVSgreedy(GameBoard gameBoard, Player player1, Player player2) throws InterruptedException {
        while(!gameBoard.isGameEnd()) {
            boolean correctMove;
            showGameInfo(player1,player2,moves);
            if(moves%2 == FIRST_STARTS) {
                Move nextMove = gameBoard.randomEmptyPlace();
                correctMove = gameBoard.makeMove(player1, nextMove.getX(), nextMove.getY());
            }
            else{
                Move nextMove = gameBoard.getBestGreedyMove();
                correctMove = gameBoard.makeMove(player2, nextMove.getX(), nextMove.getY());
            }
            gameBoard.printGameBoard();
            if(correctMove) moves++;
            //Thread.sleep(1000);
        }
        showEndGameInfo(player1,player2);
    }

    private static void showGameInfo(Player p1, Player p2, int move){
        System.out.println("---------------------------------------------");
        System.out.println(p2.getName()+ " [O] --- score: "+ p2.getScore());
        System.out.println(p1.getName()+ " [X] --- score: "+ p1.getScore());
        System.out.println("================== Move "+move+" ==================");
    }

    private static String showEndGameInfo(Player p1, Player p2){
        String winner = "";
        System.out.println("++++++++++++++++++ GAME OVER ++++++++++++++++++");
        if(p1.getScore() == p2.getScore()){
            winner = "Game tied";
            System.out.println(winner);
        }
        else if(p1.getScore()>p2.getScore()) {
            winner = p1.getName()+"[X] wins by difference: "+(p1.getScore()-p2.getScore());
            System.out.println(p1.getName()+"[X] wins ");
        }
        else {
            winner = p2.getName() + "[O] wins by difference: "+(p2.getScore()-p1.getScore());
            System.out.println(p2.getName() + "[O] wins ");
        }
        System.out.println(p2.getName()+ " [O] --- score: "+ p2.getScore());
        System.out.println(p1.getName()+ " [X] --- score: "+ p1.getScore());
        return winner;
    }

    private static Move getMove(){
        System.out.print("Podaj x: ");
        int x= scanner.nextInt();
        System.out.print("Podaj y: ");
        int y= scanner.nextInt();
        return new Move(x,y);
    }




}
