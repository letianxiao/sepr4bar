package dab.gui;

import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.FailableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CondenserFixButton extends FixButton {

	public CondenserFixButton(int coordX, int coordY, FailableComponent component){
		super(coordX, coordY, component);
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
                        try {
                            pressed();
                        } catch (CannotRepairException ex) {
                            Logger.getLogger(CondenserFixButton.class.getName()).log(Level.SEVERE, null, ex);
                        }
				if (getFixed()){
            	    notifyFixObservers();
				}
			}
		});
	}
}
