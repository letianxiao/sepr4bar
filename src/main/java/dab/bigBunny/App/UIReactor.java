/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.simulator.views.FailableComponentView;
import java.awt.Point;

/**
 *
 * @author eduard
 */
public class UIReactor extends UIComponent {

    public UIReactor(GamePanel parent, FailableComponentView component, Point location, String stdImgPath, String dmgImgPath) {
        super(parent, component, location, stdImgPath, dmgImgPath);
        
        //also do stuff regarding the control rods and what not
    }
    
}
