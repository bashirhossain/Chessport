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
import com.chess.engine.boards.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tea BAG
 */
public class King extends Piece{
    
    /**
     * A square in all directions.
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    public King(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KING,piecePosition, pieceAlliance, true);
    }
    
    public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KING,piecePosition, pieceAlliance, isFirstMove);
    }
    
    /**
     * The legalMoves are quite similar to the knight's. 
     * Only difference in CANDIDATE_MOVE_COORDINATE.
     * @param board
     * @return 
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE){
            
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)||isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)){
                continue;
            }
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
            {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                /**
                 * When the tile is ON THE BOARD
                 */
                if(!candidateDestinationTile.isTileOccupied()){
                    /**
                     * when the tile is EMPTY
                     */
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
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
        return PieceType.KING.toString();
    }
    /**
     * Edge cases.
     * @param currentPosition
     * @param candidateOffset
     * @return 
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9||candidateOffset == -1||candidateOffset == 7);
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7||candidateOffset == 1||candidateOffset == 9);
    }
    
    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
}
