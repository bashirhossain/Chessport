/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.boards.Board;
import com.chess.engine.boards.BoardUtils;
import com.chess.engine.boards.Move;
import com.chess.engine.boards.Move.AttackMove;
import com.chess.engine.boards.Move.MajorMove;
import com.chess.engine.boards.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tea BAG
 */
public class Knight extends Piece{
    
    /**
     * The positions in respect to the current position to where a knight can move
     */
    private final static int[] CALCULATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
    
    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, true);
    }
    
    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, isFirstMove);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board){
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CALCULATE_MOVE_COORDINATES){
            
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)||isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)||isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)||isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
                
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            
                /**
                 * When the tile is ON THE BOARD
                 */
                if(!candidateDestinationTile.isTileOccupied()){
                    /**
                     * when the tile is EMPTY
                     */
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance!=pieceAlliance){
                        /**
                         * When the piece on the tile is of the Opposite team
                         */
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    /**
     * To show it as a string
     * @return 
     */
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
    /**
     * Exclusion checker methods.
     * each method checks whether the candidate offset and current position are compatible or do they trigger an exceptional case
     * @param currentPosition
     * @param candidateOffset
     * @return 
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17||candidateOffset == -10||candidateOffset == 6||candidateOffset == -15);
    }
    
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10||candidateOffset == 6);
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -10||candidateOffset == 6);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15||candidateOffset == -6||candidateOffset == 10||candidateOffset == 17);
    }
    
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
}
