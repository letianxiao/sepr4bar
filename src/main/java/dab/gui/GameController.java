package dab.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;



public class GameController extends JFrame {

	static{
		//Registers relevant items to repective factories
		//See their static{} blocks.
		try{
			Class.forName("dab.gui.PumpButton");
			Class.forName("dab.gui.ValveButton");
			
			Class.forName("dab.gui.Component");
			Class.forName("dab.gui.ControlRods");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException{
		//new GameController();
		//starts the splash screen first and show it for 2 seconds
        NewSplash splash = new NewSplash();
        //splash.showSplashAndExit();
        splash.movePic();
	}
}
