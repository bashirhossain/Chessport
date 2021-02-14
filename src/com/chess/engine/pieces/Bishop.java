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
import com.chess.engine.boards.Move.MajorAttackMove;
import com.chess.engine.boards.Move.MajorMove;
import com.chess.engine.boards.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Winter
 */
public class Bishop extends Piece{
    
    /**
     * Diagonal movement.
     */
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-7,7,9};
    
    public Bishop(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance, true);
    }
    
    public Bishop(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int candidateCoordinateOffset:CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                
                if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)||isEighthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                    if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    /**
                     * When the tile is ON THE BOARD
                     */
                        if(!candidateDestinationTile.isTileOccupied()){
                            /**
                             * when the tile is EMPTY
                             */
                            legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                        }
                        else{
                            final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                            final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                            if(this.pieceAlliance!=pieceAlliance){
                                /**
                                 * When the piece on the tile is of the Opposite team
                                 */
                                    legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                                }
                                break;
                        }
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
        return PieceType.BISHOP.toString();
    }
    /**
     * Exclusion checker methods.
     * each method checks whether the candidate offset and current position are compatible or do they trigger an exceptional case
     * @param currentPosition
     * @param candidateOffset
     * @return 
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
        
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7|| candidateOffset == 9);
        
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    
}
