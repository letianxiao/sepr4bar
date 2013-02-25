package dab.engine.simulator;

import dab.engine.utilities.Density;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Velocity;
import static dab.engine.utilities.Units.*;

/**
 * Bernoulli's Law
 * 
 * Rearrangements of Bernoulli's Law for fluid flow
 * to find the value of any variable from any of the others.
 * 
 * @author David
 */
public class Bernoulli {

    /**
     *
     * @param pressure
     * @param density
     *
     * @return
     */
    public static Velocity velocity(Pressure pressure, Density density) {
        return metresPerSecond(Math.sqrt(2 * pressure.inPascals() * density.inKilogramsPerCubicMetre()));
    }

    /**
     *
     * @param pressure
     * @param velocity
     *
     * @return
     */
    public static Density density(Pressure pressure, Velocity velocity) {
        return kilogramsPerCubicMetre((2 * pressure.inPascals()) / Math.pow(velocity.inMetresPerSecond(), 2));
    }

    /**
     *
     * @param density
     * @param velocity
     *
     * @return
     */
    public static Pressure pressure(Density density, Velocity velocity) {
        return pascals(0.5 * density.inKilogramsPerCubicMetre() * Math.pow(velocity.inMetresPerSecond(), 2));
    }
}
