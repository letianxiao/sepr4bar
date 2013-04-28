package dab.gui.gamepanel;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Displays game over dialogue at the end of the game.
 * @author Team Haddock
 *
 */
public class GameOver{
    final JDialog dialog = new JDialog();

    public GameOver(JOptionPane optionPane){
        dialog.setContentPane(optionPane);
        dialog.setBounds(300, 200, 500, 500);
        dialog.setBackground(Color.blue);
        dialog.setAlwaysOnTop(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Sets the dialogue visibility to false.
     */
    public void setInvisible(){
        dialog.setVisible(false);
    }

    /**
     * 
     * @return whether the dialogue box is visible.
     */
    public boolean isShowing(){
        return dialog.isShowing();
    }
}
