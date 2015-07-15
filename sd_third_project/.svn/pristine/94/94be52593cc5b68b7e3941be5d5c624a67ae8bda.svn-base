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
 * WorkshopLogInterface.
 * @author luis
 */
public interface WorkshopLogInterface extends Remote{

    /**
     *
     * @param ID ID ID
     * @param state state state
     * @throws java.rmi.RemoteException
     */
    public void reportStatusCraftman(int ID, String state, int[] vectorClock) throws RemoteException;

    /**
     *
     * @return return result isThereStockInStoreHouse
     * @throws java.rmi.RemoteException
     */
    public boolean isThereStockInStoreHouse(int[] vectorClock) throws RemoteException;

    /**
     *
     * @param state state state
     * @throws java.rmi.RemoteException
     */
    public void reportStatusEntrepreneur(String state, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param stockPrimeMaterials stockPrimeMaterials
     * @throws java.rmi.RemoteException
     */
    public void setStockPrimeMaterials(int stockPrimeMaterials, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param entrepreneurCalledReplenishPrimeMaterials entrepreneurCalledReplenishPrimeMaterials
     * @throws java.rmi.RemoteException
     */
    public void setCallTransferPrimeMaterials(boolean entrepreneurCalledReplenishPrimeMaterials, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param primeMaterialStockResuply primeMaterialStockResuply
     * @throws java.rmi.RemoteException
     */
    public void setResuplyPrimeMaterials(int primeMaterialStockResuply, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param accumulatedSupliedPrimeMaterials accumulatedSupliedPrimeMaterials
     * @throws java.rmi.RemoteException
     */
    public void setTotalAmountPrimeMaterialsSupplied(int accumulatedSupliedPrimeMaterials, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param stockManufacturedMaterials stockManufacturedMaterials
     * @throws java.rmi.RemoteException
     */
    public void setStockFinnishedProducts(int stockManufacturedMaterials, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param accumulatedProducedProducts accumulatedProducedProducts
     * @throws java.rmi.RemoteException
     */
    public void setTotalAmountProductsManufactured(int accumulatedProducedProducts, int[] vectorClock) throws RemoteException;

    /**
     *
     * @param ID ID ID
     * @param craftsmanAccumulatedManufacturedProduct craftsmanAccumulatedManufacturedProduct
     * @throws java.rmi.RemoteException
     */
    public void addCraftsmanAccManProducts(int ID, int craftsmanAccumulatedManufacturedProduct, int[] vectorClock) throws RemoteException;

}
