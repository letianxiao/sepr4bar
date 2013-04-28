package dab.gui.auxpanels;

import dab.engine.simulator.UserCommands;
import dab.engine.simulator.views.ReactorView;
import dab.engine.utilities.Percentage;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * A slider for the ButtonPanel that controls the height of ControlRods
 * in the system.
 * @author Team Haddock
 * 
 *
 */
public class ControlRodSlider extends JSlider{

    ReactorView reactor;
    /**
     * 
     * @param
     * @param
     */
    public ControlRodSlider(ReactorView r) {
        super(JSlider.VERTICAL, 0, 100, 0);
        this.reactor = r;
        setBounds(10, 80, 100, 200);
        setOpaque(false);
        setMajorTickSpacing(25);
        setPaintTicks(true);
        setPaintLabels(true);
        setUI(new MySliderUI(this));
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!getValueIsAdjusting()){
                    //notify observer only when the rods are released
                    reactor.moveControlRods(new Percentage(getValue()/2));
                    // int y = control_rods.getHeight();
                    //  control_rods.setBounds(44,(int)( y-getValue()/1.5-87), 300, 300);
                }
            }
        });
    }
    /**
     *
     * A private class used to overwrite the natural look of a basic slider
     */
    private class MySliderUI extends BasicSliderUI {
        Image knobImage;
        public MySliderUI( JSlider aSlider ) {
            super( aSlider );
            try {
                this.knobImage = ImageIO.read( new File("resources/controlPanel/orangeSlider.png") );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        @Override
        public void scrollByUnit(int direction)
        {

        }
        @Override
        public void paintThumb(Graphics g)  {
            // overwrite the default slider icon
            g.drawImage( this.knobImage, thumbRect.x-10, thumbRect.y-1, 40, 16, null );
            repaint();
        }
    }
}
