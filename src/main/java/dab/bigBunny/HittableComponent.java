/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import dab.engine.simulator.FailableComponent;
import java.awt.Rectangle;

/**
 *
 * @author Aiste
 */
public abstract class HittableComponent {
    
    protected FailableComponent component;
    protected int x,y, width, height;
    
    
    public HittableComponent(FailableComponent component, int x, int y, int width, int height){
        this.component = component;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public FailableComponent getComponent(){
        return component;
    }
    
    protected abstract Rectangle getDimensions(int radius);
}
