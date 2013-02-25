package dab.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The splash displays at startup, and leads into the main menu.
 * 
 * @author Team Haddock
 *
 */
public class Splash extends JFrame {

    private int duration;
    JPanel mainPanel = new JPanel();
    public Splash(int d) {
        duration = d;
    }

    public void showSplash() {
        getContentPane().setBackground(Color.BLACK);
        setSize(1366,768);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0,0));
        JLabel label = new JLabel(new ImageIcon("resources/splash/comp.gif"));
        JLabel copyrt = new JLabel
        ("Team HADDOCK", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        mainPanel.setBackground(Color.black);
        mainPanel.add(label, BorderLayout.CENTER);
        mainPanel.add(copyrt, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        try {
            //show the animation for a certain duration
            Thread.sleep(duration);
        } catch (Exception e) {
        }
        //hide the animation
        mainPanel.setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();
        //initialize main Menu
        add(new Menu());
        setVisible(true);
    }

}