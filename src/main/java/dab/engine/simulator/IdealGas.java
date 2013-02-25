package dab.engine.simulator;

import dab.engine.utilities.*;
import static dab.engine.simulator.PhysicalConstants.*;
import static dab.engine.utilities.Units.*;

/**
 * Ideal Gas Equation
 * 
 * Rearrangements of the Ideal Gas Equation to find any one variable
 * given the others.
 * 
 * @author David
 */
public class IdealGas {

    /**
     *
     * @param volume 
     * @param mass 
     * @param temperature
     *
     * @return Returns the pressure which is calculated in Pascals
     */
    public static Pressure pressure(Volume volume, Mass mass, Temperature temperature) {
        return pascals((mass.inMolesOfWater() * gasConstant * temperature.inKelvin()) / volume.inCubicMetres());
    }

    /**
     *
     * @param volume
     * @param mass
     * @param pressure
     *
     * @return Returns the temperature in Kelvin
     */
    public static Temperature temperature(Volume volume, Mass mass, Pressure pressure) {
        return kelvin((pressure.inPascals() * volume.inCubicMetres()) / (mass.inKilograms() * gasConstant));
    }

    /**
     *
     * @param pressure
     * @param mass
     * @param temperature
     *
     * @return Returns the calculated volume in cubic metres 
     */
    public static Volume volume(Pressure pressure, Mass mass, Temperature temperature) {
        return cubicMetres(mass.inMolesOfWater() * gasConstant * temperature.inKelvin());
    }

    /**
     *
     * @param pressure
     * @param volume
     * @param temperature
     *
     * @return Returns mass of water in moles 
     */
    public static Mass mass(Pressure pressure, Volume volume, Temperature temperature) {
        return molesOfWater((pressure.inPascals() * volume.inCubicMetres()) / (gasConstant * temperature.inKelvin()));
    }
}
