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
 * EntrepreneurWorkshopInterface.
 * @author luis
 */
public interface EntrepreneurWorkshopInterface extends Remote{

    /**
     *
     * @param amountProductsToPickUp amountProductsToPickUp
     * @param state state
     * @return return result
     */
    public Combo collectABatchOfProducts(int amountProductsToPickUp, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param numberOfReplenish numberOfReplenish
     * @param state state
     */
    public Combo addPrimeMaterial(int numberOfReplenish, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;
    
}
