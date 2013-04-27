/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.simulator.views;

import dab.engine.utilities.Mass;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;

/**
 *
 * @author eduard
 */
public interface CondenserView extends FailableComponentView {
    public Percentage getWaterLevel();
    
    public Temperature getTemperature();    
    public Pressure getPressure();
}
