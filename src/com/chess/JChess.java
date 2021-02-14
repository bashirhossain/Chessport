package com.chess;

import com.chess.engine.boards.Board;
import com.chess.gui.Table;

/**
 *  The driver class of the whole program.
 * This is where the standard board is created and show function is called.
 * @author tea BAG
 */
public class JChess {
    /**
     * the main function.
     * creates a standard board and prints it (testing purposes)
     * fire up the GUI
     * @param args 
     */
    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        
        System.out.println(board);
        
        Table.get().show();
    }
}
