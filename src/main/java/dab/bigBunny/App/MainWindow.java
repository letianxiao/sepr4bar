/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.bigBunny.BunnyController;
import dab.bigBunny.BunnyInterface;
import dab.bigBunny.Environment;
import dab.bigBunny.TwoPlayerScreen;
import dab.engine.simulator.Simulator;
import dab.gui.InterfaceController;
import dab.gui.Menu;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
public class MainWindow extends JFrame {
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() { 
                MainWindow mw = new MainWindow();
                mw.setVisible(true);
                
                mw.showIntro();
                
            }
        });
    }
    
    private DaMMenu menu;
    private Component currentComponent = null;
    
    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // add the stuff
        menu  = new DaMMenu(this);
    }
    
    public void showIntro() {
        DaIntro intro = new DaIntro(this);
        changeToPanel(intro);
        intro.start();
    }
    
    public void showMenu() {
        changeToPanel(menu);
    }
    
    public void startSinglePlayer(Simulator sim) {
        InterfaceController ic = InterfaceController.instance();
        ic.setup(sim);
        changeToPanel(ic);
    }
    
    public void startTwoPlayer() {
        //BunnyInterface bi = new BunnyInterface();
        
        Environment env = new Environment(getWidth(), getHeight());
        BunnyController bc = new BunnyController(env, new Point(100, 100), 10);
        bc.setBounds(new Rectangle(getWidth(), getHeight()));
        TwoPlayerScreen tps = new TwoPlayerScreen(bc, env);
        addKeyListener(tps);
        addMouseListener(tps);
        changeToPanel(tps);
    }
    
    private void changeToPanel(Component p) {
        if (currentComponent != null) {
            currentComponent.setVisible(false);
            getContentPane().remove(currentComponent);
        }
        getContentPane().add(p);
        currentComponent = p;
        p.setVisible(true);
        
    }
    
    protected void setCurrentPanel(String name) {
        CardLayout layout = (CardLayout)getContentPane().getLayout();
        layout.show(getContentPane(), name);        
    }
}