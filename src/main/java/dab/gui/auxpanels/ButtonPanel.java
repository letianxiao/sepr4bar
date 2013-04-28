package dab.gui.auxpanels;

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
	private dab.gui.auxpanels.PumpButton  btnPump1, btnPump2;
    private dab.gui.auxpanels.ValveButton btnValve1, btnValve2;
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
		
		pump2_label = new JLabel("Coolant Pump");
		pump2_label.setBounds(265, 40, 100, 30);
		add(pump2_label);
		btnPump2 = new dab.gui.auxpanels.PumpButton(sim.getPump(2));
		btnPump2.setBounds(255, 61, 101, 71);
        add(btnPump2);
        
        pump1_label = new JLabel("Water Pump");
        pump1_label.setBounds(117, 40, 100, 30);
        add(pump1_label);
		btnPump1 = new dab.gui.auxpanels.PumpButton(sim.getPump(1));
		btnPump1.setBounds(111, 61, 101, 71);
        add(btnPump1);
        
		
        valve2_label = new JLabel("Condenser Valve");
        valve2_label.setBounds(261, 193, 100, 30);
        add(valve2_label);
		btnValve2 = new dab.gui.auxpanels.ValveButton(sim.getValve(2));
		btnValve2.setBounds(255, 214, 101, 71);
        add(btnValve2);
        
		
        valve1_label = new JLabel("Reactor Valve");
        valve1_label.setBounds(121, 193, 100, 30);
        add(valve1_label);
		btnValve1 = new dab.gui.auxpanels.ValveButton(sim.getValve(1));
		btnValve1.setBounds(111, 214, 101, 71);
        add(btnValve1);
        
        
        rods_label = new JLabel("Control Rods");
        rods_label.setBounds(25, 40, 100, 30);
        add(rods_label);
        controlRodSlider = new ControlRodSlider(sim.getReactor());
        add(controlRodSlider);
        
        
		
	}
    
	/**
	 * Set the contained controlRodSlider to the specified value
	 * @param value A value between 0 and 100 (inclusive)
	 */
	public void setSliderValue(int value){
		controlRodSlider.setValue(value);
	}
    
    public void update() {
        btnPump1.update();
        btnPump2.update();
        btnValve1.update();
        btnValve2.update();
        
    }
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = new ImageIcon("resources/bckgroundBLUE.png").getImage();
        g.drawImage(img, 0, 0, 600, 600,  null);

    }
}
