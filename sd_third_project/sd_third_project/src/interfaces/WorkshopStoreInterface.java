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
 * WorkshopStoreInterface.
 * @author luis
 */
public interface WorkshopStoreInterface extends Remote{

    /**
     *
     * @param ID ID ID
     */
    public Combo needMorePrimeMaterials(int ID, int[] vectorClock, int vectorClockPosition) throws RemoteException;
    
    /**
     *
     * @param ID ID ID
     */
    public Combo callEntrepreneurToCollectProducts(int ID, int[] vectorClock, int vectorClockPosition) throws RemoteException;
    
}
