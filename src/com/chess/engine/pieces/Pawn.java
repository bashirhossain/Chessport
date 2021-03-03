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
import com.chess.engine.boards.Move.*;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tea BAG
 */
public class Pawn extends Piece{
    
    
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};
    
    
    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance, true);
    }
    
    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, pieceAlliance, isFirstMove);
    }
    
    
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE){
            
            /**
             * We are using getAlliance().getDirection() here because the black pieces will
             * move to the negative direction (downwards) while the white pieces will move 
             * towards the positive direction (upwards).
             */
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection())*currentCandidateOffset;
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
           
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                }else{
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }else if(currentCandidateOffset == 16 && this.isFirstMove()&&((BoardUtils.SEVENTH_RANK[this.piecePosition]&&this.getPieceAlliance().isBlack())||(BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite()))){
                /**
                 * PAWN JUMP!!
                 */
                
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()&&!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }else if(currentCandidateOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }else{
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }else if(board.getEnPassantPawn()!=null){
                    if(board.getEnPassantPawn().getPiecePosition() ==  (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }else if(currentCandidateOffset == 9 && !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }else{
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }else if(board.getEnPassantPawn()!=null){
                    if(board.getEnPassantPawn().getPiecePosition() ==  (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
    
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    public Piece getPromotionPiece(){
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }
    
}
