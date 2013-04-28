package dab.gui;

import dab.engine.simulator.PhysicalModel;
import dab.engine.simulator.Simulator;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Reactor panel holds all information regarding plant images and fix buttons
 *
 * @author mb941
 *
 */
public class ReactorPanel extends JPanel {

    private JLabel reactor_bubbles, systemImage;
    private ComponentController pump1, pump2, valve1, valve2;
    private ComponentController reactor, condenser, turbine;
    private DynamicImage condenserPipe, generator, controlRods;
    private FixButton condenserFixButton, turbineFixButton, pump1FixButton, pump2FixButton;
    private Simulator simulator;
    

    public ReactorPanel(Simulator simulator){
    	this.simulator = simulator;
        
        setBackground(Color.WHITE);
    	setLayout(null);
    	 systemImage = new JLabel("");
         systemImage.setBounds(0, -19, 970, 635);
         add(systemImage);
         systemImage.setHorizontalAlignment(SwingConstants.CENTER);
         systemImage.setAutoscrolls(true);
         systemImage.setIcon(new ImageIcon("resources/mainInterface/BACKGROUNDCOMPONENTS.png"));
         
         /*turbineFixButton = new TurbineFixButton(360, 137, simulator.getTurbine());
         turbineFixButton.attachFixObserver(InterfaceController.instance());
         systemImage.add(turbineFixButton);
         condenserFixButton = new CondenserFixButton(450, 274, simulator.getCondenser());
         condenserFixButton.attachFixObserver(InterfaceController.instance());
         systemImage.add(condenserFixButton);
         pump1FixButton = new PumpFixButton(342, 550, simulator.getPump(1));
         pump1FixButton.attachFixObserver(InterfaceController.instance());
         systemImage.add(pump1FixButton);
         pump2FixButton = new PumpFixButton(660, 485, simulator.getPump(2));
         pump2FixButton.attachFixObserver(InterfaceController.instance());
         systemImage.add(pump2FixButton);*/
         
         controlRods = DynamicImageFactory.instance().createButton("ControlRods", 44, 250, 300, 300, "resources/mainInterface/RODS_SCALED.png");
         systemImage.add(controlRods);
         
         reactor_bubbles = new JLabel();
         reactor_bubbles.setBounds(40, 250, 229, 160);
         reactor_bubbles.setIcon(new ImageIcon("resources/mainInterface/bubbles_SCALED.gif"));
         reactor_bubbles.setIconTextGap(0);
         reactor_bubbles.setVisible(true);
         systemImage.add(reactor_bubbles);
         
         //This shouldn't change in future.
         turbine = new ComponentController();
         turbine.addImage(443, -42, 300, 300, "resources/mainInterface/movingturbine.gif", systemImage);
         turbine.showImage(0);
         
         pump2 = new ComponentController();
         pump2.addImage(666, 519, 73, 73, "resources/mainInterface/brokenComponents/BrokenCondenserPump.png", systemImage);
         pump2.addImage(666, 510, 90, 90, "resources/mainInterface/MOVINGPUMP_CON_SCALED.gif", systemImage);
         pump2.addImage(666, 519, 76, 73, "resources/mainInterface/CONPUMP_OFF.png", systemImage);
         //0=broken, 1=on, 2=off
         pump2.showImage(1);
         
         
         //This shouldn't change either
         condenserPipe = DynamicImageFactory.instance().createButton("Component", 506, 204, 515, 515, "resources/mainInterface/pipe.png");
         systemImage.add(condenserPipe);
         condenserPipe.setVisible(true);
         
         pump1 = new ComponentController();
         pump1.addImage(352, 474, 73, 73, "resources/mainInterface/brokenComponents/BrokenWaterPump.png", systemImage);
         pump1.addImage(352, 466, 90, 90, "resources/mainInterface/MOVINGPUMP_MAIN_SCALED.gif", systemImage);
         pump1.addImage(352, 470, 81, 80, "resources/mainInterface/MAINPUMP_OFF.png", systemImage);
         //0=broken, 1=on, 2=off
         pump1.showImage(1);
         
        
         
         reactor = new ComponentController();
         reactor.addImage(-73, 133, 420, 464, "resources/mainInterface/brokenComponents/BROKENREACTOR_SCALED.png", systemImage);
         //0=broken (anything else = working)
         reactor.showImage(1);
         
         //This shouldn't change in future
         generator = DynamicImageFactory.instance().createButton("Component", 590,-42,300,300, "resources/mainInterface/movingGenerator.gif");
         systemImage.add(generator);
         generator.setVisible(true);
         
         condenser = new ComponentController();
         condenser.addImage(400, 174, 515, 516, "resources/mainInterface/brokenComponents/BROKENCONDENSER_SCALED.png", systemImage);
         //0=broken (anything else = working)
         condenser.showImage(1);
         
         turbine = new ComponentController();
         turbine.addImage(404, -137, 515, 516, "resources/mainInterface/brokenComponents/BROKENTURBINE_SCALED.png",systemImage);
         
         valve1 = new ComponentController();
         valve1.addImage(305, -154, 515, 516, "resources/mainInterface/CLOSEDVALVE_SCALED.png", systemImage);
         valve1.addImage(305, -158, 515, 516, "resources/mainInterface/OPENVALVE_SCALED.png", systemImage);
         //0=closed, 1=open
         valve1.showImage(1);
         
         valve2 = new ComponentController();
         valve2.addImage(525, -18, 515, 516, "resources/mainInterface/CLOSEDVALVE_SCALED.png", systemImage);
         valve2.addImage(525, -22, 515, 516, "resources/mainInterface/OPENVALVE_SCALED.png", systemImage);
         //0=closed, 1=open
         valve2.showImage(1);    
    }
    
    public void failTurbineFixButton(){
    	turbineFixButton.fail();
    }
    public void failCondenserFixButton(){
    	condenserFixButton.fail();
    }
    public void failPump1FixButton(){
    	pump1FixButton.fail();
    }
    public void failPump2FixButton(){
    	pump2FixButton.fail();
    }
    
    public boolean getTurbineFixed(){
    	return turbineFixButton.getFixed();
    }
    public boolean getCondenserFixed(){
    	return condenserFixButton.getFixed();
    }
    public boolean getPump1Fixed(){
    	return pump1FixButton.getFixed();
    }
    public boolean getPump2Fixed(){
    	return pump2FixButton.getFixed();
    }

    public void setValve1State(int state){
        valve1.showImage(state);
    }

    public void setValve2State(int state) {
        valve2.showImage(state);
    }

    public void setPump1State(int state) {
        pump1.showImage(state);
    }

    public void setPump2State(int state) {
        pump2.showImage(state);
    }

    public void setReactorState(int state) {
        reactor.showImage(state);
    }

    public void setCondenserState(int state) {
        condenser.showImage(state);
    }

    public void setTurbineState(int state) {
        turbine.showImage(state);
    }

    public void setControlRodHeight(int height) {
        //TODO: Fix me later
        controlRods.setBounds(44, (int) (controlRods.getHeight() - height / 1.5 - 87), 300, 300);
    }
}
