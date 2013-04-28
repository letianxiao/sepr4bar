package dab.engine.simulator;

import static dab.engine.utilities.Units.kilograms;
import dab.engine.utilities.Mass;
import dab.engine.utilities.Percentage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dab.engine.simulator.views.TurbineView;

/**
 * 	Class that contains the physical model of the Turbine component.
 *
 * @author Marius
 */
public class Turbine extends FailableComponent implements TurbineView {

    @JsonProperty
    private double outputPower;
    @JsonProperty
    private Port inputPort = new Port();
    @JsonProperty
    private Port outputPort = new Port();
    @JsonIgnore
    private static PlantController controller;
    @JsonProperty
    private Mass buildUp = kilograms(0);

    /**
     *
     */
    public Turbine() {
        super();
    }

    /**
     * Executed the calculations for this component
     */
    public void step() {

        //System.out.println("Turbine Input Water Mass " + inputPort.mass);
        //System.out.println("Turbine Output Water Mass 1 " + outputPort.mass);

        if (hasFailed()) {
            outputPower = 0;
            stepWear();
            buildUp = buildUp.plus(inputPort.mass);
            return;
        }

        outputPower = inputPort.mass.inKilograms() * 1000; //Requires conversion to grams
        outputPort.mass = inputPort.mass.plus(buildUp);
        outputPort.pressure = inputPort.pressure;
        outputPort.temperature = inputPort.temperature;
        outputPort.flow = inputPort.flow;
        buildUp = kilograms(0);
        stepWear();

        //System.out.println("Turbine Output Water Mass 2 " + outputPort.mass);
    }

    /**
     *
     * @return double power
     */
    public double outputPower() {
        return outputPower;
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
     * @return output Port
     */
    public Port outputPort() {
        return outputPort;
    }

    /**
     *
     * @return Calculates the wear probability for the specific component, and returns a % wear rate
     */
    @Override
    public Percentage calculateWearDelta() {
        return new Percentage(1);
    }
}
