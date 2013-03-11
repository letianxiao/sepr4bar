/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.Simulator;

import dab.engine.simulator.Turbine;
import static dab.engine.utilities.Units.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Marius
 */
public class TurbineTest {

    @Test
    public void noPressureDeltaEqualsNoPower() {
        Turbine turbine = new Turbine();
        turbine.inputPort().pressure = pascals(101325);
        turbine.outputPort().pressure = pascals(101325);
        turbine.step();
        assertEquals(0, turbine.outputPower(), 0.1);
    }

    @Test
    public void someMassGeneratesPower() {
        Turbine turbine = new Turbine();
        turbine.inputPort().mass = kilograms(5);
        turbine.step();
        assertThat(turbine.outputPower(), greaterThan(0.0));
    }
}
