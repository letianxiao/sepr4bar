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
public class Circle extends HitBounds{
    
    private FailableComponent component;
    
    public Circle(FailableComponent component, int width, int x, int y){
      super(component, x, y, width);
    }
    
    //TODO: adjust the differences depending on how much bigger the picture frame is from 
    //the picture itself. And probably check which of the components that is or sth before adjusting
    @Override
    protected void adjustCoordinates(){
        
    }
    
    
}
