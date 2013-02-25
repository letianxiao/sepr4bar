package dab.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The InfoPanel contains a text area for holding important information about the
 * system, such as temperatures, pressures and power levels.
 * 
 * @author Team Haddock
 *
 */
public class InfoPanel extends JPanel {
	JLabel screenText;

	public InfoPanel(){
		screenText =new JLabel();
	        setBackground(new Color(30, 144, 255));
	        setLayout(null);
	        screenText.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
	        add(screenText);

	        screenText.setForeground(Color.GREEN);
	        screenText.setVerticalAlignment(SwingConstants.TOP);
	        screenText.setBounds(35, 22, 400, 258);
	        screenText.setBackground(Color.BLACK);
	        screenText.setHorizontalAlignment(SwingConstants.LEFT);
	        screenText.setHorizontalTextPosition(SwingConstants.RIGHT);
	        screenText.setText("<html>Welcome to Team Haddock's game <br></html>");
	        screenText.updateUI();

	        JLabel lblComputerScreen = new JLabel("");
	        lblComputerScreen.setForeground(Color.GREEN);
	        lblComputerScreen.setVerticalAlignment(SwingConstants.TOP);
	        lblComputerScreen.setBounds(14, 11, 400, 300);
	        lblComputerScreen.setBackground(Color.BLACK);
	        lblComputerScreen.setIconTextGap(50);
	        lblComputerScreen.setIcon(new ImageIcon("resources/splash/CompyScreeny.png"));
	        add(lblComputerScreen);
	}
	
	public void paintComponent(Graphics g){
        Image img = new ImageIcon("resources/bckgroundBLUE.png").getImage();
        g.drawImage(img, 0, 0, 600, 600,  null);
    }
	
	public void setText(String str){
		screenText.setText(str);
	}
}
