package dab.gui;

import dab.engine.simulator.Simulator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * The InfoPanel contains a text area for holding important information about
 * the system, such as temperatures, pressures and power levels.
 *
 * @author Team Haddock
 *
 */
public class InfoPanel extends JPanel {

    JLabel screenText;
    Image background;

    public InfoPanel() {
        setLayout(new BorderLayout());
        screenText = new JLabel();
        screenText.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        

        screenText.setForeground(Color.GREEN);
        screenText.setVerticalAlignment(SwingConstants.CENTER);

        screenText.setBackground(Color.BLACK);

        screenText.setBorder(BorderFactory.createMatteBorder(25, 25, 18, 18, new ImageIcon("resources/bckgroundBLUE.png")));

        screenText.setOpaque(true);
        screenText.setHorizontalAlignment(SwingConstants.CENTER);
        screenText.setHorizontalTextPosition(SwingConstants.RIGHT);
        
        add(screenText);
    }

    public void setText(String str) {
        screenText.setText(str);
    }

    public void updateText(Simulator simulator) {
        setText("<html>Control Rod Position " + (Integer.parseInt((simulator.controlRodPosition().toString())) * 2) + "<br>" + //this lines throws a NullPointerException at the moment
                "<br>" + "Reactor Water Level " + simulator.reactorWaterLevel() + "<br>" + "Reactor Temperature " + simulator.reactorTemperature()
                + "<br>" + "Reactor Pressure " + simulator.reactorPressure() + "<br>" + "<br>" + "Condenser Water Level " + simulator.condenserWaterLevel()
                + "<br>" + "Condenser Temperature " + simulator.condenserTemperature() + "<br>" + "Condenser Pressure " + simulator.condenserPressure()
                + "<br>" + "<br>" + "ENERGY GENERATED " + simulator.energyGenerated() + "</html>");
    }
}
