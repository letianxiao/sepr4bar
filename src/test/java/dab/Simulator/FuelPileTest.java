/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.Simulator;

import dab.engine.simulator.FuelPile;
import dab.engine.utilities.Percentage;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marius
 */
public class FuelPileTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldOutput3MWattsWhenControlRodsAt0() {
        FuelPile fuelPile = new FuelPile();
        fuelPile.moveControlRods(new Percentage(0));
        assertEquals(3000000, fuelPile.output(1));
    }

    @Test
    public void shouldOutput23MegaWattsWhenControlRodsAt100() {
        FuelPile fuelPile = new FuelPile();
        fuelPile.moveControlRods(new Percentage(100));
        assertEquals(23000000, fuelPile.output(1));
    }
}
