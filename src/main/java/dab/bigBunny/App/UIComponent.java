/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.FailableComponent;
import dab.gui.FixButton;
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
    protected FailableComponent component;
    protected Image standardImage;
    protected Image damagedImage;
    int counter = 0;
    
    
    public UIComponent(FailableComponent component, Point position, Image standardImage, Image damagedImage) {
        this.component = component;
        setSize(new Dimension(standardImage.getWidth(null), standardImage.getHeight(null)));
        this.standardImage = standardImage;
        this.damagedImage  = damagedImage;
        this.setBounds(position.x, position.y, standardImage.getWidth(null), standardImage.getHeight(null));
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (counter < 10) {
            g.drawImage(damagedImage, 0, 0, this);
        } else {
            g.drawImage(standardImage, 0, 0, this);
            if (counter > 20) counter = 0;
        }
        System.out.println(counter);
        counter++;
    }
}
