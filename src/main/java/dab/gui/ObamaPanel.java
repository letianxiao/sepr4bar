package dab.gui;

import dab.engine.simulator.Simulator;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * The ObamaPanel contains information of immediate importance to the user, such
 * as component failure and critical temperatures. It contains an image of
 * obama, a speech bubble and a label for text to appear
 *
 * @author Team Haddock
 *
 */
public class ObamaPanel extends JPanel {

    Simulator simulator;
    JLabel lblObama;
    JLabel lblSpeech;
    JLabel lblWords;

    public ObamaPanel(Simulator simulator) {
        this.simulator = simulator;
        
        setBackground(Color.WHITE);
        setLayout(null);
        lblObama = new JLabel();
        lblObama.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        lblObama.setForeground(Color.BLACK);
        lblObama.setVerticalAlignment(SwingConstants.TOP);
        lblObama.setBounds(0, -5, 657, 154);
        lblObama.setBackground(Color.WHITE);
        lblObama.setIcon(new ImageIcon("resources/mainInterface/littleObama.png"));
        lblObama.setHorizontalAlignment(SwingConstants.LEFT);
        lblObama.setHorizontalTextPosition(SwingConstants.RIGHT);
        add(lblObama);

        lblWords = new JLabel();
        lblWords.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        lblWords.setForeground(Color.BLACK);
        lblWords.setVerticalAlignment(SwingConstants.TOP);
        lblWords.setHorizontalAlignment(SwingConstants.LEFT);
        lblWords.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblWords.setBounds(210, 30, 400, 400);
        lblWords.setText("!!!!!!!!!!!!!!!!");
        add(lblWords);

        lblSpeech = new JLabel();
        lblSpeech.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        lblSpeech.setBounds(140, -75, 1000, 300);
        lblSpeech.setIconTextGap(50);
        lblSpeech.setIcon(new ImageIcon("resources/mainInterface/ObamaSpeechBubble.png"));
        add(lblSpeech);
    }

    // update the panel with warnings and stuff
    public void update() {
        String temp = "<html>";
        
        if (simulator.reactorTemperature().inCelsius() > 150) {
            temp += "WARNING, " + simulator.getUsername() + ": REACTOR TEMPERATURE TOO HIGH" + "<br>";
        }

        if (simulator.condenserPressure().greaterThan(SinglePlayerInterface.CONDENSER_WARNING_PRESSURE)) {
            temp += "WARNING, " + simulator.getUsername() + ": CONDENSER PRESSURE TOO HIGH" + "<br>";
        }

        for (String failedComponent : simulator.listFailedComponents()) {
            if (failedComponent.equals("Pump 1")) {
                temp += "WARNING, " + simulator.getUsername() + ": The Water pump HAS FAILED<br>";

            } else if (failedComponent.equals("Pump 2")) {
                temp += "WARNING, " + simulator.getUsername() + ": The coolant pump HAS FAILED<br>";

            } else {
                temp += "WARNING, " + simulator.getUsername() + ": " + failedComponent + " HAS FAILED<br>";
            }
        }

        if (!simulator.getSoftFailReport().getFailBool()) {
            temp += "WARNING, " + simulator.getUsername() + ": A software failure has occured!";
        }
        temp += "</html>";
        
        lblWords.setText(temp);
    }
}
