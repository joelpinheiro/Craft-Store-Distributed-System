/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log.interfaces;

/**
 *
 * @author luis
 */
public interface StoreLogInterface {

    /**
     *
     */
    public void reportStatusEntrepreneur();

    /**
     *
     * @param open
     */
    public void setStoreState(String open);

    /**
     *
     * @param numberOfAvailableProducts
     */
    public void setStorageHouseStock(int numberOfAvailableProducts);

    /**
     *
     */
    public void reportStatusCustomer();

    /**
     *
     * @param customersInStore
     */
    public void setCustomersInside(int customersInStore);

    /**
     *
     * @param id
     * @param numberOfProductsToBuy
     */
    public void addCustomerAccumulatedBoughtGoods(int id, int numberOfProductsToBuy);

    /**
     *
     * @param numberOfAvailableProducts
     */
    public void setGoodsInDisplay(int numberOfAvailableProducts);

    /**
     *
     * @param b
     */
    public void setCallTransferProducts(boolean b);

    /**
     *
     * @param primeMaterialAreNeeded
     */
    public void setCallTransferPrimeMaterials(boolean primeMaterialAreNeeded);

    /**
     *
     * @return
     */
    public boolean isThereStockInStoreHouse();

    /**
     *
     * @return
     */
    public boolean isTherePrimeMaterialInWorkshop();

    /**
     *
     * @return
     */
    public boolean isThereProductsInWotkshop();

    /**
     *
     * @return
     */
    public int getSizeCustomers();

    /**
     *
     * @return
     */
    public boolean areCraftmenDeath();

    /**
     *
     * @return
     */
    public int getStockFinnishedProducts();

    /**
     *
     * @return
     */
    public boolean areCustomersDead();
}
