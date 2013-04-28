package dab.gui;

import dab.engine.persistence.FileSystem;
import dab.engine.simulator.CannotControlException;
import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.FailMode;
import dab.engine.simulator.KeyNotFoundException;
import dab.engine.simulator.Simulator;
import dab.engine.simulator.UserCommands;

import dab.gui.sound.Sounds;

import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;

import dab.engine.seprphase2.GameOverException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import dab.bigBunny.App.GamePanel;
import dab.bigBunny.App.MainWindow;
import dab.bigBunny.App.SinglePlayerPanel;
import java.awt.FlowLayout;

public class SinglePlayerInterface extends JPanel {

    private Simulator simulator;
    private MainWindow mainWindow;
    private Sounds music;
    public final int MAX_SIZE_WIDTH = 1366;
    public final int MAX_SIZE_HEIGHT = 768;
    private SinglePlayerPanel gamePanel;
    private ObamaPanel obamaPanel;
    private ButtonPanel buttonPanel;
    private InfoPanel infoPanel;
    private Timer animator;
    public static final Pressure CONDENSER_WARNING_PRESSURE = new Pressure(25530000);
    private GameOver gameOver;

    public static SinglePlayerInterface instance() {
        throw new UnsupportedOperationException();

    }

    public SinglePlayerInterface(MainWindow mainWindow, Simulator simulator) {
        this.mainWindow = mainWindow;
        this.simulator = simulator;

        music = new Sounds("resources/music/backgroundSound.wav", true);
        //music.start();
        
        //setMinimumSize(new Dimension(MAX_SIZE_WIDTH, MAX_SIZE_HEIGHT));
        //setLayout(new BorderLayout(0, 0));
        
        setupPanels();
        setupTimer();
        setupKeyboardActions();
        
        // rock and roll baby!
        animator.start();
    }
    
    private void setupPanels() {
        setLayout(new BorderLayout());
        JSplitPane topLevelSplitPane, leftPane, rightPane;
        
        topLevelSplitPane = new JSplitPane();
        topLevelSplitPane.setDividerSize(5);
        //topLevelSplitPane.setResizeWeight(0.2);
        
        leftPane = new JSplitPane();
        leftPane.setDividerSize(5);
        leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        
        rightPane = new JSplitPane();
        rightPane.setDividerSize(5);
        rightPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightPane.setResizeWeight(0.5);
        
        gamePanel = new SinglePlayerPanel(simulator);
        obamaPanel = new ObamaPanel(simulator);
        infoPanel = new InfoPanel(simulator);
        buttonPanel = new ButtonPanel(simulator);
        
                
        leftPane.setLeftComponent(gamePanel);
        leftPane.setRightComponent(obamaPanel);
        rightPane.setLeftComponent(infoPanel);
        rightPane.setRightComponent(buttonPanel);        
        
        topLevelSplitPane.setLeftComponent(leftPane);
        topLevelSplitPane.setRightComponent(rightPane);
        add(topLevelSplitPane);
        
        //topLevelSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //topLevelSplitPane.setPreferredSize(new Dimension(MAX_SIZE_WIDTH, MAX_SIZE_HEIGHT));
        //topLevelSplitPane.setMinimumSize(new Dimension(MAX_SIZE_WIDTH, MAX_SIZE_HEIGHT));
        //topLevelSplitPane.setResizeWeight(0.73);
        

        
        //leftPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //leftPane.setResizeWeight(0.8);
        
        


        //rightSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //rightPane.setResizeWeight(0.4);

    }

    private void setupKeyboardActions() {
        // register an escape press listener
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        registerKeyboardAction(new EscapeListener(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void showGameOverScreen() {
        animator.stop();
        ImageIcon icon = new ImageIcon("resources/endGame.gif");
        Image img = icon.getImage();
        // load the game over gif and scale it to fit in the game over dialog
        Image newimg = img.getScaledInstance(330, 300, java.awt.Image.SCALE_DEFAULT);
        ImageIcon newIcon = new ImageIcon(newimg);
        //create an option pane for the game over dialog
        final JOptionPane optionPane = new JOptionPane(
                "The Reactor has failed, " + simulator.getUsername() + "! \n" + "You generated "
                + simulator.energyGenerated() + " of power." + "\n"
                + "Would you like to start a new game?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, newIcon);
        //create a new gameOver dialog, passing the option pane to it
        gameOver = new GameOver(optionPane);
        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                int value = (Integer) e.getNewValue();
                //if the user clicks the "Yes" option
                if ((value == 0)) {
                    //create a new simulator with the old username
                    String old_username = simulator.getUsername();
                    Simulator new_simulator = new Simulator();
                    new_simulator.setUsername(old_username);
                    mainWindow.startSinglePlayer(new_simulator);
                }
                if ((value == 1)) {
                    mainWindow.showMenu();
                }
            }
        });

    }

    private void setupTimer() {

        ActionListener taskStep = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    simulator.step();
                    infoPanel.update();
                    obamaPanel.update();
                    gamePanel.updateComponents();
                    buttonPanel.update();

                    // soon there will be no need to screenUpdate
                    //screenUpdate();
                    //repaint();
                } catch (GameOverException e) {

                    // stop the game loop when game over
                    animator.stop();
                    music.interrupt();
                    showGameOverScreen();
                }

            }
        };
        //game loop updates every 100 ms
        animator = new Timer(100, taskStep);
    }

    public static void rewrite() {
        throw new UnsupportedOperationException();
    }

    public void screenUpdate() {

        if (getFailed("Turbine") || (simulator.getSoftFailReport().getFailMode() != FailMode.WORKING)) {
            // if there is a software failure or the turbine has failed,
            // change the control rod slider to its actual position
            // why? no idea....
            buttonPanel.setSliderValue((int) simulator.controlRodPosition().points());
        }
    }

    private boolean getFailed(String component) {
        if (simulator.listFailedComponents().length > 0) {
            for (String ChangingComponent : simulator.listFailedComponents()) {
                if (ChangingComponent.equals(component)) {
                    return true;
                }
            }
        }
        return false;
    }

    //@Override
    public void update(UserCommands command, int parameter) {
        try {
            switch (command) {
                case TURNON:
                    simulator.changePumpState(parameter, true);
                    break;
                case TURNOFF:
                    simulator.changePumpState(parameter, false);
                    break;
                case OPEN:
                    simulator.changeValveState(parameter, true);
                    break;
                case CLOSE:
                    simulator.changeValveState(parameter, false);
                    break;
                case MOVE:
                    simulator.moveControlRods(new Percentage(parameter));
                    break;
            }
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        } catch (CannotControlException e) {
        }

        screenUpdate();
    }

    class EscapeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // pause the game loop when esc is pressed
            animator.stop();
            JMenuItem new_game = new JMenuItem();
            new_game.setOpaque(true);
            new_game.setIconTextGap(0);
            new_game.setContentAreaFilled(false);
            new_game.setIcon(new ImageIcon("resources/menu/NewGameButton.png"));
            new_game.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    //initialize a new game with the old username
                    String old_username = simulator.getUsername();
                    Simulator new_simulator = new Simulator();
                    new_simulator.setUsername(old_username);
                    mainWindow.startSinglePlayer(new_simulator);
                }
            });
            JMenuItem save_menu = new JMenuItem();
            save_menu.setOpaque(true);
            save_menu.setIconTextGap(0);
            save_menu.setContentAreaFilled(false);
            save_menu.setIcon(new ImageIcon("resources/menu/saveButton.png"));
            save_menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        simulator.saveGame();
                        JOptionPane.showMessageDialog(null, "Game Saved");
                    } catch (JsonProcessingException e1) {
                        e1.printStackTrace();
                    }

                }
            });
            JMenuItem load_menu = new JMenuItem();
            load_menu.setOpaque(true);
            load_menu.setIconTextGap(0);
            load_menu.setContentAreaFilled(false);
            load_menu.setIcon(new ImageIcon("resources/menu/LoadGameButton.png"));
            load_menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<String> saved_games_array = new ArrayList<String>();
                    String[] saved_games = FileSystem.listSaveGames();
                    for (String game : saved_games) {
                        String[] bits = game.split("\\.");
                        game = game.replace(".", "?");
                        Timestamp t = new Timestamp(Long.parseLong(bits[3]));
                        Date d = new Date(t.getTime());
                        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        //an array of the saved games in the format : username/date of save
                        saved_games_array.add(bits[2] + " " + date.format(d));
                    }
                    JMenuItem save_menu = new JMenuItem();
                    save_menu.setOpaque(true);
                    save_menu.setIconTextGap(0);
                    save_menu.setContentAreaFilled(false);
                    save_menu.setIcon(new ImageIcon("resources/menu/LoadGameButton.png"));

                    //a jlist of saved games to be used
                    final JList sampleJList = new JList(saved_games_array.toArray(new String[saved_games_array.size()]));
                    sampleJList.setVisibleRowCount(8);
                    //make a the jlist scrollable
                    JScrollPane listPane = new JScrollPane(sampleJList);

                    final JPopupMenu popupMenu = new JPopupMenu();

                    popupMenu.setBorder(BorderFactory.createEmptyBorder());
                    JButton load = new JButton("Load");
                    load.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //load the selected game
                            simulator.loadGame(sampleJList.getSelectedIndex());
                            popupMenu.setVisible(false);
                            //set the control rods in the control panel to the new rods value
                            buttonPanel.setSliderValue(2 * Integer.parseInt(simulator.controlRodPosition().toString()));
                            animator.start();
                        }
                    });
                    getParent().add(popupMenu);
                    popupMenu.add(listPane);
                    popupMenu.add(load);
                    add(popupMenu);
                    popupMenu.show(SinglePlayerInterface.this, 650, 350);
                    animator.stop();
                }
            });

            /*
             * The item on the popup menu which will offer information on how to play the game.
             * The settings draw the button.
             */
            JMenuItem help_menu = new JMenuItem();
            help_menu.setOpaque(true);
            help_menu.setIconTextGap(0);
            help_menu.setContentAreaFilled(false);
            help_menu.setIcon(new ImageIcon("resources/menu/HelpButton.png"));

            help_menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //pause game loop
                    animator.stop();
                    final JPanel helpPanel = new JPanel();
                    helpPanel.setLayout(new BorderLayout());
                    JLabel helpImage = new JLabel();
                    helpImage.setIcon(new ImageIcon("resources/menu/HelpImage.png"));
                    helpImage.setBounds(0, 0, 700, 500);
                    setVisible(false);
                    JButton back = new JButton("Back");
                    back.setBackground(Color.PINK);
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // go back to the game and resume the game loop
                            setVisible(true);
                            helpPanel.setVisible(false);
                            animator.start();
                        }
                    });
                    back.setBounds(10, 10, 70, 30);
                    helpPanel.add(back, BorderLayout.CENTER);
                    helpPanel.add(helpImage, BorderLayout.CENTER);
                    getParent().add(helpPanel);
                    getParent().setVisible(true);
                }
            });
            JMenuItem exit_menu = new JMenuItem();
            exit_menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.close();
                }
            });
            exit_menu.setIconTextGap(0);
            exit_menu.setOpaque(true);
            exit_menu.setContentAreaFilled(false);
            exit_menu.setIcon(new ImageIcon("resources/menu/QuitButton.png"));
            JPopupMenu popupMenu = new JPopupMenu() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void paintComponent(Graphics g) {
                    Image img = new ImageIcon("resources/menu/Background.png").getImage();
                    g.drawImage(img, 0, 0, 500, 500, null);
                }
            };
            popupMenu.setBorder(BorderFactory.createEmptyBorder());
            popupMenu.menuSelectionChanged(false);
            //add all of the menu items to the pop up menu
            popupMenu.add(new_game);
            popupMenu.add(save_menu);
            popupMenu.add(load_menu);
            popupMenu.add(help_menu);
            popupMenu.add(exit_menu);
            add(popupMenu);

            //show the popupMenu (when esc is pressed)
            popupMenu.show(SinglePlayerInterface.this, 300, 200);

            popupMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
                    //start the game loop when the in game pause menu is not visible
                    animator.start();
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent arg0) {
                }
            });
        }
    }
}
