/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_customer.interfaces;

/**
 * CustomerStoreInterface.
 * @author luis, joelpinheiro
 */
public interface CustomerStoreInterface {

    /**
     *
     * @param ID ID
     */
    public void goShopping(int ID);

    /**
     *
     * @return return result
     */
    public boolean isDoorOpen();

    /**
     *
     * @param ID ID
     */
    public void enterShopCustomer(int ID);

    /**
     *
     * @param ID ID
     * @param numberOfProductsToBuy numberOfProductsToBuy
     */
    public void iWantThis(int ID, int numberOfProductsToBuy);

    /**
     *
     * @param ID ID
     */
    public void exitShopCustomer(int ID);

    /**
     *
     * @param ID ID
     * @param insideTheStore insideTheStore
     */
    public void endOperationCustomer(int ID, boolean insideTheStore);

    /**
     *
     * @return return result
     */
    public boolean perusingAround();

    /**
     *
     * @param ID ID
     * @param insideTheStore insideTheStore
     * @return return result
     */
    public boolean canEndOperationCustomer(int ID, boolean insideTheStore);
}
