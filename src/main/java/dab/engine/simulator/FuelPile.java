package dab.engine.simulator;

import dab.engine.utilities.Percentage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class which sets properties for control rods based on their Fuel and rod height position
 * 
 * @author Marius
 */
@JsonTypeName(value = "FuelPile")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
public class FuelPile {

    private final int maximumOutput = 20000000;
    @JsonProperty
    private Percentage controlRodPosition = new Percentage(0);

    /**
     *
     * @param extracted Moving by the input which is a variable of the Percentage Class
     */
    public void moveControlRods(Percentage extracted) {
        controlRodPosition = extracted;

    }

    /**
     *
     * @param seconds 
     *
     * @return 
     */
    public int output(int seconds) {
        return (int)(maximumOutput * controlRodPosition.ratio() * seconds + 3000000);
    }

    /**
     *
     * @return Returns the control rod height position
     */
    public Percentage controlRodPosition() {
        return this.controlRodPosition;
    }
}
