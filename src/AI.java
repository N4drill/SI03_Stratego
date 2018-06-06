import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AI extends Player{
    GameBoard TEMP_BOARD  = new GameBoard();
    Random random = new Random();

    public AI(int playerNumber, String name) {
        super(playerNumber, name);
    }

    public Move getOptimalMove(int [][] gameBoard, Player enemy , int MAX_DEPTH, boolean MINMAXMODE) {
        int [][] copyBoard = copyBoard(gameBoard);
        Player enemyClone = new Player (enemy);

        Move bestMove = getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, MAX_DEPTH);

        System.out.println("" + this.getName() + "chosen MOVE: [" + bestMove.getX() + ","+bestMove.getY()+"]");
        return bestMove;
    }

    public Move getOptimalMoveHeuristicDepthChange(int [][] gameBoard, Player enemy , int MAX_DEPTH, boolean MINMAXMODE, int moves) {
        int [][] copyBoard = copyBoard(gameBoard);
        Player enemyClone = new Player (enemy);

        int temp_depth = ( (moves* 10) / (gameBoard.length * gameBoard.length)) +1 ;
        Move bestMove = getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, temp_depth);

        System.out.println("" + this.getName() + "chosen MOVE: [" + bestMove.getX() + ","+bestMove.getY()+"]");
        return bestMove;
    }

    public Move getOptimalMoveHeuristicDepthChange2(int [][] gameBoard, Player enemy , int MAX_DEPTH, boolean MINMAXMODE, int moves) {
        int [][] copyBoard = copyBoard(gameBoard);
        Player enemyClone = new Player (enemy);

        int boardSize = gameBoard.length * gameBoard.length;
        int branch = 20; // do zmiany
        int movesLeft = boardSize - moves;
        Move bestMove  = movesLeft <= branch ? getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, movesLeft) :
                getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, MAX_DEPTH);


        System.out.println("" + this.getName() + "chosen MOVE: [" + bestMove.getX() + ","+bestMove.getY()+"]");
        return bestMove;
    }

    public Move getOptimalMoveHeuristicRandom(int [][] gameBoard, Player enemy , int MAX_DEPTH, boolean MINMAXMODE, int moves, double percent) {
        Move bestMove;
        if(moves < (gameBoard.length * gameBoard.length) *percent){
            bestMove = this.randomEmptyPlace(gameBoard,gameBoard.length);
        }else{
            int [][] copyBoard = copyBoard(gameBoard);
            Player enemyClone = new Player (enemy);

            bestMove = getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, MAX_DEPTH);
        }

        System.out.println("" + this.getName() + "chosen MOVE: [" + bestMove.getX() + ","+bestMove.getY()+"]");
        return bestMove;
    }

    public Move getOptimalMoveHeuristicGreedy(int [][] gameBoard, Player enemy , int MAX_DEPTH, boolean MINMAXMODE, int moves, double percent) {
        Move bestMove;
        if(moves < (gameBoard.length * gameBoard.length) *percent){
            bestMove = this.getBestGreedyMove(gameBoard,gameBoard.length);
        }else{
            int [][] copyBoard = copyBoard(gameBoard);
            Player enemyClone = new Player (enemy);

            bestMove = getNextMove(MINMAXMODE, copyBoard, Move.getFreeMoves(gameBoard), enemyClone, MAX_DEPTH);
        }

        System.out.println("" + this.getName() + "chosen MOVE: [" + bestMove.getX() + ","+bestMove.getY()+"]");
        return bestMove;
    }

    private Move getNextMove(boolean isMinMax, int [][] board, ArrayList<Move> avaibleMoves, Player enemy, int MAX_DEPTH) {
        int best = -100;
        Move bestMove = avaibleMoves.get(random.nextInt(avaibleMoves.size()));

        for ( int i = 0; i < avaibleMoves.size() ; i++){
            Move current = avaibleMoves.get(i);
            int scoreFromMove = this.makeMove(board, current.getX(), current.getY());
            this.addScore(scoreFromMove);
            current.setScore(scoreFromMove);
            int futureValue = best;
            if(isMinMax){
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                futureValue = minMaxAlgorith(board, 0, MAX_DEPTH, false, enemy, otherPossibleMoves);
            }
            else // ALFA BETA...
            {
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                futureValue = minMaxAlfaBetaAlgorith(board, 0, MAX_DEPTH, false, enemy, otherPossibleMoves, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            this.undoMove(board,current.getX(),current.getY());
            this.reduceScore(current.getScore());
            if(futureValue > best ){
                best = futureValue;
                bestMove = current;
            }
        }
        return bestMove;
    }

    private int minMaxAlgorith(int[][] board, int actualDepth, int max_depth, boolean maximize, Player enemy, ArrayList<Move> avaibleMoves) {
        if(actualDepth == max_depth || TEMP_BOARD.isGameEnd(board)) {
            if(maximize) {
                //return this.evaluateScore(enemy) - actualDepth;
                return this.evaluateScore(enemy, this.getScore()) - actualDepth;
            }else {
                //return this.evaluateScore(enemy) + actualDepth;
                return this.evaluateScore(enemy, this.getScore()) + actualDepth;
            }
        }

        if(maximize){
            int best = Integer.MIN_VALUE;
            for(int i = 0 ; i < avaibleMoves.size(); i++){
                Move current = avaibleMoves.get(i);
                int scoreFormMove = this.makeMove(board, current.getX(),current.getY());
                this.addScore(scoreFormMove);
                current.setScore(scoreFormMove);
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                best = Math.max(best, minMaxAlgorith(board, actualDepth+1, max_depth, false, enemy, otherPossibleMoves));
                this.undoMove(board, current.getX(), current.getY());
                this.reduceScore(scoreFormMove);
            }
            return best;
        }else{
            int best = Integer.MAX_VALUE;
            for(int i = 0 ; i < avaibleMoves.size(); i++){
                Move current = avaibleMoves.get(i);
                int scoreFromMove= enemy.makeMove(board,current.getX(),current.getY());
                enemy.addScore(scoreFromMove);
                current.setScore(scoreFromMove);
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                best = Math.min(best, minMaxAlgorith(board, actualDepth+1, max_depth, true, enemy, otherPossibleMoves));
                enemy.reduceScore(current.getScore());
                enemy.undoMove(board, current.getX(), current.getY());
            }
            return best;
        }
    }

    private int minMaxAlfaBetaAlgorith(int[][] board, int actualDepth, int max_depth, boolean maximize, Player enemy, ArrayList<Move> avaibleMoves, int alphaValue, int betaValue) {
        if(actualDepth == max_depth || TEMP_BOARD.isGameEnd(board)) {
            if(maximize) {
                return this.evaluateScore(enemy) - actualDepth;
            }else {
                return this.evaluateScore(enemy) + actualDepth;
            }
        }

        if(maximize){
            int best = Integer.MIN_VALUE;
            for(int i = 0 ; i < avaibleMoves.size(); i++){
                Move current = avaibleMoves.get(i);
                int scoreFormMove = this.makeMove(board, current.getX(),current.getY());
                this.addScore(scoreFormMove);
                current.setScore(scoreFormMove);
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                best = Math.max(best, minMaxAlfaBetaAlgorith(board, actualDepth+1, max_depth, false, enemy, otherPossibleMoves, alphaValue, betaValue));
                this.undoMove(board, current.getX(), current.getY());
                this.reduceScore(scoreFormMove);
                alphaValue = Math.max(best , alphaValue);
                if ( betaValue <= alphaValue) break;
            }
            return best;
        }else{
            int best = Integer.MAX_VALUE;
            for(int i = 0 ; i < avaibleMoves.size(); i++){
                Move current = avaibleMoves.get(i);
                int scoreFromMove= enemy.makeMove(board,current.getX(),current.getY());
                enemy.addScore(scoreFromMove);
                current.setScore(scoreFromMove);
                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
                otherPossibleMoves.remove(i);
                best = Math.min(best, minMaxAlfaBetaAlgorith(board, actualDepth+1, max_depth, true, enemy, otherPossibleMoves,alphaValue,betaValue));
                enemy.reduceScore(current.getScore());
                enemy.undoMove(board, current.getX(), current.getY());

                betaValue = Math.min(best , betaValue);
                if ( betaValue <= alphaValue) break;
            }
            return best;
        }


    }


//    private int minMaxAlfaBetaAlgorith2(int[][] board, int actualDepth, int max_depth, boolean maximize, Player enemy, ArrayList<Move> avaibleMoves, int alphaValue, int betaValue, int moves) {
//        if(actualDepth == max_depth || TEMP_BOARD.isGameEnd(board)) {
//            if(maximize) {
//                return this.evaluateScore(enemy) - actualDepth;
//            }else {
//                return this.evaluateScore(enemy) + actualDepth;
//            }
//        }
//
//
//        if(maximize){
//            int best = Integer.MIN_VALUE;
//            for(int i = 0 ; i < avaibleMoves.size(); i++){
//                Move current = avaibleMoves.get(i);
//                int scoreFormMove = this.makeMove(board, current.getX(),current.getY());
//                this.addScore(scoreFormMove);
//                current.setScore(scoreFormMove);
//                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
//                otherPossibleMoves.remove(i);
//                best = Math.max(best, minMaxAlfaBetaAlgorith2(board, actualDepth+1, temp_depth, false, enemy, otherPossibleMoves, alphaValue, betaValue,moves ));
//                this.undoMove(board, current.getX(), current.getY());
//                this.reduceScore(scoreFormMove);
//                alphaValue = Math.max(best , alphaValue);
//                if ( betaValue <= alphaValue) break;
//
//            }
//            return best;
//        }else{
//            int best = Integer.MAX_VALUE;
//            for(int i = 0 ; i < avaibleMoves.size(); i++){
//                Move current = avaibleMoves.get(i);
//                int scoreFromMove= enemy.makeMove(board,current.getX(),current.getY());
//                enemy.addScore(scoreFromMove);
//                current.setScore(scoreFromMove);
//                ArrayList<Move> otherPossibleMoves = new ArrayList<>(avaibleMoves);
//                otherPossibleMoves.remove(i);
//                best = Math.min(best, minMaxAlfaBetaAlgorith2(board, actualDepth+1, temp_depth, true, enemy, otherPossibleMoves,alphaValue,betaValue, moves));
//                enemy.reduceScore(current.getScore());
//                enemy.undoMove(board, current.getX(), current.getY());
//
//                betaValue = Math.min(best , betaValue);
//                if ( betaValue <= alphaValue) break;
//            }
//            return best;
//        }
//
//
//    }


    private int [][] copyBoard ( int [][] board){
        int [][] boardCopy = new int [board.length][board.length];
        for ( int row= 0; row < board.length ; row ++){
            System.arraycopy(board[row], 0, boardCopy[row], 0, board.length);
        }
        return boardCopy;
    }
}
