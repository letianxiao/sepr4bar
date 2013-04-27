/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.simulator.views;

/**
 *
 * @author eduard
 */
public interface PumpView extends FailableComponentView {
    public void setStatus(boolean newStatus);
    public boolean getStatus();
}
