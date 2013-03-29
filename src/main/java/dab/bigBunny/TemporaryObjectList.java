/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.util.LinkedList;

/**
 *
 * @author eduard
 */
public class TemporaryObjectList<T extends TemporaryObject> {
    LinkedList<T> list;
    
    public TemporaryObjectList() {
        list = new LinkedList<T>();
    }
    
    public void step() {
        for (TemporaryObject o : list) {
            o.step();
        }
        
        while(!list.isEmpty() && !list.getFirst().isAlive()) {
            list.removeFirst();
        }
    }
    
    public void add(T e) {
        list.addLast(e);
    }
    
    public int size() {
        return list.size();
    }
    
    /**
     * 
     * @return the LinkedList, all objects are alive
     */
    public LinkedList<T> getRawList() {
        return list;
    }
}
