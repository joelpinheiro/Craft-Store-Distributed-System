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
 * StoreLogInterface.
 * @author luis
 */
public interface StoreLogInterface extends Remote{

    /**
     *
     * @param state state state
     */
    public void reportStatusEntrepreneur(String state, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param open open
     */
    public void setStoreState(String open, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param numberOfAvailableProducts numberOfAvailableProducts
     */
    public void setStorageHouseStock(int numberOfAvailableProducts, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param state state state
     * @throws java.rmi.RemoteException
     */
    public void reportStatusCustomer(int ID, String state, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param customersInStore customersInStore
     */
    public void setCustomersInside(int customersInStore, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param id id id
     * @param numberOfProductsToBuy numberOfProductsToBuy
     */
    public void addCustomerAccumulatedBoughtGoods(int id, int numberOfProductsToBuy, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param numberOfAvailableProducts numberOfAvailableProducts
     */
    public void setGoodsInDisplay(int numberOfAvailableProducts, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param b b
     */
    public void setCallTransferProducts(boolean b, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param primeMaterialAreNeeded primeMaterialAreNeeded
     */
    public void setCallTransferPrimeMaterials(boolean primeMaterialAreNeeded, int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result isThereStockInStoreHouse
     */
    public boolean isThereStockInStoreHouse(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result isTherePrimeMaterialInWorkshop
     */
    public boolean isTherePrimeMaterialInWorkshop(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result isThereProductsInWotkshop
     */
    public boolean isThereProductsInWotkshop(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result getSizeCustomers
     */
    public int getSizeCustomers(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result areCraftmenDeath
     */
    public boolean areCraftmenDeath(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result getStockFinnishedProducts
     */
    public int getStockFinnishedProducts(int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result areCustomersDead
     */
    public boolean areCustomersDead(int[] vectorClock) throws RemoteException;
}
