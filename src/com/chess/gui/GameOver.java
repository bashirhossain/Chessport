/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chess.gui;

import com.chess.engine.player.Player;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;

/**
 *
 * @author Tea BAG
 */
public class GameOver {
    
    public static void createWindow(Player currentPlayer){
        JFrame frame = new JFrame("Game Over");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel textLabel = new JLabel(currentPlayer.getOpponent().toString()+" is in Checkmate", SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(311,111));
        frame.getContentPane().add(textLabel, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    
}
