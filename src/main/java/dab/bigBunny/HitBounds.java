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
public abstract class HitBounds {
    
    protected FailableComponent component;
    protected int x,y, width, height;
    
    
    public HitBounds(FailableComponent component, int x, int y, int width, int height){
        this.component = component;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public FailableComponent getComponent(){
        return component;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public Rectangle getDimensions(){
        return new Rectangle(x, y, width, height);
    }
    
    protected abstract void adjustCoordinates();
}
