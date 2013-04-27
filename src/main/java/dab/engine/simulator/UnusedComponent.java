/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.simulator;

import dab.engine.utilities.Percentage;

/**
 *
 * @author Aiste
 */
public class UnusedComponent extends FailableComponent{
    
    private String theComponent;
    
    public UnusedComponent(String theComponent){
        super();
        this.theComponent = theComponent;
        
    }

    public String getNameOfComponent(){
        return theComponent;
    }
    
    @Override
    protected Percentage calculateWearDelta() {
        return new Percentage(0);
    }
    
}
