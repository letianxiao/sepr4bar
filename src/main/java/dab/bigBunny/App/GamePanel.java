/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.FailableComponent;
import dab.engine.simulator.Simulator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author eduard
 */
public abstract class GamePanel extends JPanel {
    protected Simulator simulator;
    protected BufferedImage background;
    
    public GamePanel(Simulator simulator) {
        this.simulator = simulator;
        
        // load background and set the size of the pannel according to it
        try {
            background = ImageIO.read(GamePanel.class.getResourceAsStream("gamepanel_bkg.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //setBackground(Color.WHITE); // default color should be white
        //setOpaque(true);
        Dimension panelSize = new Dimension(background.getWidth(), background.getHeight());
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setMaximumSize(panelSize);
        setSize(panelSize);
        
        setLayout(null);
        
        add(new UIComponent(simulator.components().get(0), new Point(50, 60), new ImageIcon(GamePanel.class.getResource("pump1.png")).getImage(), new ImageIcon(GamePanel.class.getResource("pump2.gif")).getImage()));
    }
    
    public void screenUpdate() {
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintComponents(g);
        g.drawImage(background, 0, 0, null);
    }
}
