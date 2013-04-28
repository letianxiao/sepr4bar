/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.auxpanels;

import dab.engine.simulator.views.ValveView;

/**
 *
 * @author eduard
 */
public class ValveButton extends ControlButton {
    private ValveView valve;
    public ValveButton(ValveView valve) {
        this.valve = valve;
    }
    
    @Override
    protected void onClick() {
        valve.setOpen(!valve.getOpen()); // toggle status
        setStatus(valve.getOpen());
    }
    
    @Override
    public void update() {
        setStatus(valve.getOpen());
    }
}
