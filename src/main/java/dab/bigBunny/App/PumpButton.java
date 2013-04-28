package dab.bigBunny.App;

import dab.engine.simulator.views.PumpView;

/**
 *
 * @author eduard
 */
public class PumpButton extends ControlButton {

    private PumpView pump;

    public PumpButton(PumpView pump) {
        this.pump = pump;
    }

    @Override
    protected void onClick() {
        if (!pump.hasFailed()) { // component is running
            pump.setStatus(!pump.getStatus()); // toggle pump status
            setStatus(pump.getStatus());
        }
    }

    @Override
    public void update() {
        if (pump.hasFailed()) {
            setFailed();
        } else {
            setStatus(pump.getStatus());
        }
    }
}
