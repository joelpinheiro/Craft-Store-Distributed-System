/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store.intefaces;

/**
 *
 * @author luis, joelpinheiro
 */
public interface CustomerStoreInterface {

    /**
     *
     * @param ID
     */
    public void goShopping(int ID);

    /**
     *
     * @return
     */
    public boolean isDoorOpen();

    /**
     *
     * @param ID
     */
    public void enterShopCustomer(int ID);

    /**
     *
     * @param ID
     * @param numberOfProductsToBuy
     */
    public void iWantThis(int ID, int numberOfProductsToBuy);

    /**
     *
     * @param ID
     */
    public void exitShopCustomer(int ID);

    /**
     *
     * @param ID
     * @param insideTheStore
     */
    public void endOperationCustomer(int ID, boolean insideTheStore);

    /**
     *
     * @return
     */
    public boolean perusingAround();

    /**
     *
     * @param ID
     * @param insideTheStore
     * @return
     */
    public boolean canEndOperationCustomer(int ID, boolean insideTheStore);
}
