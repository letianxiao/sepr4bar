/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

/**
 *
 * @author eduard
 */

import dab.engine.simulator.Simulator;

import dab.gui.sound.Sounds;
import java.awt.Color;

import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 * The main menu for the game, has new game, load game, help and quit buttons.
 * New game points onto an instance of Player.
 * Load Game displays a menu of existing save games, and loads a selected game.
 * Help ...
 * Quit exits the JRE.
 * 
 * @author Team Haddock
 *
 */
public class DaMMenu extends JPanel {
    public boolean pressed = false;
    private Simulator simulator;
    private Sounds music;

    public DaMMenu() {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JButton new_game = new JButton("asdf");
        new_game.setContentAreaFilled(true);
        new_game.setBorderPainted(true);
        //new_game.setBorder(BorderFactory.createEmptyBorder());
        //new_game.setContentAreaFilled(false);
        //new_game.setBorderPainted(false);
        //new_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        /*new_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                //create a new simulator and let the player enter his name
                System.err.println("create new game mfka!");
            }
        });*/

        /*JButton load_game = new JButton();
        load_game.setContentAreaFilled(false);
        load_game.setBorderPainted(false);
        load_game.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton help = new JButton();
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        help.setAlignmentX(Component.CENTER_ALIGNMENT);
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                System.err.println("Show menu");
            }
        });

        JButton exit_game = new JButton();
        exit_game.setContentAreaFilled(false);
        exit_game.setBorderPainted(false);
        exit_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        add(Box.createRigidArea(new Dimension(0,280)));
        // add all of the buttons to the Menu
        add(new_game);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(load_game);
        add(help);
        add(exit_game);
        setBackground(Color.BLACK);
        setVisible(true);*/
        setBackground(Color.BLACK);
        add(new_game);
        
    }
}
