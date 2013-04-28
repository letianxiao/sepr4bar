package dab.gui;

import dab.engine.simulator.Simulator;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JPanel;

/**
 * The button panel contains the basic reactor controls,
 * it is initialised to a fixed layout.
 * @author Team Haddock
 *
 */
public class ButtonPanel extends JPanel {
	private IButton btnPump1, btnPump2, btnValve1, btnValve2;
	private ControlRodSlider controlRodSlider;
	private JLabel pump1_label;
	private JLabel pump2_label;
	private JLabel valve1_label;
	private JLabel valve2_label;
	private JLabel rods_label;
    private Simulator simulator;
	
	/**
	 * Initialises the ButtonPanel to a standard layout.
	 */
	public ButtonPanel(Simulator sim){
		setLayout(null);
        simulator = sim;
		/*
		pump2_label = new JLabel("Coolant Pump");
		pump2_label.setBounds(265, 40, 100, 30);
		add(pump2_label);
		btnPump2 = ButtonFactory.instance().createButton("Pump", 2);
		btnPump2.setBounds(255, 61, 101, 71);
        add(btnPump2);
        btnPump2.attachObserver(SinglePlayerInterface.instance());
        
        pump1_label = new JLabel("Water Pump");
        pump1_label.setBounds(117, 40, 100, 30);
        add(pump1_label);
		btnPump1 = ButtonFactory.instance().createButton("Pump", 1);
		btnPump1.setBounds(111, 61, 101, 71);
        add(btnPump1);
        btnPump1.attachObserver(SinglePlayerInterface.instance());
		
        valve2_label = new JLabel("Condenser Valve");
        valve2_label.setBounds(261, 193, 100, 30);
        add(valve2_label);
		btnValve2 = ButtonFactory.instance().createButton("Valve", 2);
		btnValve2.setBounds(255, 214, 101, 71);
        add(btnValve2);
        btnValve2.attachObserver(SinglePlayerInterface.instance());
		
        valve1_label = new JLabel("Reactor Valve");
        valve1_label.setBounds(121, 193, 100, 30);
        add(valve1_label);
		btnValve1 = ButtonFactory.instance().createButton("Valve", 1);
		btnValve1.setBounds(111, 214, 101, 71);
        add(btnValve1);
        btnValve1.attachObserver(SinglePlayerInterface.instance());
        
        rods_label = new JLabel("Control Rods");
        rods_label.setBounds(25, 40, 100, 30);
        add(rods_label);
        controlRodSlider = new ControlRodSlider();
        add(controlRodSlider);
        controlRodSlider.attachObserver(SinglePlayerInterface.instance());
        */
		
	}
	
	/**
	 * Force one of the pump buttons to take a specified state
	 * @param valveID ID of the button to update.
	 * @param state state to set the button to.
	 */
	public void setPumpButtonStatus(int pumpID, ButtonState state){
		if(pumpID==1)
			btnPump1.updateState(state);
		if(pumpID==2)
			btnPump2.updateState(state);
	}
	
	/**
	 * Force one of the valve buttons to take a specified state
	 * @param valveID ID of the button to update.
	 * @param state state to set the button to.
	 */
	public void setValveButtonStatus(int valveID, ButtonState state){
		if(state!=ButtonState.BROKEN){
			if(valveID==1)
				btnValve1.updateState(state);
			if(valveID==2)
				btnValve2.updateState(state);
		}
	}
	
	/**
	 * Set the contained controlRodSlider to the specified value
	 * @param value A value between 0 and 100 (inclusive)
	 */
	public void setSliderValue(int value){
		controlRodSlider.setValue(value);
	}
	
	@Override
    public void paintComponent(Graphics g) {
        Image img = new ImageIcon("resources/bckgroundBLUE.png").getImage();
        g.drawImage(img, 0, 0, 600, 600,  null);

    }
}
