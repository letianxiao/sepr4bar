package dab.engine.simulator;

/**
 * Class which states physical constants of variables:
 * 		
 * @param bolingPointOfWater Default boiling point of water value
 * @param specificHeatOfWater Default specific heat of water value
 * @param latentHeatOfWater Default latent heat of water value
 * @param gasConstant Default gas constant value
 * @param heatVaporizationOfWater Default value for the heat vapourisation of water value
 * @param atmosphericPressure Default value for Atmospheric Pressure
 * 
 * @author Marius
 */

public class PhysicalConstants {

    /**
     *
     */
    public static final double boilingPointOfWater = 373.15;
    /**
     *
     */
    public static final int specificHeatOfWater = 4181;
    /**
     *
     */
    public static final int latentHeatOfWater = 2260000;
    /**
     *
     */
    public static final double gasConstant = 8.314;
    /*
     * 
     */
    public static final double heatVaporizationOfWater = 40.66;
    /*
     * 
     */
    public static final double atmosphericPressure = 101325;
}
