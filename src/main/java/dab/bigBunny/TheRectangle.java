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
public class TheRectangle extends HitBounds{
    
    int height;
    
    public TheRectangle(FailableComponent component, int x, int y, int width, int height){
        super(component, x, y, width);
        this.height = height;
        adjustCoordinates();
    }
      
    public int getHeight(){
        return height;
    }
    
    //TODO: adjust the differences depending on how much bigger the picture frame is from 
    //the picture itself. And probably check which of the components that is or sth before adjusting
    @Override
    protected void adjustCoordinates(){
        
    }
      
}
