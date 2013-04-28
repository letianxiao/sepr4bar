package dab.bigBunny.App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public abstract class ControlButton extends JToggleButton {

    private Icon activePressed, brokenIcon, disabledIcon;
    
    public ControlButton() {
        activePressed = new ImageIcon(ControlButton.class.getResource("active.png"));
        disabledIcon = new ImageIcon(ControlButton.class.getResource("disabled.png"));
        
        brokenIcon = new ImageIcon(ControlButton.class.getResource("broken.png"));
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                onClick();
            }
        });
        
    }
    
    private void changeState(Icon icon, boolean enabled, boolean selected, String text) {
        setIcon(icon);
        setEnabled(enabled);
        setSelected(selected);
        setText(text);
    }
    
    protected void setBroken() {
        changeState(brokenIcon, false, false, "Failed!");
    }
    
    protected void setActive() {
        changeState(activePressed, true, true, "ON");
    }
    
    protected void setDisabled() {
        changeState(disabledIcon, true, false, "OFF");
    }
    
    protected abstract void onClick();
}
