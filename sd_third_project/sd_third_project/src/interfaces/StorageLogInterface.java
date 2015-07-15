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
 * StorageLogInterface.
 * @author luis
 */
public interface StorageLogInterface extends Remote{

    /**
     *
     * @param stockPrimeMaterials stockPrimeMaterials
     * @param state state
     */
    public void setStorageHouseStock(int stockPrimeMaterials, String state, int[] vectorClock) throws RemoteException;
    
}