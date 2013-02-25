package dab.engine.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import dab.engine.utilities.Density;
import dab.engine.utilities.Units;

/**
 * Heatsink class- provides a port for cold water to be pumped to the condenser.
 * A blackbox component implemented for enjoyability and to remove heat
 * from the closed simulation model.
 *
 * @author James
 */
public class HeatSink {

    @JsonProperty
    private Port outputPort;

    public HeatSink() {
        outputPort = new Port();
        outputPort.temperature = Units.kelvin(308.15);
        outputPort.density = Density.ofLiquidWater();
        outputPort.mass = Units.kilograms(10);
    }

    public Port outputPort() {
        return outputPort;
    }
}
