package dab.engine.simulator;

import static dab.engine.simulator.PhysicalConstants.*;
import static dab.engine.utilities.Units.*;
import dab.engine.utilities.*;
import dab.engine.seprphase2.GameOverException;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class containing the physical model logic for the Reactor component
 * @author Marius
 */
public class Reactor extends FailableComponent {

    private final Mass maximumWaterMass = kilograms(1000);
    private final Mass minimumWaterMass = kilograms(800);
    private final Volume reactorVolume = cubicMetres(2);
    @JsonProperty
    private FuelPile fuelPile = new FuelPile();
    @JsonProperty
    private Mass waterMass;
    @JsonProperty
    private Mass steamMass;
    @JsonProperty
    private Temperature temperature;
    @JsonProperty
    private Pressure pressure;
    // TODO: used locally in step(), do not remove this todo until told author of his stupidity
    @JsonProperty
    private Density steamDensity;
    @JsonProperty
    private Port outputPort = new Port();
    @JsonProperty
    private Port inputPort = new Port();
    
    // TODO: used locally in step(); see above
    @JsonProperty
    private double boilingPtAtPressure;
    @JsonProperty
    private double neededEnergy;

    /**
     *
     */
    public Reactor() { // TODO: call full constructor with default values from this one
        super();
        fuelPile.moveControlRods(new Percentage(0));
        waterMass = maximumWaterMass;
        steamMass = kilograms(0);
        temperature = kelvin(350);
        pressure = pascals(101325);
    }

    /**
     *
     * @param controlRodPosition
     * @param waterLevel
     * @param temperature
     * @param pressure
     */
    public Reactor(Percentage controlRodPosition, Percentage waterLevel,
            Temperature temperature, Pressure pressure) {
        super();
        fuelPile.moveControlRods(controlRodPosition);
        waterMass = kilograms(maximumWaterMass.inKilograms() * waterLevel.ratio());
        steamMass = kilograms(0);
        this.temperature = temperature;
        this.pressure = pressure;
        correctWaterMass();
    }

    /**
     *
     * @param Percentage
     */
    public void moveControlRods(Percentage extracted) {
        fuelPile.moveControlRods(extracted);
    }

    /**
     *
     * @return control rod position (percentage)
     */
    public Percentage controlRodPosition() {
        return fuelPile.controlRodPosition();
    }

    /**
     *
     * @return water level percentage
     */
    public Percentage waterLevel() {
        return new Percentage((waterMass.inKilograms() / maximumWaterMass.inKilograms()) * 100);
    }

    /**
     *
     * @return temperature
     */
    public Temperature temperature() {
        return this.temperature;
    }

    /**
     *
     * @return pressure
     */
    public Pressure pressure() {
        return this.pressure;
    }

    /**
     * Execute the calculations for this component
     * @throws GameOverException
     */
    public void step() throws GameOverException {

       // System.out.println(inputPort.mass.inKilograms());
        if (steamMass.inKilograms() > inputPort.mass.inKilograms()) {
            steamMass = steamMass.minus(inputPort.mass);
            waterMass = waterMass.plus(inputPort.mass);
            correctWaterMass();
            calculateNewTemperature(inputPort);
        } else {
            waterMass = waterMass.plus(steamMass);
            correctWaterMass();
            steamMass = kilograms(0);
            calculateNewTemperature(inputPort);
        }

        if (hasFailed()) {
            throw new GameOverException();
        }

        /*
         * Calculates the boiling point at the current pressure,
         * then it calculates the needed enegry to reach that boiling point
         */

        double old = boilingPtAtPressure;
        // FIXME: when bug occurs boilingPtAtPressure always > temperature
        // therefore it thinks that the water is not boiling and therefore not converting
        // it into steam. Because it doesn't convert it into steam,
        // the pressure accumulates
        boilingPtAtPressure = boilingPointOfWater + 10 * Math.log(pressure.inPascals() / atmosphericPressure);
        if (boilingPtAtPressure - old < 0.3)
           // System.out.println("BROKE");
        
        neededEnergy = (boilingPtAtPressure - temperature.inKelvin()) * waterMass.inKilograms() * specificHeatOfWater;


        if (neededEnergy >= fuelPile.output(1)) {

            /*
             * Calculates how much the water heats if it's not at boiling point
             */

            temperature = kelvin(temperature.inKelvin() +
                    fuelPile.output(1) / waterMass.inKilograms() /
                    specificHeatOfWater);
            outputPort.mass = kilograms(0);
        } else {

            /*
             * Sets temperature to boiling point
             * If any energy is left from the fuelpile after heating up:
             * Calculates how much water turns to steam in one timestep at boiling point using remaining energy
             */
            temperature = kelvin(boilingPtAtPressure);
            Mass deltaMass = kilograms((fuelPile.output(1) - neededEnergy) / latentHeatOfWater);
            steamMass = steamMass.plus(deltaMass);
            waterMass = waterMass.minus(deltaMass);
            outputPort.mass = deltaMass;
            correctWaterMass();
        }

        /*
         * Calculates volume of steam in this particular timestep
         * Calculates pressure of said steam
         */

        Volume steamVolume = reactorVolume.minus(waterMass.volumeAt(Density.ofLiquidWater()));
        pressure = IdealGas.pressure(steamVolume, steamMass, temperature);
        if (pressure.inPascals() < atmosphericPressure) {
            pressure = pascals(atmosphericPressure);
        }
        steamDensity = steamMass.densityAt(steamVolume);

        /*
         * Sends information to output port
         */

        outputPort.flow = steamMass;
        outputPort.density = steamDensity;
        outputPort.pressure = pressure;
        outputPort.temperature = temperature;

        //System.out.println("Reactor Water Mass " + waterMass);
        //System.out.println("Reactor Steam Mass " + outputPort.flow);



        /*
         * Calculates component wear after a time step
         */
        stepWear();
        
        //System.out.println(temperature.toString());
    }

    /**
     *
     * @return output flow velocity
     */
    public Velocity outputFlowVelocity() {
        return metresPerSecond(pressure().inPascals() / 100);
    }

    /**
     *
     * @return output Port
     */
    public Port outputPort() {
        return outputPort;
    }

    /**
     *
     * @return input Port
     */
    public Port inputPort() {
        return inputPort;
    }

    /**
     *
     * @return maximum water Mass
     */
    public Mass maximumWaterMass() {
        return maximumWaterMass;
    }
    /**
     *
     * @return minimum water Mass
     */
    public Mass minimumWaterMass() {
        return minimumWaterMass;
    }
    /**
     *
     * @param input Port
     */
    public void calculateNewTemperature(Port in) {
        temperature = kelvin((temperature.inKelvin() * waterMass.inKilograms() + in.temperature.inKelvin() * in.mass
                .inKilograms()) / (waterMass.inKilograms() + in.mass.inKilograms()));
    }

    /**
     *
     * @return wear percentage
     */
    @Override
    public Percentage calculateWearDelta() {
        return new Percentage(0);
    }

    /**
     *
     * @return minimum water level percentage
     */
    public Percentage minimumWaterLevel() {
        return new Percentage((this.minimumWaterMass.inKilograms() / this.maximumWaterMass.inKilograms()) * 100);
    }

    /**
     *
     * Avoid issues with floating-point error
     * TODO: investigate this hackery
     */
    private void correctWaterMass() {
        if (waterMass.inKilograms() > maximumWaterMass.inKilograms()) {
            waterMass = maximumWaterMass;
        }
        if (waterMass.inKilograms() < 0) {
            waterMass = kilograms(0);
        }
    }
}

