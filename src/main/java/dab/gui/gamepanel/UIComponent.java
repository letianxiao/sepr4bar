/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.gamepanel;

import dab.engine.simulator.FailableComponent;
import dab.engine.simulator.views.FailableComponentView;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author eduard
 */
public class UIComponent extends JComponent {
    protected FailableComponentView component;
    protected FixButton fixButton;
    protected Image standardImage;
    protected Image damagedImage;
    protected GamePanel parent;
      
    public UIComponent(GamePanel parent, FailableComponentView component, Point location, String stdImgPath, String dmgImgPath) {
        this.parent = parent;
        this.component = component;
        this.standardImage = new ImageIcon(GamePanel.class.getResource(stdImgPath)).getImage();
        this.damagedImage  = new ImageIcon(GamePanel.class.getResource(dmgImgPath)).getImage();
        setLayout(null);
        setLocation(location);
        
        int width =  standardImage.getWidth(null);
        int height = standardImage.getHeight(null);
        
        setSize(width, height);   
        
        fixButton = new FixButton(component);
        
        parent.add(fixButton);
        fixButton.setVisible(true);
        fixButton.setLocation(location.x, location.y - 50);
        
    }
    
    public void update() {
        fixButton.update();
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (component.hasFailed()) {
            g.drawImage(damagedImage, 0, 0, this);
        } else {
            g.drawImage(standardImage, 0, 0, this);
        }
    }
}
