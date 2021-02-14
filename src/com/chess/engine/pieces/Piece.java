package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.boards.Board;
import com.chess.engine.boards.Move;
import java.util.Collection;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tea BAG
 */
public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    private final int cacheHashCode;
    
    
    protected final boolean isFirstMove;
    /**
     * Constructor for pieces.
     * @param piecePosition
     * @param pieceAlliance 
     */
    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cacheHashCode = computeHashCode();
    }
    
    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31*result + pieceAlliance.hashCode();
        result = 31*result + piecePosition;
        result = 31*result + (isFirstMove?1:0);
        return result;
    }
    @Override
    public boolean equals(final Object other)
    {
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()&&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }
    
    @Override
    public int hashCode(){
        return this.cacheHashCode;
    }
    public int getPiecePosition(){
        return this.piecePosition;
    }
    
    /**
     * The function that calculates the legal moves for all pieces.
     * @param board
     * @return the positions on the board where we can move to
     */
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    
    
    public abstract Piece movePiece(Move move);
    /**
     * 
     * @return the piece's team
     */
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    
    public PieceType getPieceType(){
        return this.pieceType;
    }
    
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    
    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }
    
    public enum PieceType{
        
        
        PAWN(100, "P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500,"R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(10000,"K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };
        private String pieceName;
        private int pieceValue;
        
        PieceType(final int pieceValue, final String pieceName){
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }
        
        @Override
        public String toString(){
            return this.pieceName;
        }
        
        public abstract boolean isKing();

        public abstract boolean isRook();
        
        public int getPieceValue(){
            return this.pieceValue;
        }
    } 
}
