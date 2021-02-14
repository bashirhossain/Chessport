package com.chess.engine;

import com.chess.engine.boards.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;


/**
 * Signifies which team the piece belongs to
 * Black or white
 * @author tea BAG
 */
public enum Alliance {
    WHITE {
        @Override
        public int getDirection(){
            
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }
    },
    BLACK {
        @Override
        public int getDirection(){
            /**
             * Black moves towards greater tile IDs.
             */
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;         
        }
        
        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }
    };
    
    /**
     * A sense of direction for the pawns (While other pieces can move many directions, pawns can only move "Forward")
     * @return -1 for white || 1 for black
     */
    public abstract int getDirection();
    /**
     * Returns a boolean of alliance confirmation.
     * Same for isBlack()
     * @return the type of the Alliance 
     */
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    
    /**
     * Opposite of getDirection()
     * @return the opposite of direction
     */
    public abstract int getOppositeDirection();
    /**
     * Checks if the pawn is on the opposite end of the board
     * @param position
     * @return boolean
     */
    public abstract boolean isPawnPromotionSquare(int position);
    
    /**
     * Chooses current player
     * @param whitePlayer
     * @param blackPlayer
     * @return player object
     */
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
