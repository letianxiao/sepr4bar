/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.views.FailableComponentView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author eduard
 */
public class FixButton extends JButton {

    FailableComponentView component;
    int dmg = 0;
    
    public FixButton(FailableComponentView component) {
        this.component = component;
        
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dmg--;
            }
        });
    }
    
    public void update() {
        if (component.hasFailed()) {
            setVisible(true);
            //setText(component.getDamage());
            dmg = 10;
            setText(String.valueOf(dmg));
        } else {
            setVisible(false);
        }
        
    }
}
