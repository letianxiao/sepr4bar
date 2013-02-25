package dab.engine.simulator;

import dab.engine.utilities.*;
import static dab.engine.utilities.Units.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 *
 * @author David
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Port {

    /**
     *
     */
    public Density density = Density.ofLiquidWater();
    /**
     *
     */
    public Pressure pressure = pascals(101325);
    /**
     *
     */
    public Temperature temperature = kelvin(300);
    /**
     *
     */
    public Mass mass = kilograms(0);
    /*
     * 
     */
    public Mass flow = kilograms(0);
}
