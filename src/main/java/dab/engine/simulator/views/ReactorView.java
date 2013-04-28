/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.simulator.views;

import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;

/**
 *
 * @author eduard
 */
public interface ReactorView extends FailableComponentView {
    public void moveControlRods(Percentage extracted);
    public Percentage controlRodPosition();
    
    public Percentage waterLevel();
    public Temperature temperature();
    public Pressure pressure();
    
    public Percentage minimumWaterLevel();
}
