package dab.gui;

import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.FailableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PumpFixButton extends FixButton {

	public PumpFixButton(int coordX, int coordY,  FailableComponent component){
		super(coordX, coordY,  component);
		addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                        try {
                            pressed();
                        } catch (CannotRepairException ex) {
                            Logger.getLogger(PumpFixButton.class.getName()).log(Level.SEVERE, null, ex);
                        }
                if (getFixed()){
                        notifyFixObservers();
                }
            }
		});
	}
}
