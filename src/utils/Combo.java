/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;

/**
 *
 * @author joelpinheiro
 */
public class Combo implements Serializable{
    Object object;
    int[] vectorClock;

    public Combo(Object object, int[] vectorClock) {
        this.object = object;
        this.vectorClock = vectorClock;
    }

    public Combo() {
    }

    public Object getObject() {
        return object;
    }

    public int[] getVectorClock() {
        return vectorClock;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setVectorClock(int[] vectorClock) {
        this.vectorClock = vectorClock;
    }
    
    public int[] updateVectorClock(int[] monitorVectorClock, int[] threadVectorClock)
    {
        int[] tmp = new int[monitorVectorClock.length];
        
        for(int i = 0; i < monitorVectorClock.length ; i++)
        {
            if(monitorVectorClock[i] > threadVectorClock[i])
                tmp[i] = monitorVectorClock[i];
            else
                tmp[i] = threadVectorClock[i];
        }
        
        return tmp;
    }
    
}
