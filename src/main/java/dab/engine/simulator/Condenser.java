package dab.engine.simulator;

import static dab.engine.utilities.Units.kelvin;
import static dab.engine.utilities.Units.kilograms;
import static dab.engine.utilities.Units.pascals;
import static dab.engine.utilities.Units.percent;
import dab.engine.utilities.Density;
import dab.engine.utilities.Mass;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;
import dab.engine.utilities.Volume;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Class containing the physical model logic for the Condenser component
 * 
 * @author Marius
 */
public class Condenser extends FailableComponent {

    @JsonProperty
    private Mass steamMass;
    @JsonProperty
    private Mass waterMass;
    @JsonProperty
    private final Mass maximumWaterMass = kilograms(1000);
    @JsonProperty
    private Port outputPort = new Port();
    @JsonProperty
    private Port steamInputPort = new Port();
    @JsonProperty
    private Port coolantInputPort = new Port();
    @JsonProperty
    private Port reactorInputPort = new Port();
    @JsonProperty
    private Temperature temperature;
    @JsonProperty
    private Pressure pressure;
    @JsonProperty
    private Percentage waterLevel = percent(0);
    @JsonProperty
    private Mass buildUp = kilograms(0);

    public Condenser() {
        initializeVariables();
    }
    /**
     * Execute the calculations in this component
     */
    public void step() {

        waterMass = outputPort.mass.plus(buildUp);
        steamMass = steamInputPort.flow;
        buildUp = kilograms(0);
        //Debug

        //System.out.println("Condenser Steam Mass: " + steamInputPort.mass);



        if (reactorInputPort.mass.inKilograms() > 0) {
            calculateNewTemperature(reactorInputPort);
        } else if (steamInputPort.mass.inKilograms() > 0) {
            calculateNewTemperature(steamInputPort);
        }
        if (!hasFailed()) {
            calculateNewTemperature(coolantInputPort);
        }



        pressure = IdealGas.pressure(calculateSteamVolume(), steamMass, temperature);

        //System.out.println("Condenser Steam Input Mass: " + steamInputPort.mass);

        try {

            if (steamInputPort.mass.inKilograms() > 0) {
                waterMass = waterMass.plus(steamInputPort.mass);
            }

            if (reactorInputPort.mass.inKilograms() > 0) {
                waterMass = waterMass.plus(reactorInputPort.mass);
            }

            //Debug

            //System.out.println("Condenser Water Mass: " + waterMass);

            waterLevel = new Percentage((waterMass.inKilograms() / maximumWaterMass.inKilograms()) * 100);

        } catch (Exception e) {
            waterLevel = new Percentage(100);
            //This is over-pressure condition, however will be handeled by failure model and not needed to be done here
        }


        if (hasFailed()) {
            //Do nothing until the condenser has been repaired

            outputPort.mass = kilograms(0);
            outputPort.pressure = pascals(101325);
            buildUp = waterMass;

        } else {
            outputPort.mass = waterMass;
            outputPort.temperature = temperature;
            outputPort.pressure = pressure;
            stepWear();
        }
    }

    /**
     * @return Volume
     */
    private Volume calculateSteamVolume() {
        return maximumWaterMass.volumeAt(Density.ofLiquidWater()).minus(waterMass.volumeAt(Density.ofLiquidWater()));
    }

    public void calculateNewTemperature(Port in) {
            
         //4.19 kJ/kg = c_w of water (specific heat of water)
         //h_fg = c_w * (t_f - t_0) = enthalpy of water
         //t_f = steam saturation temperature = 100C
         //t_0 = reference temperature = 0C
         //m_s*h_fg = mass of steam * enthalpy = thermal energy
         
        temperature = temperature.plus(new Temperature(((4.19 * (in.temperature.inKelvin() - temperature.inKelvin())) /
                1000) * in.mass.inKilograms()));
    }

    /**
     *  @param delta
     */
    public void reduceTemperature(int delta) {
        temperature = temperature.minus(kelvin(delta));
    }

    /**
     *  @param Temperature
     */
    public void setTemperature(Temperature newTemp) {
        temperature = newTemp;
    }

    /**
     *  Returns the temperature of the condenser
     *  @return Temperature
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * @param Mass
     */
    public void setWaterMass(Mass newWater) {
        waterMass = newWater;
    }

    /**
     * Returns the mass of the water
     * @param Mass
     */
    public Mass getWaterMass() {
        return waterMass;
    }

    /**
     *  Returns the pressure of the condenser
     *  @return Pressure
     */
    public Pressure getPressure() {
        return pressure;
    }

    /**
     * @return input Port
     */
    public Port inputPort() {
        return steamInputPort;
    }

    /**
     * @return output Port
     */
    public Port outputPort() {
        return outputPort;
    }

    /**
     * @return coolant input port
     */
    public Port coolantInputPort() {
        return coolantInputPort;
    }

    /**
     * Returns the water level of the condenser
     * @return Percentage
     */
    public Percentage getWaterLevel() {
        return waterLevel;
    }

    @Override
    public Percentage calculateWearDelta() {
        return percent(1);
    }

    @Override
    public void repair() throws CannotRepairException {
        super.repair();
        initializeVariables();
    }

    /**
     *  Initialize the condenser
     */
    private void initializeVariables() {
        pressure = pascals(101325);
        waterMass = kilograms(0);
        steamMass = kilograms(0);
        /*
         steamInputPort.mass = kilograms(0);
         reactorInputPort.mass = kilograms(0);
         outputPort.mass = kilograms(0);
         steamInputPort.temperature = kelvin(0);
         reactorInputPort.temperature = kelvin(0);
         */
        temperature = kelvin(298.15);  //Start at room temp
    }
}
