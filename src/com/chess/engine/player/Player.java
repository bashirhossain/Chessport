/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.boards.Board;
import com.chess.engine.boards.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tea BAG
 */
public abstract class Player {
    
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
    
    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        //this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves,opponentMoves)));
        this.legalMoves = legalMoves;
        
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
    }
    
    /**
     * Used to find all the moves that might be a threat to a certain tile.
     * @param piecePosition
     * @param moves
     * @return 
     */
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){
        final List<Move> attackMoves = new ArrayList<>();
        
        for(final Move move:moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
    
    /**
     * Locates the king after every turn.
     * @return King
     */
    private King establishKing(){
        for(final Piece piece: getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
           
        }
        
        
               
      throw new RuntimeException("Should Not Reach Here!");
    }
    
    public King getPlayerKing(){
        return this.playerKing;
    }
    
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    public abstract Collection<Piece> getActivePieces();
    
    public abstract Alliance getAlliance(); 
    
    public abstract Player getOpponent();
    
    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    
    public boolean isInCheck(){
        return this.isInCheck;
    }
    
    
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    
    public boolean isCastled(){
        return false;
    }
    
    /**
     * Makes a move to see if it's Legal
     * @param move
     * @return Movetransition object (MoveStatus included)
     */
    public MoveTransition makeMove(final Move move){
        
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        
        final Board transitionBoard = move.execute();
        
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());
        
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
    
    /**
     * When in check
     * @return 
     */
    protected boolean hasEscapeMoves() {
        for(final Move move:this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Will check the castle moves that are possible to be made by the player
     * @param playerLegals
     * @param opponentsLegals
     * @return collection of castle moves
     */
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}
