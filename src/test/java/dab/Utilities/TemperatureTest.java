/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.Utilities;

import dab.engine.utilities.Temperature;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author David
 */
public class TemperatureTest {

    private final Temperature temperature = new Temperature(786);

    @Test
    public void degreesKelvinShouldBeUnchanged() {
        assertEquals(786, temperature.inKelvin(), 0.00001);
    }

    @Test
    public void shouldConvertToDegreesCelsius() {
        Temperature t1 = new Temperature(273.15);
        assertEquals(0, t1.inCelsius(), 0.1);
    }

    @Test
    public void shouldConvertToStringWithUnits() {
        assertTrue(temperature.toString().contains("512.85"));
    }

    @Test
    public void shouldBeEqualToEqualTemperature() {
        assertTrue(temperature.equals(new Temperature(786)));
    }

    @Test
    public void shouldNotBeEqualToUnEqualTemperature() {
        assertFalse(temperature.equals(new Temperature(785)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertFalse(temperature.equals(null));
    }

    @Test
    public void shouldNotBeEqualToWeirdThing() {
        assertFalse(temperature.equals((Object)(new String("hello"))));
    }

    @Test
    public void shouldBeEqualToEqualTemperatureAsObject() {
        assertTrue(temperature.equals((Object)(new Temperature(786))));
    }

    @Test
    public void hashCodeShouldBeEqualToDegreesKelvin() {
        assertEquals(786, temperature.hashCode());
    }

    /**
     * Test of plus method, of class Temperature.
     */
    @Test
    public void testPlus() {
        Temperature other = new Temperature(10);
        Temperature instance = new Temperature(10);
        Temperature expResult = new Temperature(20);
        Temperature result = instance.plus(other);
        assertEquals(expResult, result);

    }

    /**
     * Test of minus method, of class Temperature.
     */
    @Test
    public void testMinus() {

        Temperature other = new Temperature(10);
        Temperature instance = new Temperature(10);
        Temperature expResult = new Temperature();
        Temperature result = instance.minus(other);
        assertEquals(expResult, result);
    }
}
