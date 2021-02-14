/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chess.engine.player;

import com.chess.engine.boards.Board;
import com.chess.engine.boards.Move;

/**
 *
 * @author Tea BAG
 */
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move; 
    private final MoveStatus moveStatus;
    
    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    
    
    //getters
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    
    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}
