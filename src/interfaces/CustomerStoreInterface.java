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
 * CustomerStoreInterface.
 * @author luis, joelpinheiro
 */
public interface CustomerStoreInterface extends Remote{

    /**
     *
     * @param ID ID ID
     * @param state state state
     * @param vectorClock
     * @param vectorClockPosition
     * @return 
     * @throws java.rmi.RemoteException
     */
    public Combo goShopping(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param vectorClock
     * @param vectorClockPosition
     * @return return result
     * @throws java.rmi.RemoteException
     */
    public Combo isDoorOpen(int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param state state state
     */
    public Combo enterShopCustomer(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param numberOfProductsToBuy numberOfProductsToBuy
     * @param state state state
     */
    public Combo iWantThis(int ID, int numberOfProductsToBuy, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param state state state
     */
    public Combo exitShopCustomer(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param insideTheStore insideTheStore
     * @param state state state
     */
    public Combo endOperationCustomer(int ID, boolean insideTheStore, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @return return result perusingAround
     */
    public Combo perusingAround(int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param insideTheStore insideTheStore
     * @param state state state
     * @return return result canEndOperationCustomer
     */
    public Combo canEndOperationCustomer(int ID, boolean insideTheStore, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;
}
