package dab.gui;

import dab.engine.simulator.Simulator;
import dab.gui.sound.Sounds;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The player menu, in which a user is prompted to enter their name before continuing.
 * The name is used to address the player and in save file names.
 * 
 * @author Team Haddock
 *
 */
public class Player extends JPanel {

    private JTextField enter_name;
    private JLabel name_label;
    private Simulator simulator;
    private Sounds music;

    public Player(final Simulator simulator,final Sounds music){
        this.simulator = simulator;
        enter_name = new JTextField(20);
        enter_name.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
        enter_name.setPreferredSize(new Dimension(150, 50));
        enter_name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    //if text is empty
                    if(enter_name.getText().length()==0){
                        JOptionPane.showMessageDialog(null,"Name can't be empty!");
                    } else {
                        simulator.setUsername(enter_name.getText());
                        setVisible(false);
                        music.interrupt();
                        SinglePlayerInterface ic = SinglePlayerInterface.instance();
                        ic.setup(simulator);
                        getParent().add(ic);
                        getParent().setVisible(true);
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        //ok button has the same functionality as when the user presses enter
        JButton ok = new JButton();
        ok.setIcon(new ImageIcon("resources/menu/ok.png"));
        ok.setBorder(BorderFactory.createEmptyBorder());
        ok.setContentAreaFilled(false);
        ok.setBorderPainted(false);
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(enter_name.getText().length()==0){
                    JOptionPane.showMessageDialog(null,"Name can't be empty!");
                } else {
                    //set the user name and initialize a new Interface
                    music.interrupt();
                    simulator.setUsername(enter_name.getText());
                    setVisible(false);
                    SinglePlayerInterface.rewrite();
                    SinglePlayerInterface.instance().setup(simulator);
                    getParent().add(SinglePlayerInterface.instance());
                    getParent().setVisible(true);
                }
            }
        });

        name_label = new JLabel("Please enter your name");
        name_label.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
        name_label.setForeground(Color.ORANGE);
        add(name_label);
        add(Box.createRigidArea(new Dimension(0,700)));
        add(enter_name);
        add(ok);
    }

    @Override
    public void paintComponent(Graphics g){
        Image img = new ImageIcon("resources/menu/Background.png").getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(),  null);
    }
}

