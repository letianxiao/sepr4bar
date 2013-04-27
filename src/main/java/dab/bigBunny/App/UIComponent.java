/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.FailableComponent;
import dab.engine.simulator.views.FailableComponentView;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
    int counter = 0;
    
    
    public UIComponent(FailableComponentView component, Point position, Image standardImage, Image damagedImage) {
        this.component = component;
        setSize(new Dimension(standardImage.getWidth(null), standardImage.getHeight(null)));
        this.standardImage = standardImage;
        this.damagedImage  = damagedImage;
        setBounds(position.x, position.y, standardImage.getWidth(null), standardImage.getHeight(null));   
        
        this.fixButton = new FixButton(component);
        
        setLayout(null);
        add(fixButton);
        fixButton.setVisible(true);
        fixButton.setBounds(20, 30, 100, 100);
        
    }
    
    public void update() {
        fixButton.update();
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
