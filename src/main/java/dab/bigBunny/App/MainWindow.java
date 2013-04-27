/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.bigBunny.BunnyController;
import dab.bigBunny.Environment;
import dab.bigBunny.HitBoundsController;
import dab.bigBunny.TwoPlayerScreen;
import dab.engine.simulator.Simulator;
import dab.gui.InterfaceController;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;


/**
 *
 * @author eduard
 */
public class MainWindow extends JFrame {

    private HitBoundsController hitboundsController;
    
    static {
        //Registers relevant items to repective factories
        //See their static{} blocks.
        // aka java voodoo need to call the exorcist
        try {
            Class.forName("dab.gui.PumpButton");
            Class.forName("dab.gui.ValveButton");

            Class.forName("dab.gui.Component");
            Class.forName("dab.gui.ControlRods");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainWindow mw = new MainWindow();
                mw.setExtendedState(MainWindow.MAXIMIZED_BOTH);
                mw.setVisible(true);

                mw.startSinglePlayer();

            }
        });
    }
    private DaMMenu menu;
    private Component currentComponent = null;

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        

        // create the menu
        menu = new DaMMenu(this);
    }

    public void showIntro() {
        DaIntro intro = new DaIntro(this);
        changeToPanel(intro);
    }

    public void showMenu() {
        changeToPanel(menu);
    }

    public void startSinglePlayer() {
        Simulator sim = new Simulator();
        sim.setUsername("willy-wanka");
        startSinglePlayer(sim);
        
    }
    
    public void startSinglePlayer(Simulator sim) {
        InterfaceController ic = InterfaceController.instance();
        ic.setup(sim);
        changeToPanel(ic);
    }

    public void startTwoPlayer() {
        //BunnyInterface bi = new BunnyInterface();

        Environment env = new Environment(getWidth(), getHeight());
        hitboundsController = new HitBoundsController();
        BunnyController bc = new BunnyController(env, hitboundsController, new Point(100, 100), 10);
        bc.setBounds(new Rectangle(getWidth(), getHeight()));
        TwoPlayerScreen tps = new TwoPlayerScreen(bc, env);
        
        changeToPanel(tps);
    }

    private void changeToPanel(Component p) {
        if (currentComponent != null) {
            currentComponent.setVisible(false);
            getContentPane().remove(currentComponent);
        }
        getContentPane().add(p);
        currentComponent = p;
        p.setVisible(true);
        
        // put the new component in focus
        p.requestFocusInWindow();

    }
}