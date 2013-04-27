package dab.gui;

import dab.engine.simulator.FailableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CondenserFixButton extends FixButton {

	public CondenserFixButton(int coordX, int coordY, FailableComponent component){
		super(coordX, coordY, component);
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				pressed();
				if (getFixed()){
            	    notifyFixObservers();
				}
			}
		});
	}
}
