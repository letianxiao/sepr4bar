package dab.engine.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that sets the component status for Valves
 *
 * @author james
 */
public class Valve {

    @JsonProperty
    private boolean open = true;

    /**
     *
     * @return boolean (if the valve is opened)
     */
    public boolean getOpen() {
        return open;
    }

    /**
     *
     * @param  boolean Open
     */
    public void setOpen(boolean Open) {
        open = Open;
    }
}
