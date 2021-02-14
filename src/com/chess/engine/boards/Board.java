package com.chess.engine.boards;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the class where details of board status is used to create objects.
 * With each update on the board, a new immutable board object is always created
 * @author Tea BAG
 */
public class Board {
    
    
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final Player currentPlayer;
    private final Pawn enPassantPawn; 
    /**
     * Keeping track of the players.
     */
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    /**
     * creates a board using the Builder pattern defined in Builder class.
     * @param builder 
     */
    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
    
        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

    }
    
    
    /**
     * Overriding the toString method for testing. 
     * @return the board's current state in a string
     */
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i< BoardUtils.NUM_TILES;i++)
        {
            final String tileText = this.gameBoard.get(i).toString(); //Each piece has their toString overriden.
            builder.append(String.format("%3s",tileText));
            if((i+1)%BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
    //getter for pieces
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    
    
    /**
     * to find the standard moves of ALL the pieces of a certain ALLIANCE(Black/white)
     * @param pieces
     * @return 
     */
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    
    /**
     * Concatenates and returns all legal moves of both sides
     * @return 
     */
    public Iterable<Move> getAllLegalMoves(){
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }
    
    //player getters
    
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    
    
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    
    /**
     * to find the collection of all the pieces of a certain ALLIANCE.
     * @param gameBoard
     * @param alliance
     * @return return all active (not taken) pieces
     */
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance){
        
        final List<Piece> activePieces = new ArrayList<>();
        
        for(final Tile tile:gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }
    public Tile getTile(final int tileCoordinate){
       
        return gameBoard.get(tileCoordinate);
    }
    
    /**
     * Make tiles through 0 to 63.
     * from Config, we get pieces associated with a particular tile
     * @param builder
     * @return 
     */
    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i<BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    
    /**
     * Places pieces in their Initial positions.
     * @return 
     */
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        
        builder.setMoveMaker(Alliance.WHITE);
        
        return builder.build();
    }
    
    
    /**
     * to check if a pawn is there on the board in an En Passant state.
     * @return The en passant pawn||null;
     */
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    
    /**
     * The class through which new boards are built
     */
    
    public static class Builder{
        
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        
        /**
         * THIS IS THE TERMINAL FUNCTION OF MOVEMAKING
         * @param piece
         * @return a new Builder object with the right updated config to create a new board
         */
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        /**
         * Sets the turn of the player who will make the next move
         * @param nextMoveMaker
         * @return 
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        
        /**
         * Builds a board based on the builder's state/config
         * @return a board created based on this Builder config
         */
        public Board build(){
            return new Board(this);
        }
        
        public void setEnPassantPawn(Pawn enPassantPawn){
            this.enPassantPawn = enPassantPawn;
        }
    }
}
