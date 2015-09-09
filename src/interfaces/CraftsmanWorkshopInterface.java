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
 * CraftsmanWorkshopInterface.
 * @author luis
 */
public interface CraftsmanWorkshopInterface extends Remote{

    /**
     *
     * @param ID ID
     * @param state state
     * @return return result
     */
    public Combo isThereWorkLeft(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect amountToCollect
     * @param state state
     * @return return result
     */
    public Combo checkForMaterials(int ID, int amountToCollect, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param amountToCollect amountToCollect
     * @param state state
     * @return return result 
     */
    public Combo collectMaterials(int ID, int amountToCollect, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param productStep productStep
     * @param state state
     * @return return result
     */
    public Combo goToStore(int ID, int productStep, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param state state
     */
    public Combo batchReadyForTransfer(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param state state
     */
    public Combo backToWork(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param state state
     */
    public Combo primeMaterialsNeeded(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param productStep productStep
     * @param state state
     */
    public Combo prepareToProduce(int ID, int productStep, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;

    /**
     *
     * @param ID ID
     * @param state state
     */
    public Combo endOperationCraftsman(int ID, String state, int[] vectorClock, int vectorClockPosition) throws RemoteException;
}
