package dab.engine.simulator;

import dab.engine.utilities.Mass;
import dab.engine.utilities.Percentage;
import static dab.engine.utilities.Units.kilograms;
import static dab.engine.utilities.Units.percent;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Class which contains the physical model for the pump component.
 *
 * @author Marius
 */
public class Pump extends FailableComponent {

    @JsonProperty
    private Port inputPort;
    @JsonProperty
    private Port outputPort;
    @JsonProperty
    private Mass capacity = kilograms(3);
    @JsonProperty
    private boolean status = true;

    private Pump() {
        super();
        inputPort = null;
        outputPort = null;
    }

    public void fail(){
    	super.fail();
    	status = false;
    }
    
    public Pump(Port input, Port output) {

        inputPort = input;
        outputPort = output;

    }
    /**
     *
     * Execute the calculations for this component
     */
    public void step() {
        if (hasFailed()) {
            outputPort.mass = kilograms(0);
            return;
        }
        if (status) {
            if (inputPort.mass.inKilograms() > capacity.inKilograms()) {
                outputPort.mass = capacity;
                inputPort.mass = inputPort.mass.minus(capacity);
            } else {
                outputPort.mass = inputPort.mass;
                inputPort.mass = kilograms(0);
            }

            outputPort.temperature = inputPort.temperature;
            stepWear();
        }
    }
    /**
     *
     * @return wear percentage
     */
    @Override
    public Percentage calculateWearDelta() {
        return percent(1);
    }

    /**
     *
     * @param bool status
     */
    public void setStatus(boolean newStatus) {
        status = newStatus;
    }

    /**
     *
     * @param capacity (mass)
     */
    public void setCapacity(Mass newCapacity) {
        capacity = newCapacity;
    }

    /**
     *
     * @return bool status
     */
    public boolean getStatus() {
        return status;
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
}
