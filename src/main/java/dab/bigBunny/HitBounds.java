/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import dab.engine.simulator.FailableComponent;

/**
 *
 * @author Aiste
 */
public abstract class HitBounds {
    
    protected FailableComponent component;
    protected int x,y, width;
    
    
    public HitBounds(FailableComponent component, int x, int y, int width){
        this.component = component;
        this.x = x;
        this.y = y;
        this.width = width;
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
    
    protected abstract void adjustCoordinates();
}
