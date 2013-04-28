package dab.gui;

import dab.engine.persistence.FileSystem;

import dab.engine.simulator.Simulator;

import dab.gui.sound.Sounds;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
public class Menu extends JPanel {
    public boolean pressed = false;
    private Simulator simulator;
    private Sounds music;

    public Menu() {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        music = new Sounds("resources/music/IntroMusic.wav", true);
        music.start();
        JButton new_game = new JButton(new ImageIcon("resources/menu/NewGameButton.png"));
        new_game.setBorder(BorderFactory.createEmptyBorder());
        new_game.setContentAreaFilled(false);
        new_game.setBorderPainted(false);
        new_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        new_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                //create a new simulator and let the player enter his name
                simulator = new Simulator();
                setVisible(false);
                getParent().add(new Player(simulator, music));
                getParent().setVisible(true);
            }
        });

        JButton load_game = new JButton(new ImageIcon("resources/menu/LoadGameButton.png"));
        load_game.setContentAreaFilled(false);
        load_game.setBorderPainted(false);
        load_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        load_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                ArrayList<String> saved_games_array = new ArrayList<String>();
                String [] saved_games = FileSystem.listSaveGames();
                //list through the list of saved games and change each saved game's name
                // to the format -> player's name, date of save
                for (String game : saved_games) {
                    String[] bits = game.split("\\.");
                    game = game.replace(".", "?");
                    Timestamp t = new Timestamp(Long.parseLong(bits[3]));
                    Date d = new Date(t.getTime());
                    SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                    saved_games_array.add(bits[2]+" "+ date.format(d));
                }

                JMenuItem save_menu = new JMenuItem();
                save_menu.setOpaque(true);
                save_menu.setIconTextGap(0);
                save_menu.setContentAreaFilled(false);
                save_menu.setIcon(new ImageIcon("resources/menu/LoadGameButton.png"));

                //fill the JList with the saved games from the array
                final JList sampleJList = new JList(saved_games_array.toArray(new String[saved_games_array.size()]));
                sampleJList.setVisibleRowCount(8);
                JScrollPane listPane = new JScrollPane(sampleJList);

                final JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.setBorder(BorderFactory.createEmptyBorder());
                JButton load = new JButton("Load");
                load.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e){
                        music.interrupt();
                        //create a new simulator and change its state
                        //according to the loaded game
                        Simulator simulator = new Simulator();
                        simulator.loadGame(sampleJList.getSelectedIndex());
                        setVisible(false);
                        //initialize the UI
                        SinglePlayerInterface ic = SinglePlayerInterface.instance();
                        ic.setup(simulator);
                        getParent().add(ic);
                        getParent().setVisible(true);
                        popupMenu.setVisible(false);
                    }
                });
                getParent().add(popupMenu);
                popupMenu.add(listPane);
                popupMenu.add(load);
                add(popupMenu);
                popupMenu.show(Menu.this, 650, 350);
            }
        });


        JButton help = new JButton(new ImageIcon("resources/menu/HelpButton.png"));
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);
        help.setAlignmentX(Component.CENTER_ALIGNMENT);
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                getParent().add(new NewHelpScreen(Menu.this));
                getParent().setVisible(true);
            }
        });

        JButton exit_game = new JButton(new ImageIcon("resources/menu/QuitButton.png"));
        exit_game.setContentAreaFilled(false);
        exit_game.setBorderPainted(false);
        exit_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_game.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                music.interrupt();
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
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
        Image img = new ImageIcon("resources/menu/Background.png").getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(),  null);
    }
}
