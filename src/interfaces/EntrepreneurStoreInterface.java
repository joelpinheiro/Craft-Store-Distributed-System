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
public interface EntrepreneurStoreInterface extends Remote{

    /**
     *
     * @param state state state
     */
    public Combo addressCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo sayGoodByeToCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     * @param vectorClock
     * @param vectorClockPosition
     * @return 
     * @throws java.rmi.RemoteException
     */
    public Combo closeTheDoor(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     * @return return result customerInShop
     */
    public Combo customerInShop(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state
     */
    public Combo prepareToLeave(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param amountProductsToPickUpFromWorkshop amountProductsToPickUpFromWorkshop
     * @param state state state
     */
    public Combo addProducts(int amountProductsToPickUpFromWorkshop, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo visitSuppliers(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo returnToShop(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     * @return return result appraiseSit
     */
    public Combo appraiseSit(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo replenishStock(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo endOperationEntrepreneur(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state
     */
    public Combo serviceCustomer(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param state state state
     */
    public Combo prepareToWork(String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;


}
