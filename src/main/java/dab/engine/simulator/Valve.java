package dab.engine.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import dab.engine.simulator.views.ValveView;

/**
 * Class that sets the component status for Valves
 *
 * @author james
 */
public abstract class Valve implements ValveView {

    @JsonProperty
    private boolean open = true;

    /**
     *
     * @return boolean (if the valve is opened)
     */
    public boolean getOpen() {
        return this.open;
    }

    /**
     *
     * @param  boolean Open
     */
    public void setOpen(boolean openStatus) {
        this.open = openStatus;
    }
}
