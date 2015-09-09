/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import utils.Combo;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * EntrepreneurStorageInterface.
 * @author luis
 */
public interface EntrepreneurStorageInterface extends Remote{
    
    /**
     *
     * @return return result
     * @throws java.rmi.RemoteException
     */
    public Combo getStock(int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param numberOfReplenish numberOfReplenish
     * @param state state 
     * @return return result
     */
    public Combo removePrimeMaterial(int numberOfReplenish, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;
    
}
