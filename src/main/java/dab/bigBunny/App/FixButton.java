/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.views.FailableComponentView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author eduard
 */
public class FixButton extends JButton {

    FailableComponentView linkedComponent;
    int dmg = 0;
    
    public FixButton(FailableComponentView component) {
        this.linkedComponent = component;
        setSize(80, 30);
        setIcon(new ImageIcon(FixButton.class.getResource("wrench.png")));
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);

        
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    linkedComponent.fixDamage();
                } catch (CannotRepairException e) {
                    System.err.println(e);
                }
                update();
            }
        });
    }
    
    public void update() {
        setText(String.valueOf(linkedComponent.getDamage()));
        if (linkedComponent.hasFailed()) {
            setVisible(true);
            
        } else {
            setVisible(true);
        }
        
    }
}
