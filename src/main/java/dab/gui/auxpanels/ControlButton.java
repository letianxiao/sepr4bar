package dab.gui.auxpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public abstract class ControlButton extends JToggleButton {

    private Icon activePressed, brokenIcon, disabledIcon;

    public ControlButton() {

        try {
            activePressed = new ImageIcon(ImageIO.read(ControlButton.class.getResourceAsStream("active.png")));
            disabledIcon = new ImageIcon(ImageIO.read(ControlButton.class.getResourceAsStream("disabled.png")));

            brokenIcon = new ImageIcon(ImageIO.read(ControlButton.class.getResourceAsStream("broken.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    protected void setFailed() {
        changeState(brokenIcon, false, false, "Failed!");
    }

    protected void setStatus(boolean status) {
        if (status == true) {
            changeState(activePressed, true, true, "ON");
        } else {
            changeState(disabledIcon, true, false, "OFF");
        }
    }

    protected abstract void onClick();

    public abstract void update();
}
