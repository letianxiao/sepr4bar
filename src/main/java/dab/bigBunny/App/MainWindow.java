/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.gui.Menu;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;



/**
 *
 * @author eduard
 */
public class MainWindow extends JFrame implements ActionListener {
    DaIntro intro;
    DaMMenu menu;
    
    
    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new CardLayout());
        
        // add the stuff
        intro = new DaIntro(this);
        menu  = new DaMMenu();
        getContentPane().add("intro", intro);
        getContentPane().add("menu", menu);
        
        new Timer(100, this).start();
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        //System.out.println(isFocusOwner());
    }
    
    public void start() {
        setCurrentPanel("intro");
        intro.start();
    }
    
    protected void setCurrentPanel(String name) {
        CardLayout layout = (CardLayout)getContentPane().getLayout();
        layout.show(getContentPane(), name);        
    }
    
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                MainWindow mw = new MainWindow();
                mw.start();
            }
        });
    }
}