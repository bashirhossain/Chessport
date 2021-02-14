/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chess.engine.player.ai;

import com.chess.engine.boards.Board;
import com.chess.engine.boards.Move;
import com.chess.engine.player.MoveTransition;

/**
 *
 * @author Tea BAG
 */
public class MiniMax implements MoveStrategy{
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    
    public MiniMax(final int searchDepth){
        this.boardEvaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
    }
    
    @Override 
    public String toString(){
        return "MiniMax";
    }
    
    /**
     * executes minimax to find the right move
     * @param board
     * @return best possible move
     */
    @Override
    public Move execute(Board board) {
        
        final long startTime = System.currentTimeMillis();
        
        Move bestMove = null;
        
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        
        //System.out.println(board.currentPlayer() +  " THINKING with depth = "+this.searchDepth);
        System.out.println(board.currentPlayer() +  " THINKING");
        
        final int numMoves = board.currentPlayer().getLegalMoves().size();
        
        for(final Move move : board.currentPlayer().getLegalMoves()){
           final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
           if(moveTransition.getMoveStatus().isDone()){
               currentValue = board.currentPlayer().getAlliance().isWhite()?
                       min(moveTransition.getTransitionBoard(), this.searchDepth -1):
                       max(moveTransition.getTransitionBoard(), this.searchDepth -1);
               if(board.currentPlayer().getAlliance().isWhite() && highestSeenValue<=currentValue){
                   highestSeenValue = currentValue;
                   bestMove = move;
               }else if(board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){
                   lowestSeenValue = currentValue;
                   bestMove = move;
               }
           }
        }
        
        
        final long executionTile = System.currentTimeMillis() - startTime;
        
        
        return bestMove;
    }
    
    /**
     * We check if the game's ended
     * @param board
     * @return boolean
     */
    private static boolean isEndGameScenario(final Board board){
        return board.currentPlayer().isInCheckMate()||board.currentPlayer().isInStaleMate();
    }
    
    public int min(final Board board, final int depth){
        if(depth == 0||isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), depth-1);
                if(currentValue<=lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }
        
        return lowestSeenValue;
    }
    
    public int max(final Board board, final int depth){
        if(depth == 0||isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), depth-1);
                if(currentValue>=highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        
        return highestSeenValue;
    }
    
    
    
}
