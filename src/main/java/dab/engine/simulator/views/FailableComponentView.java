/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.simulator.views;

import dab.engine.simulator.CannotRepairException;

/**
 *
 * @author eduard
 */
public interface FailableComponentView {
    public boolean hasFailed();
    public void repair() throws CannotRepairException;
}
