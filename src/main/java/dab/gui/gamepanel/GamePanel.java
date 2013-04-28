/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.gamepanel;

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
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author eduard
 */
public class GamePanel extends JPanel {
    protected Simulator simulator;
    protected BufferedImage background;
    protected ArrayList<UIComponent> uiComponents;
    
    public GamePanel(Simulator simulator) {
        this.simulator = simulator;
        uiComponents = new ArrayList<>();
        
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
        
        
        uiComponents.add(new UIComponent(this, simulator.getPumps().get(1), new Point(200, 100), "pump1.png", "pump2.gif"));
        uiComponents.add(new UIComponent(this, simulator.getPumps().get(0), new Point(200, 200), "pump1.png", "pump2.gif"));
        uiComponents.add(new UIComponent(this, simulator.getTurbine(),      new Point(200, 300), "pump1.png", "pump2.gif"));
        uiComponents.add(new UIComponent(this, simulator.getCondenser(),    new Point(200, 400), "pump1.png", "pump2.gif"));
        uiComponents.add(new UIComponent(this, simulator.getReactor(),      new Point(200, 500), "pump1.png", "pump2.gif"));
        
        for (UIComponent c : uiComponents) {
            add(c);
            c.setVisible(true);
        }
    }
    
    public void updateComponents() {
        for (UIComponent c : uiComponents) {
            c.update();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //paintComponents(g);
        g.drawImage(background, 0, 0, null);
    }
}
