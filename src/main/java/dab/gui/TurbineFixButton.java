package dab.gui;

import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.FailableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurbineFixButton extends FixButton {

	public TurbineFixButton(int coordX, int coordY, FailableComponent component){
		super(coordX, coordY, component);
		addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                        try {
                            pressed();
                        } catch (CannotRepairException ex) {
                            Logger.getLogger(TurbineFixButton.class.getName()).log(Level.SEVERE, null, ex);
                        }
                if (getFixed()){
                        notifyFixObservers();
                }
            }
		});
	}
}
