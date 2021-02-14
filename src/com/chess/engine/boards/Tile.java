package com.chess.engine.boards;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * <b>A class for each tile</b>
 * @author Tea BAG
 */
public abstract class Tile {
    
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }
    protected final int tileCoordinate;
    
    /**
     * The function to initiate all empty tiles at the beginning of execution.
     * @return A map containing all tiles
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles()
    {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        
        return ImmutableMap.copyOf(emptyTileMap);
    }
    
    /**
     * The only way to create the tiles now that Tile class is immutable.
     * @param tileCoordinate
     * @param piece
     * @return 
     */
    public static Tile createTile(final int tileCoordinate, final Piece piece)
    {
        return (piece!=null)? new OccupiedTile(tileCoordinate,piece):EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    private Tile(final int tileCoordinate)
    {
        this.tileCoordinate = tileCoordinate;
    }
    
    public abstract boolean isTileOccupied();
    
    public abstract Piece getPiece();
    
    /**
     * Class for tiles which are empty.
     */
    public static final class EmptyTile extends Tile{ 
        private EmptyTile(final int coordinate){
            super(coordinate);
        }
        
        /**
         * print a "-" for an empty tile.
         * @return 
         */
        @Override
        public String toString(){
            return "-";
        }
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        
        @Override
        public Piece getPiece(){
            return null;
        }
    }
    
    /**
     * Tile inherent child class for tiles with pieces.
     */
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        
        private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile)
        {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        
        /**
         * print the name of the piece in an occupied tile.
         * Caps for white alliance, small letter for black alliance.
         * @return 
         */
        
        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack()?getPiece().toString().toLowerCase():getPiece().toString();
        }
        
        @Override
        public boolean isTileOccupied(){
            return true;
        }
        
        @Override
        public Piece getPiece()
        {
            return this.pieceOnTile;
        }
    }
}
